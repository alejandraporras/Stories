package controllers

import java.util.Date

import controllers.Application._
import models.{Story, StoryRated, User, Comment}
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

  def showStoryForm=Action{ implicit request =>
    Ok(views.html.newstory(storyForm))
  }

  def makeStory()= Action {implicit request =>
    val (title,text): (String, String) = storyForm.bindFromRequest.get
    val idStory: Option[Long] = Story.create(title,userLogin.get.username, text, 0 , new Date())
    val story: Story = Story.getById(idStory.get)
    User.addStory( userLogin.get,story )
    Ok(views.html.allstories(userLogin.get, Story.all))
  }

  def story(id: Long)= Action {implicit request =>
    val story = Story.getById(id)

    val idcomment :Option[Long] = Comment.create(Application.userLogin.get.username, id, "dasdas", new Date())
    val comment = Comment.getById(idcomment.get)
    Story.addComment(Application.userLogin.get, story, comment)

    Ok(views.html.story(story))
  }

  def allStories() = Action {implicit request =>
    Ok(views.html.allstories(Application.userLogin.get, Story.all))
  }
  def incrementPoints(id: Long) = Action { implicit request =>

    val story = Story.getById(id)
    if( ! StoryRated.exists(Application.userLogin.get, story)){

      Story.update(id,  story.points + 1)
      StoryRated.create(userLogin.get.username, id)
      println(StoryRated.all().toString())
  }

    Ok(views.html.allstories(userLogin.get, Story.all))

  }

  def myStories(username: String) =Action {implicit request =>
    val user = User.getByName(username)
    Ok(views.html.storiesOfUser(user))
  }


}
