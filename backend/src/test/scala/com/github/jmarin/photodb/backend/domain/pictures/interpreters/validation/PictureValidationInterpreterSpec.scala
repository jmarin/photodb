package com.github.jmarin.photodb.backend.domain.pictures.interpreters.validation

import cats.Id
import cats.implicits._
import com.github.jmarin.photodb.backend.domain.pictures.interpreters.repositories.inmemory.PictureRepositoryInMemoryInterpreter
import com.github.jmarin.photodb.backend.domain.pictures.model.{Picture, PictureAlreadyExistsError, PictureArbitraries, PictureNotFoundError}
import org.scalatest.{Matchers, PropSpec}
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

class PictureValidationInterpreterSpec
    extends PropSpec
    with ScalaCheckPropertyChecks
    with Matchers
    with PictureArbitraries {

  val pictureRepository = PictureRepositoryInMemoryInterpreter.apply[Id]
  val pictureValidation = PictureValidationInterpreter.apply[Id](pictureRepository)

  property("exists: fails if picture is not in repository") {
    forAll { picture: Picture =>
      pictureValidation.exists(picture.id.some).value shouldBe Left(PictureNotFoundError)
      pictureValidation.exists(None).value shouldBe Left(PictureNotFoundError)
    }
  }

  property("exists: passes if picture is already in repository") {
    forAll { picture: Picture =>
      pictureRepository.create(picture) shouldEqual picture
      pictureValidation.exists(picture.id.some).value shouldBe Right(())
    }
  }

  property("doesNotExist: passes if picture is not in repository") {
    forAll { picture: Picture =>
      pictureValidation.doesNotExist(picture).value shouldBe Right(())
    }
  }

  property("doesNotExist: fails if picture is already in repository") {
    forAll { picture: Picture =>
      pictureRepository.create(picture) shouldEqual picture
      pictureValidation.doesNotExist(picture).value shouldBe Left(
        PictureAlreadyExistsError(picture.id)
      )
    }
  }

}
