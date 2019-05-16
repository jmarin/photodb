package com.github.jmarin.photodb.backend.domain.pictures.model

sealed trait ImageValidationError
case class ImageAlreadyExistsError(path: String) extends ImageValidationError
case object ImageNotFoundError                   extends ImageValidationError
