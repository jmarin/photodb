package com.github.jmarin.photodb.backend.domain.pictures.algebras.repositories

import java.util.UUID

import cats.data.OptionT
import com.github.jmarin.photodb.backend.domain.pictures.model.{Keyword, Picture}

trait PictureRepositoryAlgebra[F[_]] {
  def create(picture: Picture): F[Picture]
  def update(picture: Picture): OptionT[F, Picture]
  def get(id: UUID): OptionT[F, Picture]
  def delete(id: UUID): OptionT[F, Picture]
  def findByKeywords(keywords: Set[Keyword], pageSize: Int, offset: Int): F[List[Picture]]
}
