package com.github.jmarin.photodb.backend.domain.pictures

import java.util.UUID

import cats.Monad
import cats.syntax.all._
import cats.data.EitherT
import com.github.jmarin.photodb.backend.domain.pictures.algebras.{
  PictureRepositoryAlgebra,
  PictureValidationAlgebra
}
import com.github.jmarin.photodb.backend.domain.pictures.model.{
  Keyword,
  Picture,
  PictureAlreadyExistsError,
  PictureNotFoundError
}

class PictureService[F[_]: Monad](repository: PictureRepositoryAlgebra[F],
                                  validation: PictureValidationAlgebra[F]) {

  def createPicture(picture: Picture): EitherT[F, PictureAlreadyExistsError, Picture] =
    for {
      _     <- validation.doesNotExist(picture)
      saved <- EitherT.liftF(repository.create(picture))
    } yield saved

  def getPicture(pictureId: UUID): EitherT[F, PictureNotFoundError.type, Picture] =
    repository.get(pictureId).toRight(PictureNotFoundError)

  def deletePicture(pictureId: UUID): F[Unit] =
    repository.delete(pictureId).value.void

  def findPictureByKeywords(keywords: Set[Keyword], pageSize: Int, offset: Int): F[List[Picture]] =
    repository.findByKeywords(keywords, pageSize, offset)

}

object PictureService {
  def apply[F[_]: Monad](repository: PictureRepositoryAlgebra[F],
                         validation: PictureValidationAlgebra[F]) =
    new PictureService[F](repository, validation)
}
