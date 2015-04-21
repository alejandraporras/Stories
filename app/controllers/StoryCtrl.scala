package controllers

import java.util.Date

import controllers.Application._
import models._
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

  val commentForm: Form[(String)] = Form(
      "text" -> nonEmptyText
  )

  def makeComment(idStory: Long)= Action { implicit request =>
    val textComment = commentForm.bindFromRequest.get
    val story = StoryDAO.getById(idStory)

    val idComment :Option[Long] = CommentDAO.create(Application.userLogin.get.username, idStory, textComment, new Date())
    val comment = CommentDAO.getById(idComment.get)

    story.addComment(comment)

    //println("COMMENTS OF SUB: " +   story.getComments(story).toString())

    Ok(views.html.story(story, commentForm))
  }


  def showStoryForm=Action{ implicit request =>
    Ok(views.html.newstory(storyForm))
  }

  def makeStory()= Action {implicit request =>
    val (title,text): (String, String) = storyForm.bindFromRequest.get
    val idStory: Option[Long] = StoryDAO.create(title,userLogin.get.username, text, 0 , new Date())
    val story: Story = StoryDAO.getById(idStory.get)
    userLogin.get.addStory(story)
    //UserDAO.addStory( userLogin.get,story )
    Ok(views.html.allstories(userLogin.get, StoryDAO.all))
  }

  def story(id: Long)= Action {implicit request =>
    val story = StoryDAO.getById(id)


    Ok(views.html.story(story, commentForm))
  }

  def allStories() = Action {implicit request =>
    Ok(views.html.allstories(Application.userLogin.get, StoryDAO.all))
  }
  def incrementPoints(id: Long) = Action { implicit request =>

    val story = StoryDAO.getById(id)
    if( ! StoryRatedDAO.exists(Application.userLogin.get, story)){

      StoryDAO.update(id,  story.points + 1)
      StoryRatedDAO.create(userLogin.get.username, id)
  }

    Ok(views.html.allstories(userLogin.get, StoryDAO.all))

  }

  def myStories(username: String) =Action {implicit request =>
    val user = UserDAO.getByName(username)
    Ok(views.html.storiesOfUser(user))
  }

  def comment(idComment : Long, idStory: Long) = Action { implicit request =>
    val comment: Comment = CommentDAO.getById(idComment)
    val story = StoryDAO.getById(idStory)
    Ok(views.html.comment(comment, story, commentForm))
  }

  def replyComment(idComment: Long) = Action {implicit request =>
    val textComment = commentForm.bindFromRequest.get

    val commentToReply: Comment = CommentDAO.getById(idComment)

    val idCommentToAdd: Option[Long]= CommentDAO.create(Application.userLogin.get.username, commentToReply.story, textComment, new Date())


    val commentToAdd = CommentDAO.getById(idCommentToAdd.get)

    commentToReply.addComment(commentToAdd)

    //println("LOS COMMENTS: " + CommentDAO.getById(commentToAdd.get).getComments(CommentDAO.getById(commentToAdd.get)))

    Ok(views.html.story(StoryDAO.getById(commentToReply.story), commentForm))
  }

}
