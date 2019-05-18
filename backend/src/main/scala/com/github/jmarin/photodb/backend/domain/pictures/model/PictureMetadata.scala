package com.github.jmarin.photodb.backend.domain.pictures.model

import java.util.UUID

case class PictureMetadata(id: UUID, path: String, keywords: Seq[Keyword])
