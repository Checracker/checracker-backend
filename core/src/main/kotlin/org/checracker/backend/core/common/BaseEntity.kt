package org.checracker.backend.core.common

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.EntityListeners
import javax.persistence.MappedSuperclass

@MappedSuperclass // BaseEntity를 상속한 엔티티들은 BaseEntity에 있는 멤버변수들을 모두 컬럼으로 인식하도록 함.
@EntityListeners(value = [AuditingEntityListener::class]) // 자동으로 값을 매핑시키겠다는 의미
abstract class BaseEntity {
    @CreatedDate
    @Column(nullable = false, updatable = false)
    open var createdAt: LocalDateTime = LocalDateTime.now()

    @LastModifiedDate
    @Column(nullable = false)
    open var updatedAt: LocalDateTime = LocalDateTime.now()
}