package ecommerce.model

import jakarta.persistence.*
import org.hibernate.query.results.Builders.fetch

@Entity
@Table(name = "product_options",
    uniqueConstraints = [ // Composite unique constraint for product_id and name
        UniqueConstraint(columnNames = ["product_id", "name"])
    ])
class ProductOption(
    @Column(name = "name", nullable = false)
    var name: String,

    @Column(name = "quantity", nullable = false)
    var quantity: Int,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    var product: Product,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
)
