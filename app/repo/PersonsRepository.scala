package repo


import javax.inject.Inject
import model._

import scala.concurrent.{ExecutionContext, Future}

trait PersonsRepository {
  def setFreeTimeslots(person: Person, timeslots: List[Timeslot]): Future[Unit]
  def showAvailability: Future[List[Person]]
}

class PersonsRepositoryImpl @Inject()(implicit ec: ExecutionContext) extends PersonsRepository {
  var persons = List[Person]()
  override def showAvailability: Future[List[Person]] = Future.successful(persons)

  override def setFreeTimeslots(person: Person, timeslots: List[Timeslot])=

    Future.successful {
      val personWithTimeslots: Person = Person(person.name, person.typ, timeslots)

      persons = persons ++ List(personWithTimeslots)

    }
}