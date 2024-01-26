package org.checracker.backend.core.repository.board

import org.checracker.backend.core.entity.board.Board
import org.springframework.data.repository.CrudRepository

interface BoardRepository : CrudRepository<Board, Long> {

    fun findByUserIdAndIsDeleted(userId: Long, isDeleted: Boolean): List<Board>

    fun findByIdAndUserIdAndIsDeleted(id: Long, userId: Long, isDeleted: Boolean): Board?
}
