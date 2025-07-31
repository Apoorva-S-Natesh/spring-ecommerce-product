package ecommerce.repository

import ecommerce.model.Product
import ecommerce.model.ProductOption
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface ProductRepository : JpaRepository<Product, Long> {
        fun existsByName(name: String): Boolean

    // Find products where any of their options' names contain a keyword (case-insensitive)
    // This uses a JOIN implicitly due to the property traversal (options.name)
    fun findByOptionsNameContainingIgnoreCase(optionName: String): List<Product>

    // Custom query to decrease a specific product option's quantity
    @Modifying
    @Query("UPDATE ProductOption po SET po.quantity = po.quantity - :quantity WHERE po.id = :optionId")
    fun decreaseProductOptionQuantity(@Param("optionId") optionId: Long, @Param("quantity") quantity: Int): Int
}

