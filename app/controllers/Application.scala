package controllers

import play.api._
import mvc._
import java.util.Date

object Application extends Controller with CaptchaInput {

  def index = form

  def captcha = Action {
    implicit request => super.captchaImage
  }

  def submit: Action[AnyContent] = Action {
    implicit request =>
      val captchaSubmitted: String = request.body.asFormUrlEncoded.get("captcha")(0)
      val captchaInSession: String = request.session.get(CAPTCHA_TOKEN).mkString

      Logger.debug("Captcha submitted [%s]" format (captchaSubmitted))
      Logger.debug("Captcha inSession [%s]" format (captchaInSession))

      consumeToken {
        if (captchaSubmitted == captchaInSession)
          Ok(<a href="/">Captcha verified. Try it again !?!?!</a>).as(HTML).withCookies()
        else
          Redirect(routes.Application.form()).flashing("errorMsg" -> "The capatcha '%s' sent at %tT was invalid".format(captchaSubmitted, new Date()))
      }
  }

  def form = Action {
    implicit request =>
      Ok(views.html.captchaForm())
  }

}