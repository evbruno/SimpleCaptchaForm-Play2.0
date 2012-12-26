package controllers

import play.api._
import mvc._
import java.util.Date

object Application extends Controller with CaptchaInput {

  def index = form

  def captcha = Action {
    implicit request => captchaImage
  }

  def submit = Action {
    implicit request =>
      val captchaSubmitted: String = request.body.asFormUrlEncoded.get("captcha")(0)
      val captchaInSession: String = request.session.get(CAPTCHA_TOKEN).mkString

      println("captchaSubmitted>> " + captchaSubmitted)
      println("captchaInSession>> " + captchaInSession)

      if (captchaSubmitted == captchaInSession) Ok(<a href="/">Try it again !?!?!</a>).as(HTML).withSession(session - CAPTCHA_TOKEN)
      else Ok(views.html.captchaForm("error in captcha %s at %tT".format(captchaSubmitted, new Date())))
  }

  def form = Action {
    implicit request =>
      Ok(views.html.captchaForm("new form"))
  }
  
}