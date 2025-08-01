package ecommerce.service

import ecommerce.dto.product.ProductOptionRequest
import ecommerce.model.Product
import ecommerce.model.ProductOption
import ecommerce.repository.ProductOptionRepository
import ecommerce.repository.ProductRepository
import jakarta.transaction.Transactional
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@Transactional
@SpringBootTest
class OptionServiceTest {

    @Autowired
    private lateinit var optionService: ProductOptionService

    @Autowired
    private lateinit var productRepository: ProductRepository

    @Test
    fun `option names contain spaces`() {
        //given
        val product = productRepository.save(Product("name", 10.0, "http://img.come", 20))
        val request = ProductOptionRequest("option1", 10)

        //when
        optionService.create(product.id, request)

        //then
        val actual = productRepository.selectByProduct(product)
//        assertThat(actual).hasSize(1)
    }

    fun `throws exception when name has more than 50 char `() {
        //given
        val product = productRepository.save(Product("name", 10.0, "http://img.come", 20))
        val request = ProductOptionRequest("a".repeat(51), 10)

        //when

        //then
        assertThrows<IllegalArgumentException> { optionService.create(product.id, request)}
    }
}
//
//class InMemoryOptionRepository: ProductOptionRepository {
//    private val options:MutableMap<Long, ProductOption> = mutableMapOf()
//
//    override fun save(option :ProductOption) : ProductOption{
//        options[option.id] = option
//        return option
//    }
//
//    class OptionServiceTest2 {
//        private val optionRepository: ProductOptionRepository
//        private val productRepository: ProductRepository
//        private val ProductOptionService(optionRepository, productRepository)
//    }

