package com.jmarin.photodb.backend.model
import java.nio.file.Path
import java.util.UUID

case class Picture(id: UUID, path: Path, metadata: PictureMetadata)
