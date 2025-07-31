package ecommerce.repository

import ecommerce.model.Member
import ecommerce.model.MemberRole
import ecommerce.model.Product
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.ResultSet
import java.util.Optional
import java.util.UUID

@Repository
interface MemberRepository: JpaRepository<Member, UUID> {
    fun findByEmail(email:String): Optional<Member>
    fun existsByEmail(email: String): Boolean
}
