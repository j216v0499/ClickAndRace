package com.dearos.clickandrace.ui.screens.appScreens.productsScreen

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dearos.clickandrace.auth.domain.appRepository.ProductRepository
import com.dearos.clickandrace.auth.domain.appRepository.UserRepository
import com.dearos.clickandrace.model.dto.ProductDTO
import com.dearos.clickandrace.model.data.ProductData
import com.dearos.clickandrace.model.data.UserData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel que gestiona la lógica y datos relacionados con productos y usuarios.
 *
 * @property productRepository Repositorio para operaciones relacionadas con productos.
 * @property userRepository Repositorio para operaciones relacionadas con usuarios.
 */
class ProductViewModel(
    private val productRepository: ProductRepository,
    val userRepository: UserRepository
) : ViewModel() {

    /** Lista de productos obtenidos. */
    private val _products = MutableStateFlow<List<ProductData>>(emptyList())
    val products = _products.asStateFlow()

    /** Producto seleccionado actualmente (para edición o detalle). */
    private val _selectedProduct = MutableStateFlow<ProductData?>(null)
    val selectedProduct = _selectedProduct.asStateFlow()

    /** Estado que indica si la última operación fue exitosa o no. */
    private val _operationSuccess = MutableStateFlow<Boolean?>(null)
    val operationSuccess = _operationSuccess.asStateFlow()

    /** ID del usuario actual autenticado. */
    private val _currentUserId = MutableStateFlow<String?>(null)
    val currentUserId = _currentUserId.asStateFlow()

    /** Datos del usuario seleccionado (relacionado con el producto seleccionado). */
    private val _selectedUser = MutableStateFlow<UserData?>(null)
    val selectedUser = _selectedUser.asStateFlow()

    /** Tipos de productos disponibles. */
    private val _productTypes = MutableStateFlow<List<String>>(emptyList())
    val productTypes = _productTypes.asStateFlow()

    init {
        loadCurrentUserId()
    }

    /**
     * Carga el ID del usuario actual desde el repositorio y lo guarda en [_currentUserId].
     */
    fun loadCurrentUserId() {
        viewModelScope.launch {
            val userId = userRepository.getCurrentUserId()
            _currentUserId.value = userId
        }
    }

    /**
     * Carga los productos asociados al usuario actual y actualiza [_products].
     */
    fun loadProductsForCurrentUser() {
        viewModelScope.launch {
            userRepository.getCurrentUserId()?.let { userId ->
                val result = productRepository.getProductsByUser(userId)
                _products.value = result
            }
        }
    }

    /**
     * Carga todos los productos disponibles y actualiza [_products].
     */
    fun loadAllProducts() {
        viewModelScope.launch {
            val result = productRepository.getAllProducts()
            _products.value = result
        }
    }

    /**
     * Crea un nuevo producto asociado al usuario actual.
     *
     * @param product Datos del producto a crear.
     */
    fun createProduct(product: ProductDTO) {
        viewModelScope.launch {
            val id = _currentUserId.value
            if (id == null) {
                _operationSuccess.value = false
                return@launch
            }
            val productWithUser = product.copy(user_id = id)
            _operationSuccess.value = productRepository.createProduct(productWithUser)
            loadProductsForCurrentUser()
        }
    }

    /**
     * Sube múltiples imágenes relacionadas a un producto.
     *
     * @param uris Lista de URIs de las imágenes a subir.
     * @param context Contexto necesario para la subida.
     * @param productId ID del producto asociado a las imágenes.
     * @return Lista de URLs donde se almacenaron las imágenes.
     */
    suspend fun uploadImagesForProduct(uris: List<Uri>, context: Context, productId: String): List<String> {
        return productRepository.uploadMultipleProductImagesToFolder(uris, context, productId)
    }

    /**
     * Actualiza un producto existente identificado por [id] con los datos nuevos de [product].
     *
     * @param id ID del producto a actualizar.
     * @param product Datos nuevos del producto.
     */
    fun updateProduct(id: String, product: ProductDTO) {
        viewModelScope.launch {
            _operationSuccess.value = productRepository.updateProduct(id, product)
            loadProductsForCurrentUser()
        }
    }

    /**
     * Elimina un producto identificado por [id].
     *
     * @param id ID del producto a eliminar.
     */
    fun deleteProduct(id: String) {
        viewModelScope.launch {
            _operationSuccess.value = productRepository.deleteProduct(id)
            loadProductsForCurrentUser()
        }
    }

    /**
     * Carga los datos del usuario asociado al producto seleccionado.
     */
    fun loadUserForSelectedProduct() {
        viewModelScope.launch {
            val userId = _selectedProduct.value?.user_id ?: return@launch
            _selectedUser.value = userRepository.getUserData(userId)
        }
    }

    /**
     * Carga un producto por su [id] y actualiza [_selectedProduct].
     *
     * @param id ID del producto a cargar.
     */
    fun loadProductById(id: String) {
        viewModelScope.launch {
            _selectedProduct.value = productRepository.getProductById(id)
            loadUserForSelectedProduct()
        }
    }

    /**
     * Limpia el producto seleccionado, estableciendo uno vacío.
     */
    fun clearSelectedProduct() {
        _selectedProduct.value = ProductData(
            id = "",
            user_id = "",
            title = "",
            description = "",
            price = 0.0,
            type = "",
            location = "",
            availability = emptyMap(),
        )
    }

}
