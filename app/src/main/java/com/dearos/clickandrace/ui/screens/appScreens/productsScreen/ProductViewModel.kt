package com.dearos.clickandrace.ui.screens.appScreens.productsScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dearos.clickandrace.auth.domain.appRepository.ProductRepository
import com.dearos.clickandrace.data.model.Product
import kotlinx.coroutines.launch

class ProductViewModel(
    private val productRepository: ProductRepository
) : ViewModel() {

//    fun createProduct(product: Product, onResult: (Result<Product>) -> Unit) {
//        viewModelScope.launch {
//            val result = productRepository.createProduct(product)
//            onResult(result)
//        }
//    }
//
//    fun getProductsByLocation(location: String, onResult: (Result<List<Product>>) -> Unit) {
//        viewModelScope.launch {
//            val result = productRepository.getProductsByLocation(location)
//            onResult(result)
//        }
//    }
}