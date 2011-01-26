package eu.dnetlib.scalafixer

import com.google.inject._
import com.google.inject.name._
import uk.me.lings.scalaguice.ScalaModule
//import uk.me.lings.scalaguice.InjectorExtensions._

import org.objectweb.asm._

case class DefaultModule() extends AbstractModule with ScalaModule {
  def configure() {
    bind[Fixer].to[FixerImpl]

//    bind[List[SignatureFixer]].toInstance(List(new StupidSignatureFixer("I"), new StupidSignatureFixer("J"), new  NullSignatureFixer))

    bind[List[SignatureFixer]].toInstance(List(new PrimitiveSignatureFixer, new  NullSignatureFixer))
    bind[(List[SignatureFixer], ClassVisitor) => FixVisitor].toInstance {(sf, cv) => new FixVisitorImpl(sf, cv)}

    bind[Inspector].to[InspectorImpl]
  }
}
