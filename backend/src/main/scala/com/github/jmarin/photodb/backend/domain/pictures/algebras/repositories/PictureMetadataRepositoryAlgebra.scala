package com.github.jmarin.photodb.backend.domain.pictures.algebras.repositories

import java.util.UUID

import cats.data.OptionT
import com.github.jmarin.photodb.backend.domain.pictures.model.{Keyword, PictureMetadata}

trait PictureMetadataRepositoryAlgebra[F[_]] {
  def create(picture: PictureMetadata): F[PictureMetadata]
  def update(picture: PictureMetadata): OptionT[F, PictureMetadata]
  def get(id: UUID): OptionT[F, PictureMetadata]
  def delete(id: UUID): OptionT[F, PictureMetadata]
  def findByKeywords(keywords: Set[Keyword], pageSize: Int, offset: Int): F[List[PictureMetadata]]
}
