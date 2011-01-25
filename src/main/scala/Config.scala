package eu.dnetlib.scalafixer

import com.google.inject._
import com.google.inject.name._
import uk.me.lings.scalaguice.ScalaModule
//import uk.me.lings.scalaguice.InjectorExtensions._

import org.objectweb.asm._

case class DefaultModule() extends AbstractModule with ScalaModule {
  def configure() {
    bind[Fixer].to[FixerImpl]
    bind[ClassVisitor => FixVisitor].toInstance {cv => new FixVisitorImpl(cv)}

    bind[Inspector].to[InspectorImpl]
  }
}
