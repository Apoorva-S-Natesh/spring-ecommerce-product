package ecommerce.dto.product

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

class ProductOptionRequest (
    @field:NotNull(message = "Option Name must not be blank")
    @field:Size(max = 50, message = "Name must be at most 15 characters")
    @field:Pattern(
        regexp = "^[a-zA-Z0-9 ()\\[\\]+\\-&/_]{1,100}$",
        message =
            "Name must be 1–15 characters and only include letters, digits, spaces, " +
                    "and allowed special characters:( ), [ ], +, -, &, /, _",
    )
    val name: String,
    @field:NotNull(message = "quantity must not be null")
    @field:Min(1, message = "quantity must be greater than 0")
    @field:Max(100000000, message = "quantity must be lesser than 100000000")
    val quantity: Int,
)
