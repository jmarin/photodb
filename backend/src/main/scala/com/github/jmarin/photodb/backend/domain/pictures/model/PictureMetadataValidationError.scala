package com.github.jmarin.photodb.backend.domain.pictures.model

import java.util.UUID

sealed trait PictureMetadataValidationError              extends Product with Serializable
case class PictureMetadataAlreadyExistsError(uuid: UUID) extends PictureMetadataValidationError
case object PictureMetadataNotFoundError                 extends PictureMetadataValidationError
