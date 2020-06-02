// <- Tools ************************************************

import scala.reflect.runtime.universe._
def prettyType[T: TypeTag](
  v: T,
  level: Int = 1
) = typeOf[T].baseClasses.map(typeOf[T].baseType).take(level).mkString(", ")

// ->

// <- Variance and bounds ************************************************

// <- Covariance and lower bound ************************************************

sealed trait Animal extends Product with Serializable
case object Dog extends Animal
case object Cat extends Animal

val dogs = List(Dog, Dog)
prettyType(dogs)

val cats = List(Cat, Cat, Cat)
prettyType(cats)

// List[+A]
// → A is covariant
// List[Dog] and List[Cat] are subtypes of List[Animal]

// def ++[B >: A](others: List[B]): List[B]
// → B is a lower bound type: A subtype of B
val animals = dogs ++ cats
prettyType(animals)

// ->

// <- Contravariance and upper bound ************************************************

// → A is contravariant
// if A2 is a subtype of A1, Module[A1] is a subtype of Module[A2]
trait Module[-A] {
  // → B is a upper bound type: B subtype of A
  def ++[B <: A](b: Module[B]): Module[B] = buildModule
}

trait Database
trait Email

// All is a subtype of Database and Email
type All = Database with Email

def buildModule[R]: Module[R] = new Module[R] {}

val dbModule: Module[Database] = buildModule[Database]
val emailModule: Module[Email] = buildModule[Email]
// Module[Database] or Module[Email] are subtypes of Module[Database with Email]
val globalModule: Module[All] = dbModule ++ emailModule

// ->

// ->
