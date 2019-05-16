package com.github.jmarin.photodb.backend.domain.pictures.interpreters

import com.github.jmarin.photodb.backend.domain.pictures.algebras.PictureRepositoryAlgebra
import com.github.jmarin.photodb.backend.domain.pictures.model.Picture
import cats.data.OptionT
import java.util.UUID
import com.github.jmarin.photodb.backend.domain.pictures.model.Keyword
import scala.collection.concurrent.TrieMap
import cats.Applicative
import cats.implicits._

class PictureRepositoryInMemoryInterpreter[F[_]: Applicative] extends PictureRepositoryAlgebra[F] {

  private val cache = new TrieMap[UUID, Picture]

  override def create(picture: Picture): F[Picture] = {
    cache += (picture.id -> picture)
    picture.pure[F]
  }

  override def get(id: UUID): OptionT[F, Picture] =
    OptionT.fromOption(cache.get(id))

  override def delete(id: UUID): OptionT[F, Picture] =
    OptionT.fromOption(cache.remove(id))

  override def findByKeywords(keywords: Set[Keyword],
                              pageSize: Int,
                              offset: Int): F[List[Picture]] = {

    val filtered: List[Picture] = cache.values
      .filter(
        p =>
          p.metadata.keywords
            .map(_.value.toLowerCase)
            .toSet
            .intersect(keywords.map(_.value.toLowerCase))
            .nonEmpty)
      .toList
      .slice(offset, offset + pageSize)

    filtered.pure[F]

  }

}

object PictureRepositoryInMemoryInterpreter {
  def apply[F[_]: Applicative] =
    new PictureRepositoryInMemoryInterpreter[F]
}
