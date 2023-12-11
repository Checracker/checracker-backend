package org.checracker.backend.front.model.request

import org.checracker.backend.core.entity.board.Board
import org.springframework.http.codec.multipart.FilePart

data class BoardRequest(
    val id: Long? = 0,
    val userId: Int, // TODO : 수정 필요
    val title: String,
    val description: String? = null,
    val image: FilePart? = null,
)

fun BoardRequest.toBoardEntity(id: Long) = Board(
    id = id,
    userId = userId,
    title = title,
    description = description,
    image = "", // TODO : 수정 필요
)
