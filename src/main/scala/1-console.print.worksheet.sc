// <- Tools ************************************************

import scala.reflect.runtime.universe._
def prettyType[T: TypeTag](
  v: T,
  level: Int = 1
) = typeOf[T].baseClasses.map(typeOf[T].baseType).take(level).mkString(", ")

// ->

// <- Console **********************************************

sealed trait Console {

  // <- Compose ********************************************

  def +(that: Console): Console = Sequence(this, that)

  // ->
}

case class Print(line: String) extends Console

case class Sequence(
  first: Console,
  second: Console)
    extends Console

// ->

// <- Console: Program *************************************

def program = Print("Hello") + Print(" ... ") + Print("World!")

prettyType(program)

// ->

// <- Console: interpreter *********************************

def run(console: Console): Unit = console match {
  case Print(line)             => print(line)
  case Sequence(first, second) => run(first); run(second)
}

run(program)

// ->
