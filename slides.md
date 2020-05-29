%title: Scala ZIO, the future of Future[A]

-> ## Agenda <-

• Functional effects
• Scala Type Parameterization
• ZIO API
• Fibers, Streams, Brackets...

---

-> ## Future[A] <-

• Not referentially transparent
• Limited

TO BE COMPLETED
=================

---

-> ## Functional effects <-

• Modeling side-effects with pure/immutable values
• FE describe a program instead of running it
• Problems with side-effects: type reasoning, composition, testing
• program description will be executed by an interpreter

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

Scala Type Parameterization

• Covariance: `List[+A]`
• Contravariance: `Queue[-A]`

TO BE COMPLETED
=================

--- 

-> ZIO[-R, +E, +A] <-

• R: environment
• E: error
• A: result

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

on top of JVM threads (mapped on OS threads)

sort of green threads

each fiber has its own stack and interpreter, to be able to run effects

able to run a lot of fibers on a single thread

improve scalability

---

-> ## Magic tricks <-

*Fallback*

• composing effects
• `getFromAll` cannot fail since `getFromConfig` cannot fail

---

-> ## Magic tricks <-

*Forever*

• is an effects combinator: take effects and return effects
• can fail but can't succeed (doesn't terminate)

---

-> ## Magic tricks <-

*Eventually*

• is an effects combinator: take effects and return effects
• can succeed (or run forever) but can't fail

---

-> ## Magic tricks <-

*Retried*

• exponential retry starting at 10 ms then cap at 10 seconds only 100 times and jittered
• || will pick the minimum value
• && applies if both schedules continues

---

-> ## Magic tricks <-

*Load testing*

• foreachPar executes in parallel all item in a List
• httpGet could be enriched with retry policy, timeouts...

---
-> ## Magic tricks <-

*flip*

• flip : ZIO[R, E, A] => ZIO[R, A, E]
• mapError as the dual as map
• eventually as the dual as forever
• fallbackPlan: executing specify fallback depending on error

---