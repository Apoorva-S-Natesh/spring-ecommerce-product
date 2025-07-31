package ecommerce.service

import ecommerce.exception.auth.EmailAlreadyExistsException
import ecommerce.exception.auth.MemberNotFoundException
import ecommerce.model.Member
import ecommerce.model.MemberRole
import ecommerce.repository.MemberRepository
import io.jsonwebtoken.security.Keys.password
import jakarta.transaction.Transactional
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class MemberService(
    private val memberRepository: MemberRepository,
    private val passwordEncoder: PasswordEncoder,
) {
    fun findByEmail(email: String): Member? {
        return memberRepository.findByEmail(email).orElse(null)
    }

    fun getMemberById(id: UUID): Member {
        return memberRepository.findById(id)
            .orElseThrow { MemberNotFoundException("Member with ID $id not found") }
    }

    @Transactional
    fun createMember(
        newMember :Member,
    ): Member {
        if (memberRepository.existsByEmail(newMember.email)) {
            throw EmailAlreadyExistsException("Member with email ${newMember.email} already exists")
        }
        val hashedPassword = passwordEncoder.encode(newMember.password)
        val memberToSave =
            Member(
                id = UUID.randomUUID(),
                email = newMember.email,
                password = hashedPassword,
                role = newMember.role,
                name =  newMember.name,
            )
        return memberRepository.save(memberToSave)
    }
}
