package controllers

import java.util.Date

import controllers.Application._
import models.Story
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.{Action, Controller}


object  StoryCtrl extends Controller{


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
    Story.create(title,userLogin.get.username, text, 0 , new Date())
    Ok(views.html.welcome(userLogin.get, Story.all))
  }

  def story(id: Long)= Action {implicit request =>
    val story = Story.getById(id)
    Ok(views.html.story(story))
  }

  def allStories() = Action {
    Ok(views.html.welcome(Application.userLogin.get, Story.all))
  }
  def incrementPoints(id: Long) = Action { implicit request =>
    var story = Story.getById(id)
    Story.update(id,  story.points + 1)
    Ok(views.html.welcome(userLogin.get, Story.all))

  }


}
