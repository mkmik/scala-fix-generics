package eu.dnetlib.scalafixer

import com.google.inject._
import uk.me.lings.scalaguice.InjectorExtensions._

object Main {
  def main(args: Array[String]) {
    if(args.length < 1) {
      println("usage: scala-fix-generics dir")
    } else {

      val injector = Guice createInjector DefaultModule()
      
      val dirFixer = injector.instance[DirFixer]
      dirFixer.fix(args(1))
    }
  }
}
