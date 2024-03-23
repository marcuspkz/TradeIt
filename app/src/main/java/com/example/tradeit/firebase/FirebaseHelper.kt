import com.example.tradeit.model.Product
import com.google.firebase.database.*

object FirebaseHelper {
    private val databaseReference: DatabaseReference by lazy {
        FirebaseDatabase.getInstance().getReference("products")
    }

    fun saveProduct(product: Product) {
        val productId = databaseReference.push().key
        productId?.let {
            databaseReference.child(it).setValue(product)
        }
    }

    fun getProductByTitle(title: String, callback: (Product?) -> Unit) {
        databaseReference.orderByChild("title").equalTo(title)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (productSnapshot in snapshot.children) {
                        val product = productSnapshot.getValue(Product::class.java)
                        callback(product)
                        return
                    }
                    callback(null) //si no encuentra ningún producto
                }

                override fun onCancelled(error: DatabaseError) {
                    //manejo de errores
                }
            })
    }

    fun getAllProducts(callback: (List<Product>) -> Unit) {
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val productList = mutableListOf<Product>()
                for (productSnapshot in snapshot.children) {
                    val product = productSnapshot.getValue(Product::class.java)
                    product?.let {
                        productList.add(it)
                    }
                }
                callback(productList)
            }

            override fun onCancelled(error: DatabaseError) {
                //manejo de errores
            }
        })
    }

    fun getProductsByPriceLessThan(price: Float, callback: (List<Product>) -> Unit) {
        databaseReference.orderByChild("price").endAt(price.toDouble())
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val productList = mutableListOf<Product>()
                    for (productSnapshot in snapshot.children) {
                        val product = productSnapshot.getValue(Product::class.java)
                        product?.let {
                            productList.add(it)
                        }
                    }
                    callback(productList)
                }

                override fun onCancelled(error: DatabaseError) {
                    //manejo de errores
                }
            })
    }
}
