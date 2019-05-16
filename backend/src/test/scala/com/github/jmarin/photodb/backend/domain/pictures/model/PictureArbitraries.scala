package com.github.jmarin.photodb.backend.domain.pictures.model

import org.scalacheck._
import java.util.UUID

trait PictureArbitraries {

  implicit def keyword: Arbitrary[Keyword] = Arbitrary {
    for {
      k <- Gen.alphaStr
    } yield Keyword(k)
  }

  implicit def metadata: Arbitrary[PictureMetadata] = Arbitrary {
    for {
      ks <- Gen.nonEmptyListOf(keyword.arbitrary)
    } yield PictureMetadata(ks)
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

  implicit def picture: Arbitrary[Picture] = Arbitrary[Picture] {
    for {
      uuid     <- uuid.arbitrary
      path     <- path.arbitrary
      metadata <- metadata.arbitrary
    } yield Picture(uuid, path, metadata)
  }

}
