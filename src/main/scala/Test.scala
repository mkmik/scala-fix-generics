package eu.dnetlib.scalafixer

import org.objectweb.asm._
import java.io._

object Test {
  def test = println("wowow")

  def main = {
    
  }

  def ugo = new ClassReader(new FileInputStream("/tmp/List.class"))
}
