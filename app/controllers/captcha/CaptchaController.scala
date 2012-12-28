package controllers.captcha

import play.api.libs.iteratee.Enumerator
import play.api.mvc._
import play.api.Logger
import play.Play

trait CaptchaController extends Controller {

  val CAPTCHA_TOKEN = "captcha.token"

  private lazy val captcha = initFacade

  def captchaImage(implicit request: Request[AnyContent]) = {
    val token = captcha.nextToken
    val message: String = "Current captcha token: [%s] Current session (before update): [%s]".format(token, request.session)

    Logger.debug(message)

    val now = System.nanoTime.toString

    SimpleResult(
      header = ResponseHeader(OK, Map(
        CONTENT_TYPE -> ("image/" + captcha.imageFormat),
        CACHE_CONTROL -> "no-cache, no-store",
        PRAGMA -> "no-cache",
        LAST_MODIFIED -> now,
        EXPIRES -> now,
        DATE -> now
      )),
      body = Enumerator(captcha draw (token))
    ).withSession(
      request.session + (CAPTCHA_TOKEN -> token)
    )
  }

  def isValidCaptcha(in: String)(implicit request: Request[_]) = {
    in == request.session.get(CAPTCHA_TOKEN).mkString
  }

  def consumeToken(f: => PlainResult)(implicit request: Request[_]): PlainResult = {
    Logger.debug("Consuming/Cleaning the current token from request.session: %s" format (request.session.get(CAPTCHA_TOKEN)))
    f.withSession(request.session - CAPTCHA_TOKEN)
  }

  private def initFacade = {
    Play.application().configuration().getString(CaptchaComponent.CAPTCHA_KEY_CONFIG) match {
      case "CAGE" => new CaptchaFacade with CageCaptcha
      case _ => new CaptchaFacade with Kaptcha
    }
  }

}

private trait CaptchaFacade {

  this: CaptchaComponent =>

  def imageFormat = this.drawer.imageFormat

  def draw = this.drawer.draw(_)

  def nextToken = this.generator.next

}