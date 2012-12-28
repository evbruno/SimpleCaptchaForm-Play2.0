package controllers.captcha

import javax.imageio.ImageIO
import java.util.Properties
import com.google.code.kaptcha.util.Config
import com.google.code.kaptcha.Producer
import java.io.ByteArrayOutputStream

class Kaptcha extends CaptchaComponent {

  def generator: TokenGenerator = new KaptchaTokenGenerator(engine)

  def drawer: TokenDrawer = new KaptchaTokenDrawer(engine)

  private lazy val engine: Producer = {
    ImageIO.setUseCache(false)
    val props = new Properties
    props.put("kaptcha.border", "no")
    props.put("kaptcha.textproducer.font.color", "black")
    props.put("kaptcha.textproducer.char.space", "5")

    val config = new Config(props)
    config.getProducerImpl
  }

  class KaptchaTokenGenerator(engine: Producer) extends TokenGenerator {

    def next: String = engine.createText()

  }

  class KaptchaTokenDrawer(engine: Producer) extends TokenDrawer {

    def draw(token: String): Array[Byte] = {
      val stream = new ByteArrayOutputStream

      ImageIO.write(engine.createImage(token), "jpg", stream)

      stream.toByteArray
    }

    def imageFormat: String = "jpg"
  }

}
