ThisBuild / scalaVersion := "2.13.13"

name := "foo"

enablePlugins(ScalaNativePlugin)

// set to Debug for compilation details (Info is default)
logLevel := Level.Info

// import to add Scala Native options
import scala.scalanative.build._

// defaults set with common options shown
nativeConfig ~= { c =>
  c.withLTO(LTO.none) // thin
    .withBuildTarget(BuildTarget.libraryStatic)
    .withMode(Mode.debug) // releaseFast
    .withGC(GC.immix) // commix
}
