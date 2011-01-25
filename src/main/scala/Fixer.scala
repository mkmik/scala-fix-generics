package eu.dnetlib.scalafixer

import com.google.inject._

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

trait FixVisitor extends ClassVisitor {
  val GenDecl = "^(<[^>]*>)(.*)".r

  override def visitMethod(access: Int, name: String, desc: String, signature: String, exceptions: Array[String]) =  {
    val sig = if(signature != null && wrong(signature)) fix(name, signature) else signature
    super.visitMethod(access, name, desc, sig, exceptions)
  }

  def wrong(s: String): Boolean
  def fix(name: String, s: String): String
}

/**
 * Fix the generic declaration by defaulting to "extends Object"
 * Assumes that the scala compiler emits xx:Iyy:... instead of xx:Lclass;yy:...
 * */ 
class FixVisitorImpl @Inject() (v: ClassVisitor) extends ClassAdapter(v) with FixVisitor {

  /** check if the generic declaration appears to be truncated */
  def wrong(s: String): Boolean = {
    s match {
      case GenDecl(decl, _) => 
        decl.count(_ == ':') != decl.count(_ == ';')
      case _ => false
    }    
  }

  def fix(name: String, s: String): String = {
    println("FIXING method name: %s sig: %s".format(name, s))
    fix(s)
  }

  def fix(s: String): String = s.replaceAll(":I", ":Ljava/lang/Object;")

}

/** This is another implementation using regexps, it could be more useful if we find other buggy generators */
class FixVisitorRegexpImpl @Inject() (v: ClassVisitor) extends ClassAdapter(v) with FixVisitor {
  def fixWithRegexp(s: String) = {

    def enhance(m: String) = m match {
      case x if x.contains(":") => x; 
      case x => x+":Ljava/lang/Object"
    }

    s match {
      case GenDecl(decl, rest) => 
        decl.split(":I").map(enhance).mkString(";") + rest
    }
  }

}

class FixerImpl @Inject() (val visitorFactory: ClassVisitor => FixVisitor) extends Fixer {
  def fix(clazz : InputStream) = {
    val reader = new ClassReader(clazz)
//    val flags = ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES
    val flags = 0
    val writer = new ClassWriter(reader, flags)
    
    val vis = new FixVisitorImpl(writer)

    reader.accept(vis, 0)
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
