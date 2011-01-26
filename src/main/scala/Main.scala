package eu.dnetlib.scalafixer

import com.google.inject._
import uk.me.lings.scalaguice.InjectorExtensions._

object Main {
  def main(args: Array[String]) {
    val injector = Guice createInjector DefaultModule()

    if(args.length < 1) {
      println("usage: scala-fix-generics dir")
      println("       scala-fix-generics jar1 jar2")
    } else if(args.length == 1) {
      
      val dirFixer = injector.instance[DirFixer]

      println("scanning classes in dir %s".format(args(0)))
      dirFixer.fix(args(0))
    } else {
      val jarFixer = injector.instance[JarFixer]

      println("scanning classes in dir %s".format(args(0)))
      jarFixer.fix(args(0), args(1))
    }
  }
}
