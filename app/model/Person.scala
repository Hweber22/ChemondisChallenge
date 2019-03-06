package model

import play.api.libs.json.{JsValue, Json}

case class Person(typ: String, name: String, availableSlots: List[Timeslot])

object PersonFormat {
  import model.TimeslotFormat._

  implicit val personFormat = Json.format[Person]

  implicit class PersonOpsTo(person: Person) {
    def toJson = Json.toJson(person)
  }

  implicit class PersonOpsFrom(person: Person.type) {
    def fromJson(json: JsValue) = Json.fromJson[Person](json)
  }
}

