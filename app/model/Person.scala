package model

import java.util.UUID

import play.api.libs.json.{JsValue, Json}

trait Person {
  val id: UUID
  val name: String
}

case class Candidate(id: UUID, name: String) extends Person

object CandidateFormat {

  import model.TimeslotFormat._

  implicit val candidateFormat = Json.format[Candidate]

  implicit class CandidateOpsTo(candidate: Candidate) {
    def toJson = Json.toJson(candidate)
  }

  implicit class CandidateOpsFrom(candidate: Candidate.type) {
    def fromJson(json: JsValue) = Json.fromJson[Candidate](json)
  }

}

case class Interviewer(id: UUID, name: String) extends Person

object InterviewerFormat {

  import model.TimeslotFormat._

  implicit val interviewerFormat = Json.format[Interviewer]

  implicit class InterviewerOpsTo(interviewer: Interviewer) {
    def toJson = Json.toJson(interviewer)
  }

  implicit class InterviewerOpsFrom(interviewer: Interviewer.type) {
    def fromJson(json: JsValue) = Json.fromJson[Interviewer](json)
  }

}