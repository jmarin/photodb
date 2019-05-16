package com.github.jmarin.photodb.backend.domain.pictures.model

import java.util.UUID

sealed trait PictureValidationError              extends Product with Serializable
case class PictureAlreadyExistsError(uuid: UUID) extends PictureValidationError
case object PictureNotFoundError                 extends PictureValidationError
