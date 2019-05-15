package com.github.jmarin.photodb.backend.domain.pictures
import java.nio.file.Path
import java.util.UUID

case class Picture(id: UUID, path: Path, metadata: PictureMetadata)
