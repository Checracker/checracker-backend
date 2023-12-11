package org.checracker.backend.front.service

import org.checracker.backend.core.repository.board.BoardRepository
import org.checracker.backend.front.model.request.BoardRequest
import org.checracker.backend.front.model.request.toBoardEntity
import org.checracker.backend.front.model.response.BoardListResponse
import org.checracker.backend.front.model.response.toBoardListResponse
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.webjars.NotFoundException
import java.time.LocalDateTime

@Service
class BoardService(
    private val boardRepository: BoardRepository,
) {
    fun getBoardList(userId: Int, pageRequest: PageRequest): List<BoardListResponse> {
        return boardRepository.findByUserIdAndIsDeleted(userId = userId, isDeleted = false).toBoardListResponse()
    }

    fun saveBoard(boardId: Long? = null, request: BoardRequest) {
        boardRepository.save(request.toBoardEntity(id = boardId ?: 0))
    }

    fun deleteBoard(userId: Int, boardId: Long): Boolean {
        return boardRepository.findByIdAndUserIdAndIsDeleted(id = boardId, userId = userId, isDeleted = false)
            ?.let { board ->
                boardRepository.save(
                    board.apply {
                        isDeleted = true
                        deletedAt = LocalDateTime.now()
                    },
                ).id == boardId
            } ?: throw NotFoundException("보드를 찾을 수 없습니다.")
    }
}
