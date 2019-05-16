package com.github.jmarin.photodb.backend.domain.pictures.interpreters

import cats.Monad
import com.github.jmarin.photodb.backend.domain.pictures.algebras.ImageValidationAlgebra
import cats.data.EitherT
import cats.implicits._
import com.github.jmarin.photodb.backend.domain.pictures.model.ImageAlreadyExistsError
import com.github.jmarin.photodb.backend.domain.pictures.algebras.ImageRepositoryAlgebra

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
