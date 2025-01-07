import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile_project.Products.Category
import com.example.mobile_project.Products.Product
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductViewModel : ViewModel() {
    private val _product = MutableStateFlow<Product?>(null)
    val product: StateFlow<Product?> = _product

    private val _categoryName = MutableStateFlow("N/A")
    val categoryName: StateFlow<String> = _categoryName

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite

    init {
        checkIfFavorite()
    }

    fun fetchProduct(productId: String) {
        viewModelScope.launch {
            FirebaseFirestore.getInstance().collection("products").document(productId)
                .get()
                .addOnSuccessListener { document ->
                    val fetchedProduct = document.toObject(Product::class.java)
                    fetchedProduct?.id = productId // Ensure productId is set
                    _product.value = fetchedProduct
                    Log.d("ProductViewModel", "Fetched product: $fetchedProduct")
                    fetchedProduct?.category?.let { categoryId ->
                        fetchCategory(categoryId)
                    }
                    checkIfFavorite()
                }
                .addOnFailureListener {
                    Log.d("ProductViewModel", "Failed to fetch product")
                }
        }
    }

    fun addToFavorites(productId: String) {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val userFavoritesRef = FirebaseFirestore.getInstance()
                .collection("users")
                .document(user.uid)
                .collection("favorites")
                .document(productId)

            userFavoritesRef.set(mapOf("productId" to productId))
                .addOnSuccessListener {
                    Log.d("ProductViewModel", "Product added to favorites")
                    _isFavorite.value = true
                }
                .addOnFailureListener { e ->
                    Log.d("ProductViewModel", "Failed to add product to favorites", e)
                }
        } else {
            Log.d("ProductViewModel", "User not logged in")
        }
    }

    fun removeFromFavorites(productId: String) {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val userFavoritesRef = FirebaseFirestore.getInstance()
                .collection("users")
                .document(user.uid)
                .collection("favorites")
                .document(productId)

            userFavoritesRef.delete()
                .addOnSuccessListener {
                    Log.d("ProductViewModel", "Product removed from favorites")
                    _isFavorite.value = false
                }
                .addOnFailureListener { e ->
                    Log.d("ProductViewModel", "Failed to remove product from favorites", e)
                }
        } else {
            Log.d("ProductViewModel", "User not logged in")
        }
    }

    private fun checkIfFavorite() {
        val user = FirebaseAuth.getInstance().currentUser
        val productId = _product.value?.id
        if (user != null && productId != null) {
            val userFavoritesRef = FirebaseFirestore.getInstance()
                .collection("users")
                .document(user.uid)
                .collection("favorites")
                .document(productId)

            userFavoritesRef.get()
                .addOnSuccessListener { document ->
                    _isFavorite.value = document.exists()
                }
                .addOnFailureListener { e ->
                    Log.d("ProductViewModel", "Failed to check if product is favorite", e)
                }
        }
    }

    private fun fetchCategory(categoryId: String) {
        FirebaseFirestore.getInstance().collection("categories").document(categoryId)
            .get()
            .addOnSuccessListener { categoryDocument ->
                val category = categoryDocument.toObject(Category::class.java)
                _categoryName.value = category?.name ?: "N/A"
                Log.d("ProductViewModel", "Fetched category: $category")
            }
            .addOnFailureListener {
                _categoryName.value = "N/A"
                Log.d("ProductViewModel", "Failed to fetch category")
            }
    }
}