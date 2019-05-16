package com.github.jmarin.photodb.backend.domain.pictures.algebras

import cats.data.OptionT
import java.awt.image.BufferedImage

trait ImageRepositoryAlgebra[F[_]] {
  def create(path: String, image: BufferedImage): F[BufferedImage]
  def get(path: String): OptionT[F, BufferedImage]
  def delete(path: String): OptionT[F, BufferedImage]
}
