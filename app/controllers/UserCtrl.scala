package controllers

import controllers.Application._
import models.{StoryDAO, User, UserDAO}
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
    Ok(views.html.users(UserDAO.all(), userForm))
  }

  def newUser= Action{ implicit request =>
    userForm.bindFromRequest.fold(
      (errors) => BadRequest(views.html.users(UserDAO.all, errors)),
      data => {
        UserDAO.create(data._1, data._2)
        Redirect(routes.Application.index())
      }
    )
  }
  def deleteUser(username: String)= Action {
    UserDAO.delete(username)
    Redirect(routes.UserCtrl.users)
  }


  def authenticate = Action{ implicit request =>
    val (user,pass): (String, String) = loginForm.bindFromRequest.get

    UserDAO.all.filter(u => u.username == user && u.password == pass) match {

      case Nil => {
        Ok(views.html.users(UserDAO.all(), userForm))
      }
      case users: List[User] =>{

        userLogin = Some(users.head)
        Ok(views.html.allstories(userLogin.get, StoryDAO.all))
      }


    }

  }



}
