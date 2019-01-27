package com.jmarin.photodb.service

import java.nio.file.Paths
import java.util.UUID

import cats.{Functor, Id}
import com.jmarin.photodb.model.{Keyword, Picture, PictureMetadata}
import com.jmarin.photodb.repositories.algebras.PictureRepository
import com.jmarin.photodb.repositories.interpreters.inmemory.InMemoryPictureRepository
import org.scalatest.{Matchers, WordSpec}

import scala.collection.immutable

class PictureServiceSpec extends WordSpec with Matchers {

  // Provide a Functor[List] instance
  implicit val listFunctor: Functor[List] = new Functor[List] {
    override def map[A, B](fa: List[A])(f: A => B): List[B] = fa map f
  }

  val pictureRepository: PictureRepository[Id, List] = InMemoryPictureRepository
  val pictureService: PictureService[Id, List]       = PictureService(pictureRepository)

  val pic1 = Picture(UUID.randomUUID(),
                     Paths.get("").toAbsolutePath,
                     PictureMetadata(immutable.Seq(Keyword("travel"), Keyword("portrait"))))

  val pic2 = Picture(UUID.randomUUID(),
                     Paths.get("").toAbsolutePath,
                     PictureMetadata(immutable.Seq(Keyword("travel"))))

  "Picture Service" should {
    "add picture" in {
      pictureService.get(pic1.id) shouldEqual None
      pictureService.create(pic1) shouldEqual Right(pic1)
      pictureService.create(pic2) shouldEqual Right(pic2)
    }
    "get picture" in {
      pictureService.get(pic1.id) shouldEqual Some(pic1)
      pictureService.get(pic2.id) shouldEqual Some(pic2)
    }
    "find all pictures" in {
      val allPictures = pictureService.findAll()
      allPictures should contain(pic1)
      allPictures should contain(pic2)
      allPictures.size should equal(2)
    }
    "delete picture" in {
      pictureService.remove(pic1.id) shouldEqual Some(pic1)
      pictureService.get(pic1.id) shouldEqual None
      pictureService.remove(pic2.id) shouldEqual Some(pic2)
      pictureService.get(pic2.id) shouldEqual None
      pictureService.findAll().size should equal(0)
    }

  }

}
