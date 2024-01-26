package org.checracker.backend.front.model.request

import io.swagger.v3.oas.annotations.media.Schema
import org.checracker.backend.core.entity.board.Board

data class BoardRequest(
    @Schema(description = "보드 타이틀")
    val title: String,
    @Schema(description = "보드 상세 설명")
    val description: String? = null,
    // TODO : 보드 썸네일 - s3 와 함께 추가
//    @Schema(description = "보드 썸네일")
//    val thumbnail: FilePart? = null,
)

fun BoardRequest.toBoardEntity(id: Long, userId: Long) = Board(
    id = id,
    userId = userId,
    title = title,
    description = description,
    thumbnail = "", // TODO : 수정 필요
)
