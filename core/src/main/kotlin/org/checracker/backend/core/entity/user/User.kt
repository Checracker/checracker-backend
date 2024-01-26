package org.checracker.backend.core.entity.user

import org.checracker.backend.core.common.BaseEntity
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
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
) : BaseEntity(), UserDetails {
    var passwordUpdatedAt: LocalDateTime? = null
    var isDeleted: Boolean = false
    var deletedAt: LocalDateTime? = null
    override fun getAuthorities(): Collection<GrantedAuthority> {
        return listOf<GrantedAuthority>(SimpleGrantedAuthority("USER_ROLE"))
    }

    override fun getPassword(): String {
        return password
    }

    override fun getUsername(): String {
        return name
    }

    override fun isAccountNonExpired(): Boolean {
        TODO("Not yet implemented")
    }

    override fun isAccountNonLocked(): Boolean {
        TODO("Not yet implemented")
    }

    override fun isCredentialsNonExpired(): Boolean {
        TODO("Not yet implemented")
    }

    override fun isEnabled(): Boolean {
        return isDeleted
    }

    fun checkDeletedUser() {
        if (isDeleted && deletedAt != null) {
            throw AuthException("탈퇴 처리가 된 유저입니다.")
        }
    }
}
