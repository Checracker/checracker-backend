package org.checracker.backend.front.model.response

import org.checracker.backend.core.entity.board.Board

data class BoardListResponse(
    val id: Long,
    val title: String,
    val description: String,
    val image: String?,
)

fun List<Board>.toBoardListResponse() =
    this.map {
        BoardListResponse(
            id = it.id ?: 0,
            title = it.title,
            description = it.description ?: "",
            image = it.image,
        )
    }
