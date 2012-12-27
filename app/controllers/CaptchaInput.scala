package controllers

import com.github.cage.GCage
import play.api.libs.iteratee.Enumerator
import play.api.mvc._
import play.api.Logger

trait CaptchaInput extends Controller {

  val CAPTCHA_TOKEN = "captcha.token"

  private lazy val cage = new GCage

  def captchaImage(implicit request: Request[AnyContent]) = {
    val token = cage.getTokenGenerator.next()
    val message: String = "Current captcha token: [%s] Current session (before update): [%s]".format(token, request.session)

    Logger.debug(message)

    val now = System.nanoTime.toString

    SimpleResult(
      header = ResponseHeader(OK, Map(
        CONTENT_TYPE -> ("image/" + cage.getFormat),
        CACHE_CONTROL -> "no-cache, no-store",
        PRAGMA -> "no-cache",
        LAST_MODIFIED -> now,
        EXPIRES -> now,
        DATE -> now
      )),
      body = Enumerator(cage.draw(token))
    ).withSession(
      request.session + (CAPTCHA_TOKEN -> token)
    )
  }

  def isValidCaptcha(in: String)(implicit request: Request[_]) = {
    in == request.session.get(CAPTCHA_TOKEN).mkString
  }

}
