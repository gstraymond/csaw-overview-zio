%title: Scala ZIO, the future of Future[A]

-> ## Agenda <-

• Functional effects

• Scala Type Parametrization

• ZIO toy implementation

• ZIO async

• Cool tricks with ZIO

---

-> ## Future[A] <-

• Part of Scala std lib

• Not referentially transparent

• Error Management by throwing exception

• Use of implicits for `ExecutionContext`

---

-> ## Functional effects <-

• Modeling side-effects with pure/immutable values

• FE describe a program instead of running it

• Problems with side-effects
  type reasoning, composition, testing
  
• program description will be executed
  by an interpreter

---

-> ## Building a simple console • print <-

*Console*

• `Print(line: String)`

• `Sequence(first:Console, second: Console)`

• Interpreter

---

-> ## Building a simple console • interactive <-

*Console[A]*

• `Return(value: A)`

• `Print(line: String, console: Console[A])

• `Read(f: String => Console[A])`

• Interpreter

---

-> ## Scala Type Hierarchy <-

• `Any` - supertype of all types
  • `equals`
  • `toString`

• `Nothing` - subtype of all types
  • means non-termination (thrown exception,
  infinite loop…)

---

-> ## Type Parametrization: variance / bound <-

*Covariance*

• `List[+A]` - A is covariant
  can accept T and all sub-types

*Lower Type Bound*

• `B >: A` - A subtype of B

`+A` will make type *more* generic: `A1 || A2 || A3`...

---

-> ## Type Parametrization: variance / bound <-

*Contravariance*

• `Module[-A]` - A is contravariant
  can accept T and all super-types

*Upper Type Bound*

• `B <: A` - B subtype of A

`-A` will make type *less* generic: `A1 && A2 && A3`...

--- 

-> ## ZIO[-R, +E, +A] <-

• `R` environment

• `E` error

• `A` result

---

-> ## Building a simple ZIO <-

• constructors

• operators

• sample program

---

-> ## ZIO effects

Can represent
• success and failure
• synchronous
• asynchronous (callback) 
• concurrent
• resource (acquire/release)
• parallel
• dependency
• cancellation

---

-> ## ZIO async: fibers <-

• on top of JVM threads (mapped on OS threads)

• ~ green threads

• each fiber has its own stack and interpreter
  to be able to run effects
  
• able to run a lot of fibers on a single thread

• improve scalability

---

-> ## Cool tricks <-

*Fallback*

• composing effects

• `getFromAll` cannot fail
  since `getFromConfig` cannot fail

---

-> ## Cool tricks <-

*Forever*

• is an effects combinator
  take effects and return effects
  
• can fail but can't succeed (doesn't terminate)

---

-> ## Cool tricks <-

*Eventually*

• is an effects combinator
  take effects and return effects
  
• can succeed (or run forever) but can't fail

---

-> ## Cool tricks <-

*Retried*

• exponential retry starting at 10 ms
  then cap at 10 seconds 
  only 100 times
  and jittered
  
• `||` will pick the minimum value

• `&&` applies if both schedules continues

---

-> ## Cool tricks <-

*Load testing*

• foreachPar executes in parallel
  all item in a List
  
• httpGet could be enriched
  with retry policy, timeouts...

---

-> ## Cool tricks <-

*flip*

• `ZIO[R, E, A]` → `ZIO[R, A, E]`

• `mapError` as the dual as `map`

• `eventually` as the dual as `forever`

• `fallbackPlan`: executing specify fallback
   depending on error

