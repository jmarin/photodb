package com.github.jmarin.photodb.backend.domain.pictures.interpreters

import org.scalatest.Matchers
import org.scalatest.PropSpec
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import java.awt.image.BufferedImage
import com.github.jmarin.backend.domain.pictures.interpreters.ImageRepositoryInMemoryInterpreter
import com.github.jmarin.photodb.backend.domain.pictures.model.ImageAlreadyExistsError
import cats.Id
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO
import com.github.jmarin.photodb.backend.domain.pictures.model.ImageArbitraries

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
