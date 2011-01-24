package eu.dnetlib.scalafixer

import org.objectweb.asm._
import org.objectweb.asm.util._
import org.objectweb.asm.commons._
import java.io._
import org.apache.commons.io.IOUtils

/** Fixes a single class */
trait Fixer {
  def fix(clazz : InputStream) : Array[Byte]

  def fix(fileName : String) : Array[Byte] = fix(new FileInputStream(fileName))

  def fixAndSave(fileName : String, output : String): Unit = {
    val fixed = fix(fileName)
    IOUtils.copy(new ByteArrayInputStream(fixed), new FileOutputStream(output));
  }

}

class FixerImpl extends Fixer {
  def fix(clazz : InputStream) = {
    val reader = new ClassReader(clazz)
    val writer = new ClassWriter(reader, ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES)
    // val vis = new CheckClassAdapter(writer)
//    val vis = new EmptyVisitor()
//    reader.accept(vis, 0)
    reader.accept(writer, 0)
    writer.toByteArray()
  }
}

/** Fixes multiple classes */
trait MultiFixer {
  def fix(path : String)
}

class DirFixer extends MultiFixer {
  def fix(path : String) {
  }
}

class JarFixer extends MultiFixer {
  def fix(path : String) {
  }
}
