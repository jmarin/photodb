package com.github.jmarin.photodb.backend.domain.pictures.interpreters.validation

import cats.Id
import cats.implicits._
import com.github.jmarin.photodb.backend.domain.pictures.interpreters.repositories.inmemory.PictureMetadataRepositoryInMemoryInterpreter
import com.github.jmarin.photodb.backend.domain.pictures.model.{PictureMetadata, PictureMetadataAlreadyExistsError, PictureMetadataArbitraries, PictureMetadataNotFoundError}
import org.scalatest.{Matchers, PropSpec}
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

class PictureMetadataValidationInterpreterSpec
    extends PropSpec
    with ScalaCheckPropertyChecks
    with Matchers
    with PictureMetadataArbitraries {

  val pictureRepository = PictureMetadataRepositoryInMemoryInterpreter.apply[Id]
  val pictureValidation = PictureMetadataValidationInterpreter.apply[Id](pictureRepository)

  property("exists: fails if picture is not in repository") {
    forAll { picture: PictureMetadata =>
      pictureValidation.exists(picture.id.some).value shouldBe Left(PictureMetadataNotFoundError)
      pictureValidation.exists(None).value shouldBe Left(PictureMetadataNotFoundError)
    }
  }

  property("exists: passes if picture is already in repository") {
    forAll { picture: PictureMetadata =>
      pictureRepository.create(picture) shouldEqual picture
      pictureValidation.exists(picture.id.some).value shouldBe Right(())
    }
  }

  property("doesNotExist: passes if picture is not in repository") {
    forAll { picture: PictureMetadata =>
      pictureValidation.doesNotExist(picture).value shouldBe Right(())
    }
  }

  property("doesNotExist: fails if picture is already in repository") {
    forAll { picture: PictureMetadata =>
      pictureRepository.create(picture) shouldEqual picture
      pictureValidation.doesNotExist(picture).value shouldBe Left(
        PictureMetadataAlreadyExistsError(picture.id)
      )
    }
  }

}
