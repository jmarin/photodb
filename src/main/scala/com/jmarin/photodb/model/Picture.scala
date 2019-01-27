package com.jmarin.photodb.model

import java.nio.file.Path

case class Picture(path: Path, metadata: PictureMetadata)
