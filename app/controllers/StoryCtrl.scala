package controllers

import java.util.Date

import controllers.Application._
import models.Story
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.{Action, Controller}
import models.User


object  StoryCtrl extends Controller{


  val storyForm: Form[(String, String)] = Form(
    tuple(

      "title" -> nonEmptyText,
      "text" -> nonEmptyText
    )
  )

  def showStoryForm=Action{ implicit request =>
    Ok(views.html.newstory(storyForm))
  }

  def makeStory()= Action {implicit request =>
    val (title,text): (String, String) = storyForm.bindFromRequest.get
    val idStory: Option[Long] = Story.create(title,userLogin.get.username, text, 0 , new Date())
    val story: Story = Story.getById(idStory.get)
    User.addStory( userLogin.get,story )
    println("HE AGREGADO: " + userLogin.get.stories.size)
    Ok(views.html.allstories(userLogin.get, Story.all))
  }

  def story(id: Long)= Action {implicit request =>
    val story = Story.getById(id)
    Ok(views.html.story(story))
  }

  def allStories() = Action {implicit request =>
    Ok(views.html.allstories(Application.userLogin.get, Story.all))
  }
  def incrementPoints(id: Long) = Action { implicit request =>
    var story = Story.getById(id)
    Story.update(id,  story.points + 1)
    Ok(views.html.allstories(userLogin.get, Story.all))

  }

  def myStories(username: String) =Action {implicit request =>
    val user = User.getByName(username)
    Ok(views.html.storiesOfUser(user))
  }


}
