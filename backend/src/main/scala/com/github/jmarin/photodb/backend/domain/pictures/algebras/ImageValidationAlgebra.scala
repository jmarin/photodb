package com.github.jmarin.photodb.backend.domain.pictures.algebras

import cats.data.EitherT
import com.github.jmarin.photodb.backend.domain.pictures.model.ImageAlreadyExistsError

trait ImageValidationAlgebra[F[_]] {
  def doesNotExist(path: String): EitherT[F, ImageAlreadyExistsError, Unit]
}
