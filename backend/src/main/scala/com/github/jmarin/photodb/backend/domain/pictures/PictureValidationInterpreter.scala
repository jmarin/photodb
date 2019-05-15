package com.github.jmarin.photodb.backend.domain.pictures
import cats.data.EitherT
import java.util.UUID
import cats.Monad
import cats.implicits._

class PictureValidationInterpreter[F[_]: Monad](pictureRepository: PictureRepositoryAlgebra[F])
    extends PictureValidationAlgebra[F] {
  override def doesNotExist(picture: Picture): EitherT[F, PictureAlreadyExistsError, Unit] =
    EitherT {
      pictureRepository.get(picture.id).value.map { matches =>
        if (matches.forall(possibleMatch => possibleMatch.id != picture.id)) {
          Right(())
        } else {
          Left(PictureAlreadyExistsError(picture.id))
        }
      }
    }

  override def exists(pictureId: Option[UUID]): EitherT[F, PictureNotFoundError.type, Unit] =
    EitherT {
      pictureId match {
        case Some(id) =>
          pictureRepository.get(id).value.map {
            case Some(_) => Right(())
            case None    => Left(PictureNotFoundError)
          }
        case None => Either.left[PictureNotFoundError.type, Unit](PictureNotFoundError).pure[F]
      }
    }

}

object PictureValidationInterpreter {
  def apply[F[_]: Monad](
      pictureRepository: PictureRepositoryAlgebra[F]): PictureValidationInterpreter[F] =
    new PictureValidationInterpreter[F](pictureRepository)
}
