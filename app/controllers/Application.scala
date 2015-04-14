package controllers

import java.util.Date

import models.{Story, Task, User}
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
    Redirect(routes.Application.login)
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
        Redirect(routes.Application.login())
      }
    )
  }
  def deleteUser(username: String)= Action {
    User.delete(username)
    Redirect(routes.Application.users)
  }

  val loginForm: Form[(String, String)] = Form(
    tuple(
    "username" -> nonEmptyText,
    "password" -> nonEmptyText
    )
  )

  def login=Action{
    Ok(views.html.login(loginForm))
  }

  def authenticate = Action{ implicit request =>
    val (user,pass): (String, String) = loginForm.bindFromRequest.get

    User.all.filter(u => u.username == user && u.password == pass) match {

      case Nil => {
        Ok(views.html.users(User.all(), userForm))
      }
      case users: List[User] =>{
        val user = users.head
        Ok(views.html.welcome(user, Story.all))
      }


    }

  }
  val storyForm: Form[(String, String)] = Form(
    tuple(

      "title" -> nonEmptyText,
      "text" -> nonEmptyText
    )
  )

  def stories=Action{ implicit request =>
    Ok(views.html.newstory(storyForm))
  }

  def makeStory()= Action {implicit request =>
    val (title,text): (String, String) = storyForm.bindFromRequest.get
    Story.create(title,User.all().head.username, text, 0 , new Date())
    Ok(views.html.welcome(User.all().head, Story.all))
  }





}