// <- Tools ************************************************

import scala.reflect.runtime.universe._
def prettyType[T: TypeTag](
  v: T,
  level: Int = 1
) = typeOf[T].baseClasses.map(typeOf[T].baseType).take(level).mkString(", ")

import zio._
def execute[R >: ZEnv, E, A](zio: ZIO[R, E, A]) =
  Runtime.default.unsafeRun(zio)

// ->

val hello      = ZIO.succeed("world")    //
val printHello = ZIO(println("world !")) //

prettyType(hello)
prettyType(printHello)

execute(hello)
execute(printHello)
