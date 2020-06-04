// <- Tools ************************************************

import scala.reflect.runtime.universe._
def prettyType[T: TypeTag](
  v: T,
  level: Int = 1
) = typeOf[T].baseClasses.map(typeOf[T].baseType).take(level).mkString(", ")

// ->

import zio._
import zio.Schedule._
import zio.duration._

// <- Cool Tricks ******************************************

// <- Fallback *********************************************

// reminder
// type UIO[+A]  = ZIO[Any, Nothing, A]
// type Task[+A] = ZIO[Any, Throwable, A]

type Data = String

def getFromCache(value: String): Task[Data] = ???
def getFromDatabase(value: String): Task[Data] = ???
def getFromConfig(value: String): UIO[Data] = ???

def getFromAll(value: String): UIO[Data] =
  getFromCache(value) orElse getFromDatabase(value) orElse getFromConfig(value)

// ->

// <- Forever **********************************************

// *> (or zipRight) is flatMap but ignore input value

def forever[R, E](zio: ZIO[R, E, Any]): ZIO[R, E, Nothing] =
  zio *> forever(zio)

// ->

// <- Eventually *******************************************

def eventually_[R, A](zio: ZIO[R, Any, A]): ZIO[R, Nothing, A] =
  zio orElse eventually_(zio)

// ->

// <- Retried **********************************************

type Response = String

def httpGet(url: String): Task[Response] = ???

def retryPolicy: Schedule[ZEnv, Any, Any] =
  (Schedule.exponential(10.millis) || spaced(10.seconds)) &&
    recurs(100).jittered(min = 0, max = 1)

def httpGetWithRetry(url: String): RIO[ZEnv, Response] =
  httpGet(url) retry retryPolicy

// ->

// <- Load tester ******************************************

def loadTester(
  url: String,
  n: Int
): Task[List[Throwable]] =
  ZIO
    .foreachPar(0 to n) { _ =>
      httpGet(url).either
        .map(_.left.toOption)
    }
    .map {
      _.collect { case Some(e) => e }
    }

// ->

// <- flip *************************************************

def mapError[R, E, E1, A](zio: ZIO[R, E, A])(f: E => E1): ZIO[R, E1, A] =
  zio.flip.map(f).flip

def eventually[R, E, A](zio: ZIO[R, E, A]): ZIO[R, Nothing, A] =
  zio.flip.forever.flip

def fallbackPlan[A](
  plan1: Task[A],
  err0: Throwable,
  fallback1: Task[A],
  fallback2: Task[A]
): Task[A] =
  (for {
    err  <- plan1.flip
    err2 <- (if (err == err0) fallback1 else fallback2).flip
  } yield err2).flip

// ->

// ->
