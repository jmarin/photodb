package com.github.jmarin.photodb.backend.domain.pictures.model

import org.scalacheck._
import java.util.UUID

trait PictureMetadataArbitraries {

  implicit def keyword: Arbitrary[Keyword] = Arbitrary {
    for {
      k <- Gen.alphaStr
    } yield Keyword(k)
  }

  implicit def uuid: Arbitrary[UUID] = Arbitrary {
    UUID.randomUUID()
  }

  implicit def path: Arbitrary[String] = Arbitrary {
    for {
      a <- Gen.alphaChar
      b <- Gen.alphaChar
      c <- Gen.alphaChar
    } yield s"/$a/$b/$c"

  }

  implicit def picture: Arbitrary[PictureMetadata] = Arbitrary[PictureMetadata] {
    for {
      uuid     <- uuid.arbitrary
      path     <- path.arbitrary
      keywords <- Gen.listOf(keyword.arbitrary)
    } yield PictureMetadata(uuid, path, keywords)
  }

}
