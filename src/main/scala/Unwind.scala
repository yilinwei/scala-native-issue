package scala.scalanative.runtime.posix

import scala.scalanative.unsafe._

// https://itanium-cxx-abi.github.io/cxx-abi/abi-eh.html

@extern
object Unwind {

  type ReasonCode = CInt

  object ReasonCode {
    def noReason: ReasonCode = 0
  }

  type Context = CStruct0
  type TraceFn = CFuncPtr2[Ptr[Context], CVoidPtr, ReasonCode]

  @name("_Unwind_Backtrace")
  def Backtrace(traceFn: TraceFn, traceArgument: CVoidPtr): ReasonCode = extern

  @name("_Unwind_GetIP")
  def GetIP(context: Ptr[Context]): CVoidPtr = extern
}
