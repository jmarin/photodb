package com.github.jmarin.backend.domain.pictures.interpreters

import cats.Applicative
import com.github.jmarin.photodb.backend.domain.pictures.algebras.ImageRepositoryAlgebra
import cats.data.OptionT
import scala.collection.concurrent.TrieMap
import cats.implicits._

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
