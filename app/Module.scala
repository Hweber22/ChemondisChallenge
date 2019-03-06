import com.google.inject.AbstractModule
import repo.{PersonsRepository, PersonsRepositoryImpl}

class Module extends AbstractModule {

  override def configure() = {
    bind(classOf[PersonsRepository]).to(classOf[PersonsRepositoryImpl]).asEagerSingleton()
  }
}