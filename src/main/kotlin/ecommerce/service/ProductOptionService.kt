package ecommerce.service

import ecommerce.dto.product.ProductOptionRequest
import ecommerce.repository.ProductOptionRepository
import ecommerce.repository.ProductRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Transactional
@Service
class ProductOptionService (
    private val productRepository: ProductRepository,
    private val optionRepository: ProductOptionRepository
) {

    fun create(productId: Long, request: ProductOptionRequest) {

    }
}
