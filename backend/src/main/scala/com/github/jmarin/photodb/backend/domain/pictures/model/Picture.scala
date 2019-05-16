package com.github.jmarin.photodb.backend.domain.pictures.model

import java.util.UUID

case class Picture(id: UUID, path: String, metadata: PictureMetadata)
