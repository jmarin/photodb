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

  val pic3 = Picture(UUID.randomUUID(),
                     Paths.get("").toAbsolutePath,
                     PictureMetadata(immutable.Seq(Keyword("events"))))

  "Picture Service" should {
    "create picture" in {
      pictureService.get(pic1.id) shouldEqual None
      pictureService.create(pic1) shouldEqual Right(pic1)
      pictureService.create(pic2) shouldEqual Right(pic2)
      pictureService.create(pic3) shouldEqual Right(pic3)
    }
    "return error when creating an image that already exists" in {
      pictureService.create(pic3) shouldEqual Left(PictureAlreadyExists(pic3.id))
    }
    "get picture" in {
      pictureService.get(pic1.id) shouldEqual Some(pic1)
      pictureService.get(pic2.id) shouldEqual Some(pic2)
      pictureService.get(pic3.id) shouldEqual Some(pic3)
    }
    "find all pictures" in {
      val allPictures = pictureService.findAll()
      allPictures should contain(pic1)
      allPictures should contain(pic2)
      allPictures should contain(pic3)
      allPictures.size should equal(3)
    }
    "find pictures by keyword" in {
      val travelPictures = pictureService.findByKeywords(Set(Keyword("travel")))
      travelPictures.size should equal(2)
      val portraitPictures = pictureService.findByKeywords(Set(Keyword("portrait")))
      portraitPictures.size should equal(1)
      val allPictures = pictureService.findByKeywords(Set(Keyword("travel"), Keyword("events")))
      allPictures.size should equal(3)
      val noFilter = pictureService.findByKeywords(Set.empty)
      noFilter.size should equal(3)
    }
    "delete picture" in {
      pictureService.remove(pic1.id) shouldEqual Some(pic1)
      pictureService.get(pic1.id) shouldEqual None
      pictureService.remove(pic2.id) shouldEqual Some(pic2)
      pictureService.get(pic2.id) shouldEqual None
      pictureService.remove(pic3.id) shouldEqual Some(pic3)
      pictureService.get(pic3.id) shouldEqual None
      pictureService.findAll().size should equal(0)
    }

  }

}