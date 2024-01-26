package org.checracker.backend.front.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.annotations.tags.Tag
import org.checracker.backend.front.model.request.BoardRequest
import org.checracker.backend.front.model.response.BoardListResponse
import org.checracker.backend.front.service.BoardService
import org.springframework.data.domain.PageRequest
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

@Tag(name = "보드 API", description = "보드 관련 API")
@RestController
@RequestMapping("v1/board")
class BoardController(
    private val boardService: BoardService,
) {

    @Operation(summary = "보드 리스트 조회", description = "보드 리스트를 조회합니다.")
    @GetMapping("")
    fun getBoardList(
        @Parameter(name = "userId", description = "임시용 userId", `in` = ParameterIn.QUERY)
        userId: Int, // TODO : 수정 필요
        @Parameter(name = "page", description = "0페이지부터 시작", `in` = ParameterIn.QUERY)
        page: Int? = 0,
        @Parameter(name = "size", description = "1페이지 당 크기", `in` = ParameterIn.QUERY)
        size: Int? = 10,
    ): List<BoardListResponse> =
        boardService.getBoardList(userId = userId, pageRequest = PageRequest.of(page ?: 0, size ?: 10))

    @Operation(summary = "보드 생성", description = "보드를 생성합니다.")
    @PostMapping("", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun createBoard(
        @RequestBody boardRequest: BoardRequest,
    ) {
        boardService.saveBoard(request = boardRequest)
    }

    @Operation(summary = "보드 수정", description = "보드를 수정합니다.")
    @PutMapping("/{id}")
    fun modifyBoard(
        @Parameter(name = "id", description = "보드 ID", `in` = ParameterIn.PATH)
        @PathVariable
        id: Long,
        @RequestBody boardRequest: BoardRequest,
    ) {
        boardService.saveBoard(boardId = id, request = boardRequest)
    }

    @Operation(summary = "보드 삭제", description = "보드를 삭제합니다.")
    @DeleteMapping("/{id}")
    fun deleteBoard(
        @Parameter(name = "id", description = "보드 ID", `in` = ParameterIn.PATH)
        @PathVariable
        id: Long,
        @Parameter(name = "userId", description = "임시용 userId", `in` = ParameterIn.QUERY)
        userId: Int, // TODO : 수정 필요
    ): Boolean =
        boardService.deleteBoard(userId = userId, boardId = id)
}
