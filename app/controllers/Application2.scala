package controllers

import captcha.CaptchaController
import play.api._
import data.Form
import mvc._
import play.api.data.Forms._

object Application2 extends Controller with CaptchaController {

  def myForm(implicit request: Request[_]): Form[(String, String, String)] = Form(
    tuple(
      "firstName" -> nonEmptyText,
      "lastName" -> text,
      "captcha" -> nonEmptyText.verifying("Invalid CAPTCHA dude !", captcha => isValidCaptcha(captcha)
      )
    )
  )

  def submit = Action {
    implicit request =>

      consumeToken {
        myForm.bindFromRequest().fold(
          errors => BadRequest(views.html.captchaForm2(errors)),
          ok => Redirect(routes.Application2.form())
            .flashing("successMsg" -> "Captcha verified !!!")
        )
      }
  }

  def form = Action {
    implicit request =>
      Ok(views.html.captchaForm2(myForm))
  }

}