// <- Tools ************************************************

import scala.reflect.runtime.universe._
def prettyType[T: TypeTag](
  v: T,
  level: Int = 1
) = typeOf[T].baseClasses.map(typeOf[T].baseType).take(level).mkString(", ")

// ->

import zio._

// <- Async ************************************************

// <- Fibers ***********************************************

// fibonacci
def fib(n: Int): UIO[Int] =
  if (n <= 1) {
    IO.succeed(1)
  } else {
    for {
      fiber1 <- fib(n - 2).fork
      fiber2 <- fib(n - 1).fork
      v2     <- fiber2.join
      v1     <- fiber1.join
    } yield v1 + v2
  }

// ->

// <- Racing ***********************************************

// return the first one who complete and interrupt the other

fib(100) race fib(200)

// ->

// <- Parallelism ******************************************

type Matrix = Unit

def computeInverse(m: Matrix): Task[Matrix] = ???

def matrices: List[Matrix] = ???

def bigCompute(): Task[List[Matrix]] =
  ZIO.foreachPar(matrices)(m => computeInverse(m))

// ->

// ->
