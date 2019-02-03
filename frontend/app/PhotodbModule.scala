import cats.Id
import com.google.inject.AbstractModule
import com.jmarin.photodb.backend.repositories.algebras.PictureRepository
import com.jmarin.photodb.backend.repositories.interpreters.inmemory.InMemoryPictureRepository

class PhotodbModule extends AbstractModule {
  override def configure(): Unit = {
    bind(classOf[PictureRepository[Id]]).toInstance(InMemoryPictureRepository)
  }
}
