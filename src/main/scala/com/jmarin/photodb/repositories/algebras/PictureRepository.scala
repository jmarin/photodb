package com.jmarin.photodb.repositories.algebras

import java.util.UUID

import com.jmarin.photodb.model.Picture

trait PictureRepository[F[_]] {
  def create(picture: Picture): F[Picture]
  def get(id: UUID): F[Option[Picture]]
  def delete(id: UUID): F[Option[Picture]]
}
