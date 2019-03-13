package model

import play.api.libs.json.{JsValue, Json}

case class Appointments(candidate: Candidate, interviewers: List[Interviewer], possibleDates: List[Timeslot])

object Appointments {

  def commonSlots(slotlists: List[List[Timeslot]], acc: List[Timeslot]): List[Timeslot] = {
    slotlists match {
      case Nil => Nil
      case List(slots) => slots.filter(slot => acc.contains(slot))
      case x :: xs => commonSlots(xs, x.filter(slot => acc.contains(slot)))
    }
  }

  def possibleAppointments(persons: Map[Person, List[Timeslot]]): Either[String, Appointments] = {
    val personList = persons.keys.toList
    val candidates: List[Candidate] = personList.collect {
      case p: Candidate => p
    }
    val interviewers: List[Interviewer] = personList.collect {
      case p: Interviewer => p
    }
    if (candidates.length == 1 && interviewers.nonEmpty) {
      val slots = commonSlots(persons.values.toList, persons(candidates.head))
      Right(Appointments(candidates.head, interviewers, slots))
    }
    else Left("must be one candidate and one or more interviewers")
  }
}

object AppointmentsFormat {

  import TimeslotFormat._
  import model.CandidateFormat._
  import model.InterviewerFormat._

  implicit val appointmentsFormat = Json.format[Appointments]

  implicit class AppointmentsOpsTo(appointments: Appointments) {
    def toJson = Json.toJson(appointments)
  }

  implicit class AppointmentsOpsFrom(appointments: Appointments.type) {
    def fromJson(json: JsValue) = Json.fromJson[Appointments](json)
  }

}