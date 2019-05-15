// package com.jmarin.photodb.backend.repositories.interpreters.inmemory

// import java.util.UUID

// import akka.NotUsed
// import akka.stream.scaladsl.Source
// import cats.Id
// import cats.syntax.option._
// import com.jmarin.photodb.backend.model.{Keyword, Picture}
// import com.jmarin.photodb.backend.repositories.algebras.PictureRepository

// class InMemoryPictureRepository extends PictureRepository[Id] {

//   var pictures = Map.empty[UUID, Picture]

//   override def create(picture: Picture): Id[Picture] = {
//     pictures = pictures + (picture.id -> picture)
//     picture
//   }

//   override def get(id: UUID): Id[Option[Picture]] = pictures.get(id)

//   override def delete(id: UUID): Id[Option[Picture]] = {
//     pictures.get(id).fold[Option[Picture]](ifEmpty = None) { existingPicture =>
//       pictures = pictures - existingPicture.id
//       existingPicture.some
//     }
//   }

//   override def findAll(): Source[Picture, NotUsed] =
//     Source.fromIterator(() => pictures.values.toIterator)

//   override def findByKeywords(keywords: Set[Keyword]): Source[Picture, NotUsed] = {
//     if (keywords.isEmpty) {
//       findAll()
//     } else {
//       findAll().filter(
//         p =>
//           p.metadata.keywords
//             .map(_.value.toLowerCase)
//             .toSet
//             .intersect(keywords.map(_.value.toLowerCase))
//             .nonEmpty)
//     }
//   }

// }

// object InMemoryPictureRepository extends InMemoryPictureRepository
