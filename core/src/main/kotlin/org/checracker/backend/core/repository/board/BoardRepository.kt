package org.checracker.backend.core.repository.board

import org.checracker.backend.core.entity.board.Board
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface BoardRepository : CrudRepository<Board, Long> {

    fun findByUserIdAndIsDeleted(userId: Int, isDeleted: Boolean): List<Board>

    fun findByIdAndUserIdAndIsDeleted(id: Long, userId: Int, isDeleted: Boolean): Board?
}
