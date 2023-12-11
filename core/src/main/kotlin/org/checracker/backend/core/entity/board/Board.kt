package org.checracker.backend.core.entity.board

import org.checracker.backend.core.common.BaseEntity
import java.time.LocalDateTime
import javax.persistence.GeneratedValue
import javax.persistence.Id

data class Board(
    @Id
    @GeneratedValue
    val id: Long? = null,
    val userId: Int,
    val title: String,
    val description: String? = null,
    val image: String? = null,
    val imageOpacity: Int? = null
) : BaseEntity() {
    var isDeleted: Boolean = false
    var deletedAt: LocalDateTime? = null
}
