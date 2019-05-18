package com.github.jmarin.photodb.backend.domain.pictures.service

import cats.Id
import org.scalatest.{Matchers, WordSpec}
import com.github.jmarin.photodb.backend.domain.pictures.algebras.ImageRepositoryAlgebra
import com.github.jmarin.backend.domain.pictures.interpreters.ImageRepositoryInMemoryInterpreter
import com.github.jmarin.photodb.backend.domain.pictures.algebras.ImageValidationAlgebra
import com.github.jmarin.photodb.backend.domain.pictures.interpreters.ImageValidationInterpreter
import java.awt.image.BufferedImage
import com.github.jmarin.photodb.backend.domain.pictures.model.ImageNotFoundError
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO

class ImageServiceSpec extends WordSpec with Matchers {

  val imageRepository: ImageRepositoryAlgebra[Id] = ImageRepositoryInMemoryInterpreter.apply[Id]
  val imageValidation: ImageValidationAlgebra[Id] =
    ImageValidationInterpreter.apply[Id](imageRepository)
  val imageService: ImageService[Id] = ImageService(imageRepository, imageValidation)

  val img1         = new BufferedImage(100, 100, 1)
  val path         = "/some/path"
  val outputStream = new ByteArrayOutputStream
  ImageIO.write(img1, "jpg", outputStream)
  val bytes = outputStream.toByteArray()

  "Image Service" should {
    "create image" in {
      imageService.get(path).value shouldBe Left(ImageNotFoundError)
      imageService.createImage(path, bytes).value shouldBe Right(bytes)
    }
    "retrieve image" in {
      imageService.get(path).value shouldBe Right(bytes)
    }
    "delete image" in {
      imageService.delete(path).value shouldBe Some(path)
      imageService.get(path).value shouldBe Left(ImageNotFoundError)
    }
  }

}
