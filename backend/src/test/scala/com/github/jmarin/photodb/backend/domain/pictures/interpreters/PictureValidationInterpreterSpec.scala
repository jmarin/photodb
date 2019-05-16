package com.github.jmarin.photodb.backend.domain.pictures.interpreters

import org.scalatest.Matchers
import org.scalatest.PropSpec
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import cats.Id
import cats.implicits._
import com.github.jmarin.photodb.backend.domain.pictures.model.PictureArbitraries
import com.github.jmarin.photodb.backend.domain.pictures.model.Picture
import com.github.jmarin.photodb.backend.domain.pictures.model.PictureNotFoundError
import com.github.jmarin.photodb.backend.domain.pictures.model.PictureAlreadyExistsError

class PictureValidationInterpreterSpec
    extends PropSpec
    with ScalaCheckPropertyChecks
    with Matchers
    with PictureArbitraries {

  val pictureRepository = PictureRepositoryInMemoryInterpreter.apply[Id]
  val pictureValidation = PictureValidationInterpreter.apply[Id](pictureRepository)

  property("exists: fails if image is not in repository") {
    forAll { picture: Picture =>
      pictureValidation.exists(picture.id.some).value shouldBe Left(PictureNotFoundError)
      pictureValidation.exists(None).value shouldBe Left(PictureNotFoundError)
    }
  }

  property("exists: passes if image is already in repository") {
    forAll { picture: Picture =>
      pictureRepository.create(picture) shouldEqual picture
      pictureValidation.exists(picture.id.some).value shouldBe Right(())
    }
  }

  property("doesNotExist: passes if image is not in repository") {
    forAll { picture: Picture =>
      pictureValidation.doesNotExist(picture).value shouldBe Right(())
    }
  }

  property("doesNotExist: fails if image is already in repository") {
    forAll { picture: Picture =>
      pictureRepository.create(picture) shouldEqual picture
      pictureValidation.doesNotExist(picture).value shouldBe Left(
        PictureAlreadyExistsError(picture.id)
      )
    }
  }

}
