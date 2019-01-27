package com.jmarin.photodb.service

import java.nio.file.Paths
import java.util.UUID

import cats.Id
import com.jmarin.photodb.model.{Keyword, Picture, PictureMetadata}
import com.jmarin.photodb.repositories.algebras.PictureRepository
import com.jmarin.photodb.repositories.interpreters.inmemory.InMemoryPictureRepository
import org.scalatest.{Matchers, WordSpec}

import scala.collection.immutable

class PictureServiceSpec extends WordSpec with Matchers {

  val pictureRepository: PictureRepository[Id] = InMemoryPictureRepository
  val pictureService: PictureService[Id]       = PictureService(pictureRepository)

  val pic1 = Picture(UUID.randomUUID(),
                     Paths.get("").toAbsolutePath,
                     PictureMetadata(immutable.Seq(Keyword("travel"), Keyword("portrait"))))

  "Picture Service" should {
    "add picture" in {
      pictureService.get(pic1.id) shouldEqual None
      pictureService.create(pic1) shouldEqual Right(pic1)
    }
    "get picture" in {
      pictureService.get(pic1.id) shouldEqual Some(pic1)
    }
    "delete picture" in {
      pictureService.remove(pic1.id) shouldEqual Some(pic1)
      pictureService.get(pic1.id) shouldEqual None
    }

  }

}
