# Scala ZIO: The future of Future[A]

## Agenda

- functional effects
- ZIO API
- cool tricks

## What is ZIO / origins

- respects FP fundamentals
  - ref transp
  - immutability
  - total functions / deterministic / no side-effects
- to build high-performance, type-safe, concurrent, asynchronous applications
  - doesn't leak resources

## Functional effects

- Modeling side-effects with pure values
- Side-effets is something that interact with the outside world
- Problems with side-effects
  - difficult to reason about a program only with types
  - don't compose nicely
  - hard to test
- will describe a program (immutable values) instead of running it
- at the end of the program, this program description will be executed 

## Building a simple console - read-only

- Console
  - Print(line: String)
  - Sequence(first:Console, second: Console)
- Interpreter

## Building a simple console - interactive

- Console[A]: Is now polymorphic and A represents the success value (return value)
  - Return(value: A): only returns the A
  - Print(line: String, console: Console[A]): print a line and do someting else
  - Read(f: String => Console[A]): a function that accepts user input and produce a console
- Interpreter

## Scala Type Parameterization

+T is covariant: can accept T and all sub-types
  if T a subtype of U, then M[T] is a subtype of M[U]
-T is contravariant: can accept T and all super-types
  if T a subtype of U, then M[U] is a subtype of M[T]

  as a generalization, +T will make type more generic
  and -T more specific

U <: T upper type bound: U subtype of T
U >: T lower type bound: U supertype of T

## ZIO[-R, +E, +A]

Functionnal effects is a immutable data structure that describe side-effects, which are run at the edge of the program

Better future which does async / concurrent

Describe side-effect

- R: environment, could depend on a db connection, a webservice, the clock, conf
  - describe the context in which the effect needs to be run
- E: error
  - in Future, it's throwable and can't be changed
- A: result
  - the type, the effect may succeed

## Building a simple ZIO

- constructors
- operators
- example program

## ZIO

Can represents effects

- success and failure
- synchronous
- asynchronous (callback) 
- concurrent
- resource
- parallel
- dependency
- cancellation

Enable to build robust/performant application which are typesafe

## ZIO: Task[A]

## ZIO: IO[E, A]

immutable value that describes an effectful program

IO[Nothing, A] is a program that never fails

unsafeRun: IO[E, A]: A (runtime system) interprets program language

which enable

- asynchronicity
- concurrency
- resource safety
- fibers (instead of threads)
- interruption
- supervision

## Program Combinators

retry / schedule

- take an ZIO and return an ZIO

## ZIO: async + fibers

on top of JVM threads (mapped on OS threads)

sort of green threads

each fiber has its own stack and interpreter, to be able to run effects

able to run a lot of fibers on a single thread

improve scalability

## ZIO concurrency/parallelism

race 2 effects

attempt/timeout

par

## ZIO: lazyness

interruption of a fiber

interrupt/join by the runtime system

parallelism: interrupt others if one fails

## ZIO: stream

## ZIO: Bracket/Managed

do better than try/finally whch is synchronous

## ZIO: testing

## ZIO: others - Ref, Semaphore, Scheduler, Queue...

Ref → transactions - compare and swap

RefM (use effect when updating)
ensure consistency even when ran in parallel

Promise - do fiber communication 

Schedule[A, B] immutable value that describe an effectul schedule which after consuming an A, produces a B and decides to halt or continue some delay d

- retryable OR repeatable

compose schedules

Back-pressured queue (slow down producder)