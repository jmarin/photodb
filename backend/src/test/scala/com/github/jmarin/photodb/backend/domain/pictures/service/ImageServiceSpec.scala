package com.github.jmarin.photodb.backend.domain.pictures.service

import cats.Id
import org.scalatest.{Matchers, WordSpec}
import com.github.jmarin.photodb.backend.domain.pictures.algebras.ImageRepositoryAlgebra
import com.github.jmarin.backend.domain.pictures.interpreters.ImageRepositoryInMemoryInterpreter
import com.github.jmarin.photodb.backend.domain.pictures.algebras.ImageValidationAlgebra
import com.github.jmarin.photodb.backend.domain.pictures.interpreters.ImageValidationInterpreter
import java.awt.image.BufferedImage
import com.github.jmarin.photodb.backend.domain.pictures.model.ImageNotFoundError

class ImageServiceSpec extends WordSpec with Matchers {

  val imageRepository: ImageRepositoryAlgebra[Id] = ImageRepositoryInMemoryInterpreter.apply[Id]
  val imageValidation: ImageValidationAlgebra[Id] =
    ImageValidationInterpreter.apply[Id](imageRepository)
  val imageService: ImageService[Id] = ImageService(imageRepository, imageValidation)  

  val img1 = new BufferedImage(100, 100, 1)
  val path = "/some/path"

  "Image Service" should {
    "create image" in {
      imageService.get(path).value shouldBe Left(ImageNotFoundError)
      imageService.createImage(path, img1).value shouldBe Right(img1)
    }
    "retrieve image" in {
      imageService.get(path).value shouldBe Right(img1)
    }
    "delete image" in {
      imageService.delete(path).value shouldBe Some(path)
      imageService.get(path).value shouldBe Left(ImageNotFoundError)
    }
  }

}
