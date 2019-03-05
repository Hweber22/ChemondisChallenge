package model

import org.scalatest.{FlatSpec, Matchers}

class PersonTest extends FlatSpec with Matchers {

  "Person" should "serialize to Json and the Json should deserialize to Person" in {
    import model.PersonFormat._

    val person = Person(
      name = "Henning",
      typ = "Candidate",
      availableSlots = List(Timeslot("Tu", 15))
    )
    Person.fromJson(person.toJson).get should equal(person)
  }
}