package com.github.jmarin.photodb.backend.domain.pictures.algebras

import cats.data.EitherT
import java.util.UUID

import com.github.jmarin.photodb.backend.domain.pictures.model.{Picture, PictureAlreadyExistsError, PictureNotFoundError}

trait PictureValidationAlgebra[F[_]] {
  /* Fails with PictureAlreadyExistsError */
  def doesNotExist(picture: Picture): EitherT[F, PictureAlreadyExistsError, Unit]

  /* Fails with PictureNotFoundError if the picture id does not exist or is None */
  def exists(pictureId: Option[UUID]): EitherT[F, PictureNotFoundError.type, Unit]
}