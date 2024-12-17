import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kasjan.R
import com.kasjan.model.Product

class ProductAdapter(private val productList: List<Product>) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    // ViewHolder dla RecyclerView
    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productName: TextView = itemView.findViewById(R.id.tv_product_name)
        val category: TextView = itemView.findViewById(R.id.tv_category)
        val quantity: TextView = itemView.findViewById(R.id.tv_quantity)
        val expiryDate: TextView = itemView.findViewById(R.id.tv_expiry_date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]
        holder.productName.text = product.name
        holder.category.text = product.category
        holder.quantity.text = "Ilość: ${product.quantity}"
        holder.expiryDate.text = "Termin przydatności: ${product.expiryDate}"
    }

    override fun getItemCount(): Int = productList.size
}
