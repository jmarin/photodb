package com.github.jmarin.photodb.backend.domain.pictures

import java.nio.file.Paths
import java.util.UUID

import cats.Id
import org.scalatest.{Matchers, WordSpec}
import com.github.jmarin.photodb.backend.domain.pictures.model._
import com.github.jmarin.photodb.backend.domain.pictures.interpreters.PictureRepositoryInMemoryInterpreter
import com.github.jmarin.photodb.backend.domain.pictures.interpreters.PictureValidationInterpreter
import scala.collection.immutable

class PictureServiceSpec extends WordSpec with Matchers {

  val pictureRepository = PictureRepositoryInMemoryInterpreter.apply[Id]
  val pictureValidation = PictureValidationInterpreter.apply[Id](pictureRepository)
  val pictureService    = PictureService(pictureRepository, pictureValidation)

  val pic1 = Picture(
    UUID.randomUUID(),
    Paths.get("").toAbsolutePath,
    PictureMetadata(immutable.Seq(Keyword("travel"), Keyword("portrait")))
  )

  val pic2 = Picture(
    UUID.randomUUID(),
    Paths.get("").toAbsolutePath,
    PictureMetadata(immutable.Seq(Keyword("travel")))
  )

  val pic3 = Picture(
    UUID.randomUUID(),
    Paths.get("").toAbsolutePath,
    PictureMetadata(immutable.Seq(Keyword("events")))
  )

  "Picture Service" should {
    "create picture" in {
      pictureService.getPicture(pic1.id).value shouldEqual Left(PictureNotFoundError)
      pictureService.createPicture(pic1).value shouldEqual Right(pic1)
      pictureService.createPicture(pic2).value shouldEqual Right(pic2)
      pictureService.createPicture(pic3).value shouldEqual Right(pic3)
    }
    "return error when creating an image that already exists" in {
      pictureService.createPicture(pic3).value shouldEqual Left(PictureAlreadyExistsError(pic3.id))
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
    "delete picture" in {
      pictureService.deletePicture(pic1.id).value shouldEqual Some(pic1.id)
      pictureService.getPicture(pic1.id).value shouldEqual Left(PictureNotFoundError)
      pictureService.deletePicture(pic2.id).value shouldEqual Some(pic2.id)
      pictureService.getPicture(pic2.id).value shouldEqual Left(PictureNotFoundError)
      pictureService.deletePicture(pic3.id).value shouldEqual Some(pic3.id)
      pictureService.getPicture(pic3.id).value shouldEqual Left(PictureNotFoundError)
    }

  }

}
