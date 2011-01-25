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

trait SignatureFixer {
  val GenDecl = "^(<[^>]*>)(.*)".r

  def wrong(s: String): Boolean
  def fix(s: String): String
}

trait FixVisitor extends ClassVisitor

class FixVisitorImpl @Inject() (val signatureFixer: SignatureFixer, v: ClassVisitor) extends ClassAdapter(v)  with FixVisitor {

  override def visitMethod(access: Int, name: String, desc: String, signature: String, exceptions: Array[String]) =  {
    val sig = if(signature != null && signatureFixer.wrong(signature)) fix(name, signature) else signature
    super.visitMethod(access, name, desc, sig, exceptions)
  }

  def fix(name: String, s: String): String = {
    println("FIXING method name: %s sig: %s".format(name, s))
    signatureFixer.fix(s)
  }
}

/**
 * Fix the generic declaration by defaulting to "extends Object"
 * Assumes that the scala compiler emits xx:Iyy:... instead of xx:Lclass;yy:...
 * */ 
class StupidSignatureFixer extends SignatureFixer {

  /** check if the generic declaration appears to be truncated */
  def oldWrong(s: String): Boolean = {
    s match {
      case GenDecl(decl, _) => 
        decl.count(_ == ':') != decl.count(_ == ';')
      case _ => false
    }    
  }


  /** check if the generic declaration appears to be truncated */
  override def wrong(s: String): Boolean = oldWrong(s) && s.contains(":I")

  override def fix(s: String): String = s.replaceAll(":I", ":Ljava/lang/Object;")

}

/** This is another implementation using regexps, it could be more useful if we find other buggy generators */
class RegexpSignatureFixer extends SignatureFixer {
  /** check if the generic declaration appears to be truncated */
  override def wrong(s: String): Boolean = {
    s match {
      case GenDecl(decl, _) => 
        decl.count(_ == ':') != decl.count(_ == ';')
      case _ => false
    }    
  }

  override def fix(s: String) = {

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

class FixerImpl @Inject() (val signatureFixer: SignatureFixer, val visitorFactory: (SignatureFixer, ClassVisitor) => FixVisitor) extends Fixer {
  def fix(clazz : InputStream) = {
    val reader = new ClassReader(clazz)
//    val flags = ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES
    val flags = 0
    val writer = new ClassWriter(reader, flags)
    
    val vis = visitorFactory(signatureFixer, writer)

    reader.accept(vis, 0)
    writer.toByteArray()
  }
}

/** Fixes multiple classes */
trait MultiFixer {
  def fix(path : String)
}

class DirFixer @Inject() (val fixer: Fixer) extends MultiFixer {
  def fix(path : String) {
    import RichFile._

    val root = new File(path)
 
    // filtering comes for free
    for(f <- root.andTree; if f.getName.endsWith(".class")) 
      fixer.fixAndSave(f.getPath(), f.getPath())
  }
}

class JarFixer @Inject() (val fixer: Fixer) extends MultiFixer {
  def fix(path : String) {
  }
}
