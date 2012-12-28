package controllers.captcha

import com.github.cage.{Cage, GCage}

class CageCaptcha extends CaptchaComponent {

  def generator: TokenGenerator = new CageTokenGenerator(engine)

  def drawer: TokenDrawer = new CageTokenDrawer(engine)

  private lazy val engine = new GCage

  class CageTokenGenerator(engine: Cage) extends TokenGenerator {

    def next: String = engine.getTokenGenerator.next

  }

  class CageTokenDrawer(cage: Cage) extends TokenDrawer {

    def draw(token: String): Array[Byte] = cage draw(token)

    def imageFormat: String = cage.getFormat
  }

}
