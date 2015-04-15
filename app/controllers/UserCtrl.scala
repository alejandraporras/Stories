package controllers

import controllers.Application._
import models.{Story, User}
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.Action

/**
 * Created by alejandra.porras on 15/04/15.
 */
object UserCtrl {
  val userForm: Form[User] =  Form(
    mapping(
      "username" -> nonEmptyText,
      "password" -> nonEmptyText
    )(User.apply)(User.unapply)
  )


  def users = Action{
    Ok(views.html.users(User.all(), userForm))
  }

  def newUser= Action{ implicit request =>
    userForm.bindFromRequest.fold(
      (errors) => BadRequest(views.html.users(User.all, errors)),
      data => {
        User.create(data.username, data.password)
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
        Ok(views.html.welcome(userLogin.get, Story.all))
      }


    }

  }



}
