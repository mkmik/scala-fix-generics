import eu.dnetlib.scalafixer._

import com.google.inject._
import uk.me.lings.scalaguice.InjectorExtensions._

val injector = Guice createInjector DefaultModule()

val fixer = injector.instance[Fixer]
val inspector = injector.instance[Inspector]

println(fixer)

fixer.fixAndSave("/tmp/Ta.class", "/tmp/out/Ta.class")
//inspector.inspect("/tmp/List.class")
inspector.inspect("/tmp/out/Ta.class")
