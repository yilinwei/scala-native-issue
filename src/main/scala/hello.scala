import scala.scalanative.unsafe._

object Foo {
  def someMethod(): Unit = {
    try {
      throw new IllegalArgumentException("foo")
    } catch {
      case e: IllegalArgumentException =>
        e.printStackTrace()
    }
  }
}

object Lib {
  @exported("lib_bar")
  def bar(): Unit = Foo.someMethod()
}
