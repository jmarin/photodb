package com.github.jmarin.photodb.backend.domain.pictures.interpreters.repositories.inmemory

import java.util.UUID

import cats.Applicative
import cats.data.OptionT
import cats.implicits._
import com.github.jmarin.photodb.backend.domain.pictures.algebras.repositories.PictureMetadataRepositoryAlgebra
import com.github.jmarin.photodb.backend.domain.pictures.model.{Keyword, PictureMetadata}

import scala.collection.concurrent.TrieMap

class PictureMetadataRepositoryInMemoryInterpreter[F[_]: Applicative] extends PictureMetadataRepositoryAlgebra[F] {

  private val cache = new TrieMap[UUID, PictureMetadata]

  override def create(picture: PictureMetadata): F[PictureMetadata] = {
    cache += (picture.id -> picture)
    picture.pure[F]
  }

  override def update(picture: PictureMetadata): OptionT[F, PictureMetadata] = OptionT {
    cache.update(picture.id, picture)
    picture.some.pure[F]
  }

  override def get(id: UUID): OptionT[F, PictureMetadata] =
    OptionT.fromOption(cache.get(id))

  override def delete(id: UUID): OptionT[F, PictureMetadata] =
    OptionT.fromOption(cache.remove(id))

  override def findByKeywords(
      keywords: Set[Keyword],
      pageSize: Int,
      offset: Int
  ): F[List[PictureMetadata]] = {

    val filtered: List[PictureMetadata] = cache.values
      .filter(
        p =>
          p.keywords
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

object PictureMetadataRepositoryInMemoryInterpreter {
  def apply[F[_]: Applicative] =
    new PictureMetadataRepositoryInMemoryInterpreter[F]
}
