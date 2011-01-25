Background
==========

Eclipse is very picky about class signatures. Scala 2.8.1 generates bogus signatures in some cases.

I want to be able to use libraries written in scala from within eclipse, but eclipse code completion breaks 
if it encounters such a bad signature:

https://bugs.eclipse.org/bugs/show_bug.cgi?id=332423

See the scala issue:

https://lampsvn.epfl.ch/trac/scala/ticket/4067


All that will be fixed soon in the respective projects: scala will not emit those signatures and eclipse will not crash the 
code completion gui if it encounters one. 

Unfortunately I cannot wait for these fixes to be released, and I'm not feeling well to use head version of scalac and eclipse :-)

Hack
====

This tool processes the class files generated by the scala compiler (like those in scala-library.jar) and finds all generic 
signature which are bogus.

It tries to fix some common mistakes or drops them if not found.

Usage
=====

   mkdir tmp
   cd tmp
   unzip ...../scala-library.jar
   ../scala-fix-generics .

Build
=====

   sbt update
   sbt assembly
   
