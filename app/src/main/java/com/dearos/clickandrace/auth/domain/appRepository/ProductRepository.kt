package com.dearos.clickandrace.auth.domain.appRepository

import com.dearos.clickandrace.data.model.Product
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProductRepository(private val supabaseClient: SupabaseClient) {

    /** 1. Create */
    suspend fun createProduct(product: Product): Result<Product> = withContext(Dispatchers.IO) {
        runCatching {
            val inserted: Map<String, Any> = supabaseClient
                .from("products")
                .insert(
                    mapOf(
                        "user_id"     to product.userId,
                        "title"       to product.title,
                        "description" to product.description,
                        "price"       to product.price,
                        "type"        to product.type,
                        "location"    to product.location,
                        "availability" to product.availability
                    )
                )
                .decodeSingle()

            inserted.toProduct()
        }
    }

    /** 2a. Read by ID */
    suspend fun getProductById(productId: String): Result<Product> = withContext(Dispatchers.IO) {
        runCatching {
            val row: Map<String, Any> = supabaseClient
                .from("products")
                .select {
                    filter { eq("id", productId) }
                }
                .decodeSingle()

            row.toProduct()
        }
    }

    /** 2b. Read all or by Location */
    suspend fun getProductsByLocation(location: String): Result<List<Product>> = withContext(Dispatchers.IO) {
        runCatching {
            val rows: List<Map<String, Any>> = supabaseClient
                .from("products")
                .select {
                    filter { eq("location", location) }
                }
                .decodeList()

            rows.map { it.toProduct() }
        }
    }

    /** 2c. Read all products */
    suspend fun getAllProducts(): Result<List<Product>> = withContext(Dispatchers.IO) {
        runCatching {
            val rows: List<Map<String, Any>> = supabaseClient
                .from("products")
                .select()  // sin filtro devuelve toda la tabla
                .decodeList()

            rows.map { it.toProduct() }
        }
    }

    /** 3. Update */
    suspend fun updateProduct(productId: String, updatedFields: Map<String, Any>): Result<Product> = withContext(Dispatchers.IO) {
        runCatching {
            val updated: Map<String, Any> = supabaseClient
                .from("products")
                .update(updatedFields) {
                    filter { eq("id", productId) }
                }
                .decodeSingle()

            updated.toProduct()
        }
    }

    /** 4. Delete */
    suspend fun deleteProduct(productId: String): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching {
            val deleted: List<Map<String, Any>> = supabaseClient
                .from("products")
                .delete {
                    filter { eq("id", productId) }
                }
                .decodeList()

            if (deleted.isEmpty()) throw Exception("Producto no encontrado o ya eliminado")
            // Si quieres ignorar el resultado, puedes quitar la comprobación y simplemente return Unit
        }.fold(
            onSuccess = { Result.success(Unit) },
            onFailure = { Result.failure(it) }
        )
    }

    /** Helper: convertir Map → Product */
    private fun Map<String, Any?>.toProduct(): Product = Product(
        id           = this["id"] as? String,
        userId       = this["user_id"] as String,
        title        = this["title"] as String,
        description  = this["description"] as String,
        price        = (this["price"] as? Number)?.toFloat() ?: 0f,
        type         = this["type"] as String,
        location     = this["location"] as String,
        availability = this["availability"] as? Map<String, Any>,
        createdAt    = this["created_at"] as? String
    )
}
