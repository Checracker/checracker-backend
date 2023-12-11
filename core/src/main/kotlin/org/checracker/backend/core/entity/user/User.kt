package org.checracker.backend.core.entity.user

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
    val name: String
)
