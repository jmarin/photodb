package com.jmarin.photodb.backend.repositories.algebras
import java.util.UUID

import akka.NotUsed
import akka.stream.scaladsl.Source
import com.jmarin.photodb.backend.model.{Keyword, Picture}

trait PictureRepository[F[_]] {
  def create(picture: Picture): F[Picture]
  def get(id: UUID): F[Option[Picture]]
  def delete(id: UUID): F[Option[Picture]]
  def findAll(): Source[Picture, NotUsed]
  def findByKeywords(keywords: Set[Keyword]): Source[Picture, NotUsed]
}
