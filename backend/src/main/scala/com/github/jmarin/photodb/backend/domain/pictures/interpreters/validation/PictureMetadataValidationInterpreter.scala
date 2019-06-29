package com.github.jmarin.photodb.backend.domain.pictures.interpreters.validation

import java.util.UUID

import cats.Monad
import cats.data.EitherT
import cats.implicits._
import com.github.jmarin.photodb.backend.domain.pictures.algebras.repositories.PictureMetadataRepositoryAlgebra
import com.github.jmarin.photodb.backend.domain.pictures.algebras.validation.PictureMetadataValidationAlgebra
import com.github.jmarin.photodb.backend.domain.pictures.model.{PictureMetadata, PictureMetadataAlreadyExistsError, PictureMetadataNotFoundError}

class PictureMetadataValidationInterpreter[F[_]: Monad](pictureRepository: PictureMetadataRepositoryAlgebra[F])
    extends PictureMetadataValidationAlgebra[F] {

  override def doesNotExist(picture: PictureMetadata): EitherT[F, PictureMetadataAlreadyExistsError, Unit] =
    EitherT {
      pictureRepository.get(picture.id).value.map { matches =>
        if (matches.forall(possibleMatch => possibleMatch.id != picture.id)) {
          Right(())
        } else {
          Left(PictureMetadataAlreadyExistsError(picture.id))
        }
      }
    }

  override def exists(pictureId: Option[UUID]): EitherT[F, PictureMetadataNotFoundError.type, Unit] =
    EitherT {
      pictureId match {
        case Some(id) =>
          pictureRepository.get(id).value.map {
            case Some(_) => Right(())
            case None    => Left(PictureMetadataNotFoundError)
          }
        case None => Either.left[PictureMetadataNotFoundError.type, Unit](PictureMetadataNotFoundError).pure[F]
      }
    }

}

object PictureMetadataValidationInterpreter {
  def apply[F[_]: Monad](
      pictureRepository: PictureMetadataRepositoryAlgebra[F]
  ): PictureMetadataValidationInterpreter[F] =
    new PictureMetadataValidationInterpreter[F](pictureRepository)
}
