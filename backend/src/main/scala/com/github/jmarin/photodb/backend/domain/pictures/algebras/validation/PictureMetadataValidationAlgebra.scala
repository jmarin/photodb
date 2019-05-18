package com.github.jmarin.photodb.backend.domain.pictures.algebras.validation

import java.util.UUID

import cats.data.EitherT
import com.github.jmarin.photodb.backend.domain.pictures.model.{PictureMetadata, PictureMetadataAlreadyExistsError, PictureMetadataNotFoundError$}

trait PictureMetadataValidationAlgebra[F[_]] {
  /* Fails with PictureAlreadyExistsError (i.e. when trying to create an image)*/
  def doesNotExist(picture: PictureMetadata): EitherT[F, PictureMetadataAlreadyExistsError, Unit]

  /* Fails with PictureNotFoundError if the picture id does not exist or is None (i.e. when trying to update an image) */
  def exists(pictureId: Option[UUID]): EitherT[F, PictureMetadataNotFoundError$.type, Unit]
}
