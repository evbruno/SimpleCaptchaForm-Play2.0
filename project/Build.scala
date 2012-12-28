import sbt._
import PlayProject._
import Keys._

object ApplicationBuild extends Build {

  val appName = "SimpleCaptchaForm"
  val appVersion = "1.1-SNAPSHOT"

  val appDependencies = Seq(
    "com.github.cage" % "cage" % "1.0",
    "com.github.axet" % "kaptcha" % "0.0.6"
  )

  val main = PlayProject(appName, appVersion, appDependencies, mainLang = SCALA).settings(
    // Add your own project settings here
  )

}
