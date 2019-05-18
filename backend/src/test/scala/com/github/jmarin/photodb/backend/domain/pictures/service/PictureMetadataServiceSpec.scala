package com.github.jmarin.photodb.backend.domain.pictures.service

import java.util.UUID

import cats.Id
import org.scalatest.{Matchers, WordSpec}
import com.github.jmarin.photodb.backend.domain.pictures.model._
import com.github.jmarin.photodb.backend.domain.pictures.interpreters.repositories.inmemory.PictureMetadataRepositoryInMemoryInterpreter
import com.github.jmarin.photodb.backend.domain.pictures.interpreters.validation.PictureMetadataValidationInterpreter

import scala.collection.immutable

class PictureMetadataServiceSpec extends WordSpec with Matchers {

  val pictureRepository = PictureMetadataRepositoryInMemoryInterpreter.apply[Id]
  val pictureValidation = PictureMetadataValidationInterpreter.apply[Id](pictureRepository)
  val pictureService    = PictureMetadataService(pictureRepository, pictureValidation)

  val pic1 = PictureMetadata(
    UUID.randomUUID(),
    "",
    immutable.Seq(Keyword("travel"), Keyword("portrait"))
  )

  val pic2 = PictureMetadata(
    UUID.randomUUID(),
    "",
    immutable.Seq(Keyword("travel"))
  )

  val pic3 = PictureMetadata(
    UUID.randomUUID(),
    "",
    immutable.Seq(Keyword("events"))
  )

  "Picture Service" should {
    "create picture" in {
      pictureService.getPicture(pic1.id).value shouldEqual Left(PictureMetadataNotFoundError$)
      pictureService.createPicture(pic1).value shouldEqual Right(pic1)
      pictureService.createPicture(pic2).value shouldEqual Right(pic2)
      pictureService.createPicture(pic3).value shouldEqual Right(pic3)
    }
    "return error when creating an image that already exists" in {
      pictureService.createPicture(pic3).value shouldEqual Left(PictureMetadataAlreadyExistsError(pic3.id))
    }
    "get picture" in {
      pictureService.getPicture(pic1.id).value shouldEqual Right(pic1)
      pictureService.getPicture(pic2.id).value shouldEqual Right(pic2)
      pictureService.getPicture(pic3.id).value shouldEqual Right(pic3)
    }
    "find pictures by keyword" in {
      val travelPictures = pictureService.findPictureByKeywords(Set(Keyword("travel")), 10, 0)
      travelPictures.size should equal(2)
      val portraitPictures = pictureService.findPictureByKeywords(Set(Keyword("portrait")), 10, 0)
      portraitPictures.size should equal(1)

      val noKeywords = pictureService.findPictureByKeywords(Set.empty, 10, 0)
      noKeywords.size should equal(0)
    }
    "update picture" in {
      val existing = pictureService.getPicture(pic1.id).value
      val updated =
        existing.map(picture => picture.copy(path = "/new/path"))
      pictureService
        .updatePicture(
          updated
            .getOrElse(PictureMetadata(UUID.randomUUID(), "", Nil))
        )
        .value shouldEqual updated
    }
    "delete picture" in {
      pictureService.deletePicture(pic1.id).value shouldEqual Some(pic1.id)
      pictureService.getPicture(pic1.id).value shouldEqual Left(PictureMetadataNotFoundError$)
      pictureService.deletePicture(pic2.id).value shouldEqual Some(pic2.id)
      pictureService.getPicture(pic2.id).value shouldEqual Left(PictureMetadataNotFoundError$)
      pictureService.deletePicture(pic3.id).value shouldEqual Some(pic3.id)
      pictureService.getPicture(pic3.id).value shouldEqual Left(PictureMetadataNotFoundError$)
    }

  }

}
