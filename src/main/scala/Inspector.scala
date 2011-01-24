package eu.dnetlib.scalafixer

import org.objectweb.asm._
import org.objectweb.asm.util._
import java.io._


trait Inspector {
  def inspect(fileName : String) : String = inspect(new FileInputStream(fileName))

  def inspect(clazz : InputStream) : String = {
    val reader = new ClassReader(clazz)
    val writer = new StringWriter()
    val cv = new TraceClassVisitor(new PrintWriter(writer));
    reader.accept(cv, 0)
    writer.toString
  }
}

class InspectorImpl extends Inspector {
}

