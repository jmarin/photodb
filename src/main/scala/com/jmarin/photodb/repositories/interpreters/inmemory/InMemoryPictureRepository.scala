package com.jmarin.photodb.repositories.interpreters.inmemory

import java.util.UUID

import cats.Id
import cats.syntax.option._
import com.jmarin.photodb.model.Picture
import com.jmarin.photodb.repositories.algebras.PictureRepository

class InMemoryPictureRepository extends PictureRepository[Id] {

  var pictures = Map.empty[UUID, Picture]

  override def create(picture: Picture): Id[Picture] = {
    pictures = pictures + (picture.id -> picture)
    picture
  }

  override def get(id: UUID): Id[Option[Picture]] = {
    pictures.get(id)
  }

  override def delete(id: UUID): Id[Option[Picture]] = {
    pictures.get(id).fold[Option[Picture]](ifEmpty = None) { existingPicture =>
      pictures = pictures - existingPicture.id
      existingPicture.some
    }
  }

}

object InMemoryPictureRepository extends InMemoryPictureRepository
