package com.github.jmarin.backend.domain.pictures.interpreters
import cats.Applicative
import com.github.jmarin.photodb.backend.domain.pictures.algebras.ImageRepositoryAlgebra
import java.awt.image.BufferedImage
import cats.data.OptionT
import scala.collection.concurrent.TrieMap
import cats.implicits._

class ImageRepositoryInMemoryInterpreter[F[_]: Applicative] extends ImageRepositoryAlgebra[F] {

  private val cache = new TrieMap[String, BufferedImage]

  override def create(path: String, image: BufferedImage): F[BufferedImage] = {
    cache += (path -> image)
    image.pure[F]
  }

  override def get(path: String): OptionT[F, BufferedImage] =
    OptionT.fromOption(cache.get(path))

  override def delete(path: String): OptionT[F, BufferedImage] =
    OptionT.fromOption(cache.remove(path))

}
