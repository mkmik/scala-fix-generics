import eu.dnetlib.scalafixer._

import com.google.inject._
import uk.me.lings.scalaguice.InjectorExtensions._

val injector = Guice createInjector DefaultModule()

val fixer = injector.instance[Fixer]
val inspector = injector.instance[Inspector]

println(fixer)

val cname = "List.class"
//val cname = "Ta.class"

//fixer.fixAndSave("/tmp/" + cname, "/tmp/out/" + cname)
//inspector.inspect("/tmp/out/" + cname)


val dirFixer = injector.instance[DirFixer]
dirFixer.fix("/tmp/scala-lib")
