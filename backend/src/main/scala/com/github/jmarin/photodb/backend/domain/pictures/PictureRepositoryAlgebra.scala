package com.github.jmarin.photodb.backend.domain.pictures
import java.util.UUID

import cats.data.OptionT

trait PictureRepositoryAlgebra[F[_]] {
  def create(picture: Picture): F[Picture]
  def get(id: UUID): OptionT[F, Picture]
  def delete(id: UUID): OptionT[F, Picture]
  def findByKeywords(keywords: Set[Keyword], pageSize: Int, offset: Int): F[List[Picture]]
}
