package com.dearos.clickandrace.auth.domain.appRepository

import android.content.Context
import android.net.Uri
import android.util.Log
import com.dearos.clickandrace.auth.domain.supabase.SupabaseClientProvider
import com.dearos.clickandrace.model.data.ProductData
import com.dearos.clickandrace.model.dto.ProductDTO
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repositorio encargado de gestionar las operaciones CRUD relacionadas con los productos (product)
 * en la base de datos Supabase.
 *
 * Esta clase encapsula toda la lógica de acceso a datos relacionada con productos:
 * creación, consulta, actualización, eliminación, y carga de imágenes a Supabase Storage.
 *
 * @param supabaseClient Cliente Supabase configurado.
 */
class ProductRepository(private val supabaseClient: SupabaseClient) {

    /**
     * Crea un producto en la tabla `products`.
     *
     * @param product Producto a insertar.
     * @return `true` si la operación fue exitosa, `false` si falló.
     */
    suspend fun createProduct(product: ProductDTO): Boolean {
        return try {
            supabaseClient.from("products").insert(product)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * Crea un producto y devuelve su ID generado automáticamente.
     *
     * @param product Producto a insertar.
     * @return ID del producto creado, o `null` si hubo error.
     */
    suspend fun createProductAndReturnId(product: ProductDTO): String? {
        return try {
            val result = supabaseClient.from("products")
                .insert(product) { select() }
                .decodeSingle<ProductData>()
            result.id
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Actualiza un producto existente en base a su ID.
     *
     * @param id ID del producto.
     * @param updatedProduct Datos actualizados.
     * @return `true` si la operación fue exitosa.
     */
    suspend fun updateProduct(id: String, updatedProduct: ProductDTO): Boolean {
        return try {
            supabaseClient.from("products")
                .update(updatedProduct) {
                    filter { eq("id", id) }
                }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }


    /**
     * Sube una única imagen a Supabase Storage dentro de una carpeta específica por producto.
     *
     * @param uri URI de la imagen.
     * @param context Contexto Android para acceder al contenido.
     * @param productId ID del producto.
     * @param index Índice usado para nombrar la imagen.
     * @return URL pública de la imagen, o `null` si falló.
     */
    private suspend fun uploadSingleProductImageToFolder(
        uri: Uri,
        context: Context,
        productId: String,
        index: Int
    ): String? = withContext(Dispatchers.IO) {
        try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val bytes = inputStream?.readBytes() ?: return@withContext null

            val folder = "photos/$productId"
            val fileName = "photo_$index.jpg"
            val path = "$folder/$fileName"

            SupabaseClientProvider.client.storage
                .from("products")
                .upload(path = path, data = bytes, options = { upsert = true })

            SupabaseClientProvider.client.storage
                .from("products")
                .publicUrl(path)
        } catch (e: Exception) {
            Log.e("SupabaseUpload", "Error al subir imagen", e)
            null
        }
    }

    /**
     * Sube múltiples imágenes a Supabase Storage para un producto.
     *
     * @param uris Lista de URIs de las imágenes.
     * @param context Contexto Android.
     * @param productId ID del producto.
     * @return Lista de URLs públicas de las imágenes subidas.
     */
    suspend fun uploadMultipleProductImagesToFolder(
        uris: List<Uri>,
        context: Context,
        productId: String
    ): List<String> {
        return uris.mapIndexedNotNull { index, uri ->
            uploadSingleProductImageToFolder(uri, context, productId, index)
        }
    }

    /**
     * Elimina un producto y sus imágenes asociadas desde Supabase.
     *
     * @param id ID del producto.
     * @return `true` si fue completamente eliminado correctamente.
     */
    suspend fun deleteProduct(id: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val folderPath = "photos/$id"
            val files = supabaseClient.storage.from("products").list(prefix = folderPath)

            val pathsToDelete = files.map { "$folderPath/${it.name}" }

            if (pathsToDelete.isNotEmpty()) {
                supabaseClient.storage.from("products").delete(pathsToDelete)
            }

            supabaseClient.from("products").delete {
                filter { eq("id", id) }
            }

            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * Obtiene todos los productos almacenados en la base de datos.
     *
     * @return Lista de productos, o vacía si ocurre un error.
     */
    suspend fun getAllProducts(): List<ProductData> {
        return try {
            supabaseClient.from("products").select().decodeList()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    /**
     * Obtiene un producto por su ID.
     *
     * @param id ID del producto.
     * @return Objeto [ProductData] si existe, o `null`.
     */
    suspend fun getProductById(id: String): ProductData? {
        return try {
            supabaseClient.from("products")
                .select {
                    filter { eq("id", id) }
                }
                .decodeSingleOrNull()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Obtiene todos los productos publicados por un usuario específico.
     *
     * @param userId ID del usuario.
     * @return Lista de productos.
     */
    suspend fun getProductsByUser(userId: String): List<ProductData> {
        return try {
            supabaseClient.from("products")
                .select {
                    filter { eq("user_id", userId) }
                }
                .decodeList()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}
