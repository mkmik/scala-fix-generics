Background


Eclipse is very picky about class signatures. Scala 2.8.1 generates bogus signatures in some cases.

I want to be able to use libraries written in scala from within eclipse, but eclipse code completion breaks 
if it encounters such a bad signature:

https://bugs.eclipse.org/bugs/show_bug.cgi?id=332423

See the scala issue:

https://lampsvn.epfl.ch/trac/scala/ticket/4067


All that will be fixed soon in the respective projects: scala will not emit those signatures and eclipse will not crash the 
code completion gui if it encounters one. 

Unfortunately 