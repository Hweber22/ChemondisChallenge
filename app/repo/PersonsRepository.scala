package repo

import javax.inject.Inject
import model._

import scala.concurrent.{ExecutionContext, Future}

trait PersonsRepository {
  def setFreeTimeslots(name: String, typ: String, timeslots: List[Timeslot]): Future[Unit]
  def addedPersons: Future[List[Person]]
}

class PersonsRepositoryImpl @Inject()(implicit ec: ExecutionContext) extends PersonsRepository {
  var persons = List[Person]()

  override def setFreeTimeslots(name: String, typ: String, timeslots: List[Timeslot])=
    Future.successful {
      val personWithTimeslots = Person(name, typ, timeslots)
      persons = persons ++ List(personWithTimeslots)
    }

  override def addedPersons: Future[List[Person]] = Future.successful(persons)

}