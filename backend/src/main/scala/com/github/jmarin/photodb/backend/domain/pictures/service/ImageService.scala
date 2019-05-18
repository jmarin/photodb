package com.github.jmarin.photodb.backend.domain.pictures.service
import cats.Monad
import cats.data.{EitherT, OptionT}
import com.github.jmarin.photodb.backend.domain.pictures.algebras.repositories.ImageRepositoryAlgebra
import com.github.jmarin.photodb.backend.domain.pictures.algebras.validation.ImageValidationAlgebra
import com.github.jmarin.photodb.backend.domain.pictures.model.ImageAlreadyExistsError
import com.github.jmarin.photodb.backend.domain.pictures.model.ImageNotFoundError

class ImageService[F[_]: Monad](
    imageRepository: ImageRepositoryAlgebra[F],
    imageValidation: ImageValidationAlgebra[F]
) {

  def createImage(
      path: String,
      image: Array[Byte]
  ): EitherT[F, ImageAlreadyExistsError, Array[Byte]] = {
    for {
      _     <- imageValidation.doesNotExist(path)
      saved <- EitherT.liftF(imageRepository.create(path, image))
    } yield saved
  }

  def get(path: String): EitherT[F, ImageNotFoundError.type, Array[Byte]] =
    imageRepository.get(path).toRight(ImageNotFoundError)

  def delete(path: String): OptionT[F, String] =
    imageRepository.delete(path).map(_ => path)
}

object ImageService {
  def apply[F[_]: Monad](
      imageRepository: ImageRepositoryAlgebra[F],
      imageValidation: ImageValidationAlgebra[F]
  ): ImageService[F] =
    new ImageService[F](imageRepository, imageValidation)
}
