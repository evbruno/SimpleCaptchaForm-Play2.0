package controllers.captcha

import play.api.libs.iteratee.Enumerator
import play.api.mvc._
import play.api.Logger

trait CaptchaController extends Controller {

  val CAPTCHA_TOKEN = "captcha.token"

  private val captcha : CaptchaComponent = new CageCaptcha

  def captchaImage(implicit request: Request[AnyContent]) = {
    val token = captcha.generator.next
    val message: String = "Current captcha token: [%s] Current session (before update): [%s]".format(token, request.session)

    Logger.debug(message)

    val now = System.nanoTime.toString

    SimpleResult(
      header = ResponseHeader(OK, Map(
        CONTENT_TYPE -> ("image/" + captcha.drawer.imageFormat),
        CACHE_CONTROL -> "no-cache, no-store",
        PRAGMA -> "no-cache",
        LAST_MODIFIED -> now,
        EXPIRES -> now,
        DATE -> now
      )),
      body = Enumerator(captcha.drawer.draw(token))
    ).withSession(
      request.session + (CAPTCHA_TOKEN -> token)
    )
  }

  def isValidCaptcha(in: String)(implicit request: Request[_]) = {
    in == request.session.get(CAPTCHA_TOKEN).mkString
  }

  def consumeToken(f: => PlainResult)(implicit request: Request[_]): PlainResult = {
    Logger.debug("Consuming/Cleaning the current token from request.session: %s" format(request.session.get(CAPTCHA_TOKEN)))
    f.withSession(request.session - CAPTCHA_TOKEN)
  }

}
