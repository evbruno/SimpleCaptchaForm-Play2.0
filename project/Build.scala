import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "SimpleCaptchaForm"
    val appVersion      = "1.0-SNAPSHOT"

    val appDependencies = Seq(
      "com.github.cage" % "cage" % "1.0"
    )

    val main = PlayProject(appName, appVersion, appDependencies, mainLang = SCALA).settings(
      // Add your own project settings here      
    )

}
