package ecommerce.repository

import ecommerce.dto.stats.TopProductStats
import ecommerce.model.CartItem
import ecommerce.model.Member
import ecommerce.model.ProductOption
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.jdbc.support.KeyHolder
import org.springframework.stereotype.Repository
import java.sql.ResultSet
import java.time.LocalDateTime
import java.util.Optional
import java.util.UUID

@Repository
interface CartItemRepository:  JpaRepository<CartItem, Long>  {

    fun findByMemberAndProductOption(
        member: Member,
        productOption: ProductOption,
    ): Optional<CartItem>

    fun findAllByMember(member: Member): List<CartItem>

    // For clearing all cart items for a member
    @Modifying
    @Query("DELETE FROM CartItem ci WHERE ci.member = :member")
    fun deleteAllByMember(@Param("member") member: Member): Int

//    fun findTop5AddedProducts(since: LocalDateTime): List<TopProductStats> {
//        val sql =
//            """
//            SELECT
//                p.id AS product_id,
//                p.name AS product_name,
//                COUNT(ci.product_id) AS times_added,
//                MAX(ci.created_at) AS most_recent_added_time
//            FROM
//                cart_items ci
//            JOIN
//                products p ON ci.product_id = p.id
//            WHERE
//                ci.created_at >= ?
//            GROUP BY
//                p.id, p.name
//            ORDER BY
//                times_added DESC, most_recent_added_time DESC
//            LIMIT 5
//            """.trimIndent()
//
//    }

    // Custom query for top 5 most added products (analytics)
    // This query needs to join CartItem with ProductOption and Product to get product details.
    // It returns an array of Any because it selects multiple columns from different entities.
    @Query("""
        SELECT 
            p.name as productName, 
            SUM(ci.quantity) as totalQuantityAdded, 
            MAX(ci.createdAt) as mostRecentAddedTime
        FROM CartItem ci
        JOIN ci.productOption po // Traverse to ProductOption
        JOIN po.product p // Traverse from ProductOption to Product
        WHERE ci.createdAt >= :since
        GROUP BY p.id, p.name
        ORDER BY totalQuantityAdded DESC, mostRecentAddedTime DESC
    """)
    fun findTopAddedProductsSince(@Param("since") since: LocalDateTime, pageable: Pageable): List<Array<Any>>

//
//    fun findRecentlyActiveMemberIds(since: LocalDateTime): List<UUID> {
//        val sql =
//            """
//            SELECT m.id
//            FROM members m
//            INNER JOIN cart_items ci ON m.id = ci.member_id
//            WHERE ci.created_at >= ?
//            GROUP BY m.id
//            ORDER BY MAX(ci.created_at) DESC
//            """.trimIndent()
//        return jdbcTemplate.query(sql, { rs, _ ->
//            UUID.fromString(rs.getString("id"))
//        }, since)
//    }
}
