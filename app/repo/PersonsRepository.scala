package repo

import java.util.UUID

import javax.inject.Inject
import model._

import scala.concurrent.{ExecutionContext, Future}

trait PersonsRepository {
  def addCandidate(candidate: Candidate): Future[Unit]

  def addInterviewer(interviewer: Interviewer): Future[Unit]

  def setFreeTimeslots(person: Person, timeslots: List[Timeslot]): Future[Unit]

  def addedCandidates: Future[Map[Candidate, List[Timeslot]]]

  def addedInterviewers: Future[Map[Interviewer, List[Timeslot]]]

  def addedPersons: Future[Map[Person, List[Timeslot]]]

  def findPersonByID(id: UUID): Future[Person]
}

class PersonsRepositoryImpl @Inject()(implicit ec: ExecutionContext) extends PersonsRepository {
  var candidates = Map[Candidate, List[Timeslot]]()
  var interviewers = Map[Interviewer, List[Timeslot]]()

  override def addCandidate(candidate: Candidate) =
    Future.successful {
      candidates = candidates ++ Map(candidate -> List.empty)
    }

  override def addInterviewer(interviewer: Interviewer) =
    Future.successful {
      interviewers = interviewers ++ Map(interviewer -> List.empty)
    }

  override def setFreeTimeslots(person: Person, timeslots: List[Timeslot]) =
    Future.successful {
      person match {
        case p: Candidate => candidates = candidates ++ Map(p -> timeslots)
        case p: Interviewer => interviewers = interviewers ++ Map(p -> timeslots)

      }
    }

  override def addedCandidates: Future[Map[Candidate, List[Timeslot]]] = Future.successful(candidates)

  override def addedInterviewers: Future[Map[Interviewer, List[Timeslot]]] = Future.successful(interviewers)

  override def addedPersons: Future[Map[Person, List[Timeslot]]] = Future.successful(candidates ++ interviewers)

  override def findPersonByID(id: UUID): Future[Person] =
    Future.successful {
      (candidates ++ interviewers).keys.filter(person => person.id == id).head
    }
}