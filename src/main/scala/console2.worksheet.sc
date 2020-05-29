// <- Tools ************************************************

import scala.reflect.runtime.universe._
def prettyType[T: TypeTag](
  v: T,
  level: Int = 1
) = typeOf[T].baseClasses.map(typeOf[T].baseType).take(level).mkString(", ")

// ->

// <- Console **********************************************

sealed trait Console[A]

case class Return[A](value: A) extends Console[A]

case class Print[A](
  line: String,
  console: Console[A])
    extends Console[A]

case class Read[A](read: String => Console[A]) extends Console[A]

// ->

// <- Program **********************************************

val program: Console[String] =
  Print(
    "What is your name?",
    Read(name =>
      Print(
        s"Hello, $name!",
        Return(name)
      )
    )
  )

prettyType(program)

// ->

// <- Interpreter ******************************************
import scala.util.Random

def run[A](console: Console[A]): A =
  console match {
    case Return(value) => value
    case Print(line, console) => print(line); run(console)
    // we can't use StdIn in a worksheet
    //case Read(read) => run(read(StdIn.readLine))
    // returning some random characters
    case Read(read) => run(read(Random.alphanumeric.take(5).mkString))
  }

run(program)

// ->
