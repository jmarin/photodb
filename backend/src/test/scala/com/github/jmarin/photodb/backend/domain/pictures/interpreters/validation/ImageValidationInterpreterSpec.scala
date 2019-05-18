package com.github.jmarin.photodb.backend.domain.pictures.interpreters.validation

import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream

import cats.Id
import com.github.jmarin.photodb.backend.domain.pictures.interpreters.repositories.inmemory.ImageRepositoryInMemoryInterpreter
import com.github.jmarin.photodb.backend.domain.pictures.model.{ImageAlreadyExistsError, ImageArbitraries}
import javax.imageio.ImageIO
import org.scalatest.{Matchers, PropSpec}
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

class ImageValidationInterpreterSpec
    extends PropSpec
    with ScalaCheckPropertyChecks
    with Matchers
    with ImageArbitraries {

  val imageRepository = ImageRepositoryInMemoryInterpreter.apply[Id]
  val imageValidation = ImageValidationInterpreter.apply[Id](imageRepository)

  property("doesNotExist: passes if image is not in repository") {
    forAll { path: String =>
      imageValidation.doesNotExist(path).value shouldBe Right(())
    }
  }

  property("doesNotExist: fails if image is already in repository") {
    forAll { (path: String, image: BufferedImage) =>
      if (!path.isEmpty()) {
        val outputStream = new ByteArrayOutputStream
        ImageIO.write(image, "jpg", outputStream)
        val bytes = outputStream.toByteArray()
        imageRepository.create(path, bytes) shouldBe bytes
        imageValidation.doesNotExist(path).value shouldBe Left(ImageAlreadyExistsError(path))
      }
    }
  }

}
