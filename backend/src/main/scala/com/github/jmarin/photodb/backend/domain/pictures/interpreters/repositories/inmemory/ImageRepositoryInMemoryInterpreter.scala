package com.github.jmarin.photodb.backend.domain.pictures.interpreters.repositories.inmemory

import cats.Applicative
import cats.data.OptionT
import cats.implicits._
import com.github.jmarin.photodb.backend.domain.pictures.algebras.repositories.ImageRepositoryAlgebra

import scala.collection.concurrent.TrieMap

class ImageRepositoryInMemoryInterpreter[F[_]: Applicative] extends ImageRepositoryAlgebra[F] {

  private val cache = new TrieMap[String, Array[Byte]]

  override def create(path: String, bytes: Array[Byte]): F[Array[Byte]] = {
    cache += (path -> bytes)
    bytes.pure[F]
  }

  override def get(path: String): OptionT[F, Array[Byte]] =
    OptionT.fromOption(cache.get(path))

  override def delete(path: String): OptionT[F, Array[Byte]] =
    OptionT.fromOption(cache.remove(path))

}

object ImageRepositoryInMemoryInterpreter {
  def apply[F[_]: Applicative]: ImageRepositoryInMemoryInterpreter[F] =
    new ImageRepositoryInMemoryInterpreter[F]
}
