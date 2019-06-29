package com.github.jmarin.photodb.backend.domain.pictures.service

import java.util.UUID

import cats.Monad
import cats.data.{EitherT, OptionT}
import cats.implicits._
import com.github.jmarin.photodb.backend.domain.pictures.algebras.repositories.PictureMetadataRepositoryAlgebra
import com.github.jmarin.photodb.backend.domain.pictures.algebras.validation.PictureMetadataValidationAlgebra
import com.github.jmarin.photodb.backend.domain.pictures.model.{Keyword, PictureMetadata, PictureMetadataAlreadyExistsError, PictureMetadataNotFoundError}

class PictureMetadataService[F[_]: Monad](
                                   repository: PictureMetadataRepositoryAlgebra[F],
                                   validation: PictureMetadataValidationAlgebra[F]
) {

  def createPicture(picture: PictureMetadata): EitherT[F, PictureMetadataAlreadyExistsError, PictureMetadata] =
    for {
      _     <- validation.doesNotExist(picture)
      saved <- EitherT.liftF(repository.create(picture))
    } yield saved

  def updatePicture(picture: PictureMetadata): EitherT[F, PictureMetadataNotFoundError.type, PictureMetadata] =
    for {
      _       <- validation.exists(picture.id.some)
      updated <- EitherT.fromOptionF(repository.update(picture).value, PictureMetadataNotFoundError)
    } yield updated

  def getPicture(pictureId: UUID): EitherT[F, PictureMetadataNotFoundError.type, PictureMetadata] =
    repository.get(pictureId).toRight(PictureMetadataNotFoundError)

  def deletePicture(pictureId: UUID): OptionT[F, UUID] =
    repository.delete(pictureId).map(p => p.id)

  def findPictureByKeywords(keywords: Set[Keyword], pageSize: Int, offset: Int): F[List[PictureMetadata]] =
    repository.findByKeywords(keywords, pageSize, offset)

}

object PictureMetadataService {
  def apply[F[_]: Monad](
                          repository: PictureMetadataRepositoryAlgebra[F],
                          validation: PictureMetadataValidationAlgebra[F]
  ) =
    new PictureMetadataService[F](repository, validation)
}
