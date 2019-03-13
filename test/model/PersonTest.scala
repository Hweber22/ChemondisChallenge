package model

import org.scalatest.{FlatSpec, Matchers}
import java.util.UUID

class PersonTest extends FlatSpec with Matchers {

  "Person" should "serialize to Json and the Json should deserialize to Person" in {
    import model.CandidateFormat._

    val person = Candidate(
      id = UUID.fromString("12345678-1234-1234-1234-1234567890ab"),
      name = "Henning"
    )
    Candidate.fromJson(person.toJson).get should equal(person)
  }
}