package ecommerce.service

import ecommerce.dto.product.ProductOptionRequest
import ecommerce.exception.product.DuplicateProductNameException
import ecommerce.exception.product.InsufficientStockException
import ecommerce.exception.product.ProductNotFoundException
import ecommerce.exception.product.ProductOptionNotFoundException
import ecommerce.model.Product
import ecommerce.model.ProductOption
import ecommerce.repository.ProductRepository
import jakarta.transaction.Transactional
import org.hibernate.query.Page.page
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.AbstractPersistable_.id
import org.springframework.stereotype.Service

@Service
class ProductService(private val productRepository: ProductRepository) {
    fun getAllProducts(page: Int, size: Int, sortBy: String): Page<Product> {
        val pageable = PageRequest.of(page, size, Sort.by(sortBy))
        return productRepository.findAll(pageable)
    }
    fun getProductById(id: Long): Product =
        productRepository.findById(id).orElseThrow { ProductNotFoundException("Product with ID $id not found") }

    @Transactional //Required for write operations
    fun createProduct(newProduct: Product): Product {
        if (productRepository.existsByName(newProduct.name)) {
            throw DuplicateProductNameException("Product name ${newProduct.name} already exists.")
        }
        return productRepository.save(newProduct)
    }

    @Transactional
    fun updateProduct(
        id: Long,
        updatedProduct: Product,
    ): Product {
        val existingProduct = productRepository.findById(id)
            .orElseThrow { ProductNotFoundException("Product with ID $id not found") }

        // Update fields individually to avoid overwriting relationships carelessly
        // Or, if updatedProduct is a complete representation, you can copy properties.
        // Be careful with updating collections like 'options' directly if you want to
        // merge changes rather than replace the entire collection.

        // Example: Update scalar fields
        if (existingProduct.name != updatedProduct.name) {
            if (productRepository.existsByName(updatedProduct.name)) {
                throw DuplicateProductNameException("Product name ${updatedProduct.name} already exists.")
            }
            existingProduct.name = updatedProduct.name
        }
        existingProduct.price = updatedProduct.price
        existingProduct.img = updatedProduct.img

        // Handle Product Options update (more complex, requires careful merging)
        // A common strategy is to remove options not present in updatedProduct and add new ones.
        // You might need a more sophisticated DTO for updating products with options.
        // For simplicity here, let's assume 'updatedProduct.options' contains the desired
        // final set of options. You'd need logic to compare and update, or remove/add.

        // If you send the whole Product with its options, JPA's cascade and orphanRemoval can help:
        // Clear existing options and re-add from the updated product.
        // This implicitly handles removals (orphans) and additions.
        existingProduct.options.clear() // Remove all existing options from the collection
        updatedProduct.options.forEach { option ->
            val newOption = ProductOption(option.name, option.quantity, existingProduct)
            existingProduct.addOption(newOption) // Use the addOption helper
        }
        return productRepository.save(existingProduct) // save() persists the changes
    }

    @Transactional
    fun deleteProduct(id: Long) {
        if(!productRepository.existsById(id)) {
            throw (ProductNotFoundException("Product with ID $id not found"))
        }
        productRepository.deleteById(id)
    }

    @Transactional
    fun decreaseProductOptionStock(productId: Long, optionName: String, quantityToDecrease: Int): Product {
        val product = productRepository.findById(productId).orElseThrow { ProductNotFoundException("Product with ID $productId not found") }
        val option = product.findOption(optionName) ?: throw ProductOptionNotFoundException("Product option '$optionName' not found for product ID $productId")
        if (option.quantity < quantityToDecrease) {
            throw InsufficientStockException("\"Insufficient stock for option '${option.name}'. Available: ${option.quantity}, Requested: $quantityToDecrease")
        }
        option.quantity -= quantityToDecrease // Update the entity directly
        return productRepository.save(product) // Persist the change to the option via the product
    }

    @Transactional
    fun addProductOption(productId: Long, optionRequest: ProductOptionRequest): Product {
        val product = productRepository.findById(productId)
            .orElseThrow { ProductNotFoundException("Product with ID $productId not found") }

        val newOption = ProductOption(
            name = optionRequest.name,
            quantity = optionRequest.quantity,
            product = product // Link to the parent product
        )
        product.addOption(newOption) // Use the helper in Product entity
        return productRepository.save(product) // Save the product to persist the new option
    }
}
