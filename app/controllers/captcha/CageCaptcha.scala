package controllers.captcha

import com.github.cage.{Cage, GCage}

class CageCaptcha extends CaptchaComponent {

  def generator: TokenGenerator = new CageTokeGenerator(cage)

  def drawer: TokenDrawer = new CageTokenDrawer(cage)

  private lazy val cage = new GCage

  class CageTokeGenerator(cage: Cage) extends TokenGenerator {

    def next: String = cage.getTokenGenerator.next

  }

  class CageTokenDrawer(cage: Cage) extends TokenDrawer {

    def draw(token: String): Array[Byte] = cage draw(token)

    def imageFormat: String = cage.getFormat
  }

}
