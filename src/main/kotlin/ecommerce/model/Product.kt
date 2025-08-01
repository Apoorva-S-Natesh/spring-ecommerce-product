package ecommerce.model

import jakarta.persistence.*
import org.springframework.http.RequestEntity.options

@Entity
@Table(name = "products")
class Product(
    @Column(name = "name", nullable = false, unique=true)
    var name: String,

    @Column(name = "price", nullable = false)
    var price: Double,

    @Column(name = "img", nullable = false)
    var img: String,

    @Column(name = "quantity", nullable = false)
    var quantity: Int,

    @OneToMany(mappedBy = "product", cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    val options: List<ProductOption> = mutableListOf(),

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    // One-to-Many relationship with ProductOption
    // cascade = [CascadeType.ALL] means operations like persist, merge, remove on Product
    // will cascade to its ProductOptions.
    // orphanRemoval = true means if a ProductOption is removed from the 'options' collection,
    // it will be deleted from the database.
    // fetch = FetchType.LAZY is generally preferred for performance for OneToMany relationships.
//    @OneToMany(mappedBy = "product", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
//    var options: MutableSet<ProductOption> = mutableSetOf() // Using Set for uniqueness of options within a product
) {
    //add a product option, ensuring uniqueness by name
//    fun addOption(option: ProductOption) {
//        if (options.any { it.name.equals(option.name, ignoreCase = true) }) {
//            throw IllegalArgumentException("Product option with name '${option.name}' already exists for this product.")
//        }
//        options.add(option)
//        option.product = this // Set the inverse side of the relationship
//    }
//
//    fun findOption(optionName: String): ProductOption? {
//        return options.find { it.name.equals(optionName, ignoreCase = true)}
//    }
//
//    @Transient // Not persisted in the database
//    fun getTotalStock(): Int {
//        return options.sumOf { it.quantity }
//    }
//
//    @Transient // Not persisted in the database
//    fun hasStock(): Boolean {
//        return getTotalStock() > 0
//    }

    fun addOption(option: ProductOption) {
        require(options.none { it.name == option.name})
        options.add(option)
    }
}

