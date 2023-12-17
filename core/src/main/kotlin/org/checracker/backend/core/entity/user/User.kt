package org.checracker.backend.core.entity.user

import org.checracker.backend.core.common.BaseEntity
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

@Table(name = "users") // user 예약어라서 user 쓰면 에러남; users로 대체
@Entity
class User(
    @Id
    @GeneratedValue
    val id: Long? = null,
    val provider: String,
    val email: String,
    val password: String,
    val name: String,
    val nickname: String? = null,
    val profileImage: String,
) : BaseEntity() {
    var passwordUpdatedAt: LocalDateTime? = null
    var isDeleted: Boolean = false
    var deletedAt: LocalDateTime? = null

    fun checkPassword(password: String) {
        if (this.password != password) {
            throw IllegalArgumentException("잘못된 패스워드입니다.")
        }
    }
}
