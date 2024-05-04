package scala.scalanative
package runtime

import scala.scalanative.unsafe._
import posix.Unwind
import scala.scalanative.runtime.Intrinsics.{castLongToRawPtr, castObjectToRawPtr, castRawPtrToObject}
import scala.collection.mutable

@link("dl")
@extern
object dl {
  type Info = CStruct4[CString, CVoidPtr, CString, CVoidPtr]

  @name("dladdr")
  def addr(addr: CVoidPtr, info: Ptr[Info]): CInt = extern

}

object StackTrace {

  @exported("StackTrace_PrintStackTrace")
  def dummy(): Unit = {}

  @exported("scala_native_backtrace_trace_fn")
  def traceFn(context: Ptr[Unwind.Context], traceArgument: CVoidPtr): Unwind.ReasonCode = {
    val ip = Unwind.GetIP(context)
    if (ip != null) {
      val info = stackalloc[dl.Info]()
      dl.addr(ip, info)
      val sname = info._3
      val fname = info._1
      if (sname != null) {
        try {
          val buf = castRawPtrToObject(traceArgument.rawptr).asInstanceOf[mutable.ArrayBuffer[StackTraceElement]]
          val element = StackTraceElement(sname, fname, Backtrace.Position.empty)
          buf.append(element)
        } finally { }
      }
    }
    Unwind.ReasonCode.noReason
  }

  @noinline def currentStackTrace(): scala.Array[StackTraceElement] = {
    val buf = new mutable.ArrayBuffer[StackTraceElement]()
    Unwind.Backtrace(traceFn _, fromRawPtr(castObjectToRawPtr(buf)));
    buf.toArray
  }

}

import scala.scalanative.unsafe._
import scala.scalanative.unsigned._
import scala.scalanative.meta.LinktimeInfo
import scala.scalanative.runtime.ffi.{malloc, calloc, free}

// This part is copied from Scala Native directly, licensed via Apache
private object StackTraceElement {
  // ScalaNative specific
  def apply(
    sym: CString,
    unwindFileName: CString,
    position: Backtrace.Position
  ): StackTraceElement = {
    val className: Ptr[CChar] = fromRawPtr(
      Intrinsics.stackalloc[CChar](Intrinsics.castIntToRawSizeUnsigned(512))
    )
    val methodName: Ptr[CChar] = fromRawPtr(
      Intrinsics.stackalloc[CChar](Intrinsics.castIntToRawSizeUnsigned(256))
    )
    val fileName: Ptr[CChar] =
      if (LinktimeInfo.isWindows)
        fromRawPtr(
          Intrinsics.stackalloc[CChar](Intrinsics.castIntToRawSizeUnsigned(512))
        )
      else null
    val lineOut: Ptr[Int] = fromRawPtr(Intrinsics.stackalloc[Int]())
    SymbolFormatter.asyncSafeFromSymbol(
      sym = sym,
      classNameOut = className,
      methodNameOut = methodName,
      fileNameOut = fileName,
      lineOut = lineOut
    )
    val filename =
      if (position.filename != null) position.filename
      else {
        val str = if (fileName == null) unwindFileName else fileName
        fromCString(str).trim()
      }
    val line =
      if (position.line > 0 || filename == null) position.line
      else !lineOut

    new StackTraceElement(
      fromCString(className),
      fromCString(methodName),
      filename,
      line
    )
  }
}
