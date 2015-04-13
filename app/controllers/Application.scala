package controllers

import models.{Task, User}
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._

object Application extends Controller {
  val taskForm = Form(
    "label" -> nonEmptyText
  )

  def index = Action {
    //Redirect(routes.Application.tasks)
    // We want to redirect to the users page
    Redirect(routes.Application.users)
  }

  def tasks = Action{
    Ok(views.html.index(Task.all(), taskForm))
  }

  def newTask = Action{ implicit request =>
    taskForm.bindFromRequest.fold(
      (errors: Form[String]) => BadRequest(views.html.index(Task.all, errors)),
      (label: String) => {
        Task.create(label)
        Redirect(routes.Application.tasks())
      }
    )
  }

  def deleteTask(id: Long)=Action {
    Task.delete(id)
    Redirect(routes.Application.tasks)
  }

  //####### USERS

  val userForm =  Form(
    mapping(
      "username" -> text,
      "password" -> text
    )(User.apply)(User.unapply)
  )


  def users = Action{
    Ok(views.html.users(User.all(), userForm))
  }
  /*
  def newUser = Action{
    User.create("pepe")
    Ok("USUARIO:S " + User.all().size)
  }*/


  def newUser= Action{ implicit request =>
    userForm.bindFromRequest.fold(
      (errors) => BadRequest(views.html.users(User.all, errors)),
      data => {
        User.create(data.username, data.password)
        Redirect(routes.Application.users)
      }
    )
  }
  def deleteUser(username: String)= Action {
    User.delete(username)
    Redirect(routes.Application.users)
  }


}