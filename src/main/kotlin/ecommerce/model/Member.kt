package ecommerce.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "members")
class Member(
    @Column(name = "email", nullable = false, unique=true)
    var email: String,

    @Column(name = "password", nullable = false)
    var password: String,

    @Column(name = "MemberRole", nullable = false)
    var role: MemberRole = MemberRole.ROLE_USER,

    @Column(name = "name", nullable = false)
    var name: String,

    // For UUIDs as IDs, you typically generate them in the application code
    // or use a UUID generation strategy like org.hibernate.annotations.GenericGenerator
    // GenerationType.IDENTITY is usually for auto-incrementing numeric IDs.
    // Let's stick with UUID.randomUUID() for now, generated before persist.
    @Id
    val id: UUID = UUID.randomUUID(),
)

enum class MemberRole {
    ROLE_USER,
    ROLE_ADMIN,
}
