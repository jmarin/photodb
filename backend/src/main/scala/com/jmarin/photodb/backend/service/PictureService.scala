package com.jmarin.photodb.backend.service

import java.util.UUID

import cats.{Functor, Monad}
import cats.syntax.all._
import com.jmarin.photodb.backend.model.{Keyword, Picture}
import com.jmarin.photodb.backend.repositories.algebras.PictureRepository
sealed trait PictureError
case class PictureAlreadyExists(id: UUID) extends PictureError

class PictureService[F[_]: Monad, G[_]: Functor](repository: PictureRepository[F, G]) {

  def create(picture: Picture): F[Either[PictureError, Picture]] =
    get(picture.id).flatMap {
      case None =>
        repository.create(picture).map(_.asRight)

      case Some(existingPicture) =>
        Monad[F].pure(PictureAlreadyExists(existingPicture.id)).map(_.asLeft)
    }

  def get(pictureId: UUID): F[Option[Picture]] = repository.get(pictureId)

  def remove(pictureId: UUID): F[Option[Picture]] = repository.delete(pictureId)

  def findAll(): G[Picture] = repository.findAll()

  def findByKeywords(keywords: Set[Keyword]): G[Picture] = repository.findByKeywords(keywords)

}

object PictureService {
  def apply[F[_]: Monad, G[_]: Functor](pictureRepository: PictureRepository[F, G]) =
    new PictureService[F, G](pictureRepository)
}
