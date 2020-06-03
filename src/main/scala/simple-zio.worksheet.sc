// <- Tools ************************************************

import scala.reflect.runtime.universe._
def prettyType[T: TypeTag](
  v: T,
  level: Int = 1
) = typeOf[T].baseClasses.map(typeOf[T].baseType).take(level).mkString(", ")

// ->

// <- ZIO: case class **************************************

case class ZIO[-R, +E, +A](run: R => Either[E, A]) { self =>

  // <- map ************************************************

  def map[B](f: A => B): ZIO[R, E, B] =
    ZIO(r => run(r).map(f))

  // ->

  // <- flatMap ********************************************

  def flatMap[R1 <: R, E1 >: E, B](f: A => ZIO[R1, E1, B]): ZIO[R1, E1, B] =
    ZIO(r => run(r).flatMap(a => f(a).run(r)))

  // ->

  // <- provide ********************************************

  def provide(r: R): ZIO[Any, E, A] =
    ZIO(_ => run(r))

  // ->

  // <- either *********************************************

  def either: ZIO[R, Nothing, Either[E, A]] =
    ZIO(r => Right(run(r)))

  // ->

  // <- zip ************************************************

  def zip[R1 <: R, E1 >: E, B](that: => ZIO[R1, E1, B]): ZIO[R1, E1, (A, B)] =
    self.flatMap(a => that.map(b => a -> b))

  // ->

}

// ->

// <- ZIO: object ******************************************

object ZIO {

  // <- succeed ********************************************

  def succeed[A](a: A): ZIO[Any, Nothing, A] =
    ZIO(_ => Right(a))

  // ->

  // <- fail ***********************************************

  def fail[E](e: E): ZIO[Any, E, Nothing] =
    ZIO(_ => Left(e))

  // ->

  // <- environment ****************************************

  def environment[R]: ZIO[R, Nothing, R] =
    ZIO(r => Right(r))

  // ->

  // <- effect *********************************************

  def effect[A](action: => A): ZIO[Any, Throwable, A] =
    ZIO { _ =>
      try Right(action)
      catch {
        case t: Throwable => Left(t)
      }
    }

  // ->

  // <- unsafeRun ******************************************

  def unsafeRun[R, E, A](
    zio: ZIO[R, E, A],
    r: R
  ): A =
    zio.run(r) match {
      case Left(err)     => throw new RuntimeException(s"failed: $err")
      case Right(result) => result
    }

  // ->
}

// ->

// <- ZIO: samples *****************************************

val hello = ZIO.succeed("hello")

prettyType(hello)

trait Module1 { def x1: String }
trait Module2 { def x2: Int    }

sealed trait Errors extends Product with Serializable
case object TooLarge         extends Errors
case object StringNotAllowed extends Errors

val forbiddenString = "___"
val numberThreshold = 100

val res = for {
  string <- ZIO.environment[Module1].map(_.x1)
  number <- ZIO.environment[Module2].map(_.x2)
  _ <- if (string.contains(forbiddenString)) ZIO.fail(StringNotAllowed)
      else ZIO.succeed(())
  _ <- if (number > numberThreshold) ZIO.fail(TooLarge)
      else ZIO.succeed(())
} yield s"$string-$number"

prettyType(res)

val modules = new Module1 with Module2 { val x1 = "foo"; val x2 = 42 }

ZIO.unsafeRun(res, modules)

// ->
