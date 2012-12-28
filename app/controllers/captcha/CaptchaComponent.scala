package controllers.captcha

trait CaptchaComponent {

  def generator: TokenGenerator

  def drawer: TokenDrawer

  trait TokenGenerator {
    def next : String
  }

  trait TokenDrawer {
    def draw(token: String) : Array[Byte]
    def imageFormat : String
  }

}

object CaptchaComponent {

  val CAPTCHA_KEY_CONFIG = "captcha.impl"

}
