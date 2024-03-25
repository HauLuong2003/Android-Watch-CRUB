package com.example.watch_store;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.watch_store.adapters.ProductAdapter;
import com.example.watch_store.entities.Product;
import com.example.watch_store.services.ProductService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements ProductAdapter.ProductItemClickListener {
    private RecyclerView recyclerView;
    private ProductAdapter adapter;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnManager = findViewById(R.id.btnManager);
        btnManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ManageProductActivity.class);
                startActivity(i);
            }
        });

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/WebsiteWatch/rest/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ProductService productService = retrofit.create(ProductService.class);
        Call<List<Product>> call = productService.getAll();
        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful()) {
                    List<Product> productList = response.body();
                    adapter = new ProductAdapter(MainActivity.this, productList);
                    adapter.setProductItemClickListener(MainActivity.this);
                    recyclerView.setAdapter(adapter);
                }
            }
            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                System.out.println("error: " + t.getMessage());
            }
        });

    }

    @Override
    public void onDeleteProductClicked(int productId) {
        // Xử lý sự kiện click vào nút Delete
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/WebsiteWatch/rest/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ProductService productService = retrofit.create(ProductService.class);
        Call<Void> call = productService.deleteProduct(productId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Xóa sản phẩm thành công", Toast.LENGTH_SHORT).show();

                    // Xóa sản phẩm khỏi danh sách sản phẩm trong adapter
                    adapter.removeProduct(productId);

                    // Reload lại giao diện
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(MainActivity.this, "Xóa sản phẩm thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    };



    @Override
    public void onUpdateProductClicked(int productId) {
//             Chuyển người dùng đến activity để cập nhật sản phẩm
            Intent intent = new Intent(MainActivity.this, ManageProductActivity.class);
            intent.putExtra("productId", productId); // Truyền ID của sản phẩm cần chỉnh sửa
            startActivity(intent);
    }
}
