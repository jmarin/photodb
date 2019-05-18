package com.github.jmarin.photodb.backend.domain.pictures.interpreters.validation

import cats.Monad
import cats.data.EitherT
import cats.implicits._
import com.github.jmarin.photodb.backend.domain.pictures.algebras.repositories.ImageRepositoryAlgebra
import com.github.jmarin.photodb.backend.domain.pictures.algebras.validation.ImageValidationAlgebra
import com.github.jmarin.photodb.backend.domain.pictures.model.ImageAlreadyExistsError

class ImageValidationInterpreter[F[_]: Monad](imageRepository: ImageRepositoryAlgebra[F])
    extends ImageValidationAlgebra[F] {

  override def doesNotExist(path: String): EitherT[F, ImageAlreadyExistsError, Unit] = EitherT {

    imageRepository.get(path).value.map {
      case Some(i) => Left(ImageAlreadyExistsError(path))
      case None    => Right(())
    }
  }

}

object ImageValidationInterpreter {
  def apply[F[_]: Monad](imageRepository: ImageRepositoryAlgebra[F]): ImageValidationAlgebra[F] =
    new ImageValidationInterpreter[F](imageRepository)
}
