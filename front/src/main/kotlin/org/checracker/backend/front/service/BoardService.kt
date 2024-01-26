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
    fun getBoardList(userId: Long, pageRequest: PageRequest): List<BoardListResponse> {
        return boardRepository.findByUserIdAndIsDeleted(userId = userId, isDeleted = false).toBoardListResponse()
    }

    fun saveBoard(userId: Long, boardId: Long? = null, request: BoardRequest): Long {
        // TODO : 보드 네임 중복 검사? + 유저당 보드 생성 개수 제한 약 20개?
        val board = boardRepository.save(request.toBoardEntity(id = boardId ?: 0, userId = userId))
        return board.id!!
    }

    fun deleteBoard(userId: Long, boardId: Long): Boolean {
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
