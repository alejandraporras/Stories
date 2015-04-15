package controllers

import models.User
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._

object Application extends Controller {

  var userLogin: Option[User] = None

  val taskForm = Form(
    "label" -> nonEmptyText
  )

  def index = Action {
    //Redirect(routes.Application.tasks)
    // We want to redirect to the users page
    Ok(views.html.index(loginForm))
  }

  val loginForm: Form[(String, String)] = Form(
    tuple(
      "username" -> nonEmptyText,
      "password" -> nonEmptyText
    )
  )

}