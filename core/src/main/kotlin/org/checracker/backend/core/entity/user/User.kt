package org.checracker.backend.core.entity.user

import org.checracker.backend.core.common.BaseEntity
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table
import javax.security.auth.message.AuthException

@Table(name = "`user`") // user 예약어라서 `user`
@Entity
class User(
    @Id
    @GeneratedValue
    val id: Long? = null,
    val provider: String,
    val email: String,
    private val password: String,
    val name: String,
    val nickname: String? = null,
    val profileImage: String,
) : BaseEntity() {
    var passwordUpdatedAt: LocalDateTime? = null
    var isDeleted: Boolean = false
    var deletedAt: LocalDateTime? = null

    fun getPassword() = password

    fun checkDeletedUser() {
        if (isDeleted && deletedAt != null) {
            throw AuthException("탈퇴 처리가 된 유저입니다.")
        }
    }
}
