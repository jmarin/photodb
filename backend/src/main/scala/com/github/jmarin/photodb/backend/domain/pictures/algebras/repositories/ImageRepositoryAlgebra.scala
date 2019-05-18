package com.github.jmarin.photodb.backend.domain.pictures.algebras.repositories

import cats.data.OptionT

trait ImageRepositoryAlgebra[F[_]] {
  def create(path: String, image: Array[Byte]): F[Array[Byte]]
  def get(path: String): OptionT[F, Array[Byte]]
  def delete(path: String): OptionT[F, Array[Byte]]
}
