package com.github.jmarin.photodb.backend.domain.pictures

import java.util.UUID

import cats.Monad
import cats.data.{EitherT, OptionT}
import cats.implicits._
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

class PictureService[F[_]: Monad](
    repository: PictureRepositoryAlgebra[F],
    validation: PictureValidationAlgebra[F]
) {

  def createPicture(picture: Picture): EitherT[F, PictureAlreadyExistsError, Picture] =
    for {
      _     <- validation.doesNotExist(picture)
      saved <- EitherT.liftF(repository.create(picture))
    } yield saved

  def updatePicture(picture: Picture): EitherT[F, PictureNotFoundError.type, Picture] =
    for {
      _       <- validation.exists(picture.id.some)
      updated <- EitherT.fromOptionF(repository.update(picture).value, PictureNotFoundError)
    } yield updated

  def getPicture(pictureId: UUID): EitherT[F, PictureNotFoundError.type, Picture] =
    repository.get(pictureId).toRight(PictureNotFoundError)

  def deletePicture(pictureId: UUID): OptionT[F, UUID] =
    repository.delete(pictureId).map(p => p.id)

  def findPictureByKeywords(keywords: Set[Keyword], pageSize: Int, offset: Int): F[List[Picture]] =
    repository.findByKeywords(keywords, pageSize, offset)

}

object PictureService {
  def apply[F[_]: Monad](
      repository: PictureRepositoryAlgebra[F],
      validation: PictureValidationAlgebra[F]
  ) =
    new PictureService[F](repository, validation)
}
