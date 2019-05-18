package com.github.jmarin.photodb.backend.domain.pictures.interpreters.repositories.inmemory

import java.util.UUID

import cats.Applicative
import cats.data.OptionT
import cats.implicits._
import com.github.jmarin.photodb.backend.domain.pictures.algebras.repositories.PictureRepositoryAlgebra
import com.github.jmarin.photodb.backend.domain.pictures.model.{Keyword, Picture}

import scala.collection.concurrent.TrieMap

class PictureRepositoryInMemoryInterpreter[F[_]: Applicative] extends PictureRepositoryAlgebra[F] {

  private val cache = new TrieMap[UUID, Picture]

  override def create(picture: Picture): F[Picture] = {
    cache += (picture.id -> picture)
    picture.pure[F]
  }

  override def update(picture: Picture): OptionT[F, Picture] = OptionT {
    cache.update(picture.id, picture)
    picture.some.pure[F]
  }

  override def get(id: UUID): OptionT[F, Picture] =
    OptionT.fromOption(cache.get(id))

  override def delete(id: UUID): OptionT[F, Picture] =
    OptionT.fromOption(cache.remove(id))

  override def findByKeywords(
      keywords: Set[Keyword],
      pageSize: Int,
      offset: Int
  ): F[List[Picture]] = {

    val filtered: List[Picture] = cache.values
      .filter(
        p =>
          p.metadata.keywords
            .map(_.value.toLowerCase)
            .toSet
            .intersect(keywords.map(_.value.toLowerCase))
            .nonEmpty
      )
      .toList
      .slice(offset, offset + pageSize)

    filtered.pure[F]

  }

}

object PictureRepositoryInMemoryInterpreter {
  def apply[F[_]: Applicative] =
    new PictureRepositoryInMemoryInterpreter[F]
}
