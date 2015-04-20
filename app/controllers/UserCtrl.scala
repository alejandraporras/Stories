package controllers

import controllers.Application._
import models.{Story, User}
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.Action


object UserCtrl {

  val userForm: Form[(String, String)] = Form(
    tuple(
      "username" -> nonEmptyText,
      "password" -> nonEmptyText
    )
  )


  def users = Action{
    Ok(views.html.users(User.all(), userForm))
  }

  def newUser= Action{ implicit request =>
    userForm.bindFromRequest.fold(
      (errors) => BadRequest(views.html.users(User.all, errors)),
      data => {
        User.create(data._1, data._2)
        Redirect(routes.Application.index())
      }
    )
  }
  def deleteUser(username: String)= Action {
    User.delete(username)
    Redirect(routes.UserCtrl.users)
  }


  def authenticate = Action{ implicit request =>
    val (user,pass): (String, String) = loginForm.bindFromRequest.get

    User.all.filter(u => u.username == user && u.password == pass) match {

      case Nil => {
        Ok(views.html.users(User.all(), userForm))
      }
      case users: List[User] =>{

        userLogin = Some(users.head)
        Ok(views.html.allstories(userLogin.get, Story.all))
      }


    }

  }



}
