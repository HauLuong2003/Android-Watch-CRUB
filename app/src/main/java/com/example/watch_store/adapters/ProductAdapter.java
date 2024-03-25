package com.example.watch_store.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.watch_store.R;
import com.example.watch_store.entities.Product;
import com.squareup.picasso.Picasso;

import java.util.List;


public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private Context context;
    private List<Product> productList;
    private ProductItemClickListener mListener;

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    public void setProductItemClickListener(ProductItemClickListener listener) {
        this.mListener = listener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);

        holder.textViewId.setText(String.valueOf("id: "+ product.getId()));
        holder.textViewName.setText(String.valueOf("name: " + product.getName()));
        holder.textViewPrice.setText(String.valueOf("price: " + product.getPrice()));
        holder.textViewDescription.setText(String.valueOf("des: "+ product.getDescription()));
        if (product.getImage() != null && !product.getImage().isEmpty()) {
            String imageUrl = product.getImage();
            Picasso.get().load(imageUrl).into(holder.imageView);
        } else {
            System.out.println("error");
        }

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Triggers the interface method with the product ID
                mListener.onDeleteProductClicked(product.getId());
            }
        });

        holder.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Triggers the interface method with the product ID
                mListener.onUpdateProductClicked(product.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textViewId;
        TextView textViewName;
        TextView textViewPrice;
        TextView textViewDescription;
        Button btnUpdate;
        Button btnDelete;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textViewId = itemView.findViewById(R.id.textViewId);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewPrice = itemView.findViewById(R.id.textViewPrice);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            btnDelete = itemView.findViewById(R.id.buttonDeleteProduct);
            btnUpdate = itemView.findViewById(R.id.buttonUpdateProduct);
        }
    }

    public void removeProduct(int productId) {
        for (int i = 0; i < productList.size(); i++) {
            if (productList.get(i).getId() == productId) {
                productList.remove(i);
                notifyItemRemoved(i);
                break;
            }
        }
    }


    public interface ProductItemClickListener {
        void onDeleteProductClicked(int productId);
        void onUpdateProductClicked(int productId);
    }
}
