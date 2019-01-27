package com.jmarin.photodb.repositories.algebras

import java.util.UUID

import com.jmarin.photodb.model.{Keyword, Picture}

trait PictureRepository[F[_], G[_]] {
  def create(picture: Picture): F[Picture]
  def get(id: UUID): F[Option[Picture]]
  def delete(id: UUID): F[Option[Picture]]
  def findAll(): G[Picture]
  def findByKeywords(keywords: Set[Keyword]): G[Picture]
}
