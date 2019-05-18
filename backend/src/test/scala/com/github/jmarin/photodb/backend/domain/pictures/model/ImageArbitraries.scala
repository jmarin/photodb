package com.github.jmarin.photodb.backend.domain.pictures.model

import org.scalacheck._
import java.awt.image.BufferedImage

trait ImageArbitraries {

  implicit def path: Arbitrary[String] = Arbitrary {
    Gen.alphaStr
  }

  implicit def image: Arbitrary[BufferedImage] = Arbitrary {
    for {
      l <- Gen.choose(1, 100)
    } yield new BufferedImage(l, l, 1)
  }

}
