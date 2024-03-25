package com.example.watch_store;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.watch_store.entities.Product;
import com.example.watch_store.services.ProductService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ManageProductActivity extends AppCompatActivity {
    private EditText nameEdit;
    private EditText priceEdit;
    private EditText descriptionEdit;
    private EditText imageEdit;

    View.OnClickListener AddProduct_clicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String name = nameEdit.getText().toString();
            double price = Double.parseDouble(priceEdit.getText().toString());
            String image = imageEdit.getText().toString();
            String description = descriptionEdit.getText().toString();

            Product newProduct = new Product(name,price,description,image);

            insertProduct(newProduct);

        }
    };

    View.OnClickListener UpdateProduct_clicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int productId = getIntent().getIntExtra("productId", -1);

            if (productId != -1) {
                String name = nameEdit.getText().toString();
                double price = Double.parseDouble(priceEdit.getText().toString());
                String description = descriptionEdit.getText().toString();
                String image = imageEdit.getText().toString();
                Product updateProduct = new Product(name, price, description, image);
                updateProduct(updateProduct, productId);
            } else {
                Toast.makeText(ManageProductActivity.this, "Không có sản phẩm được chọn", Toast.LENGTH_SHORT).show();
            }
        }
    };


    View.OnClickListener BackHome_clicked = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_product);

        nameEdit = findViewById(R.id.productName);
        priceEdit = findViewById(R.id.productPrice);
        imageEdit = findViewById(R.id.productImage);
        descriptionEdit = findViewById(R.id.productDescription);

        // Nhận thông tin sản phẩm từ Intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("productId")) {
            int productId = intent.getIntExtra("productId", -1); //nếu không có id trả về -1

            // Hiển thị thông tin sản phẩm trên giao diện
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://10.0.2.2:8080/WebsiteWatch/rest/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ProductService productService = retrofit.create(ProductService.class);
            Call<Product> call = productService.getById(productId);
            call.enqueue(new Callback<Product>() {
                @Override
                public void onResponse(Call<Product> call, Response<Product> response) {
                    if (response.isSuccessful()) {
                        Product product = response.body();
                        nameEdit.setText(product.getName());
                        priceEdit.setText(String.valueOf(product.getPrice()));
                        imageEdit.setText(product.getImage());
                        descriptionEdit.setText(product.getDescription());
                    }
                }

                @Override
                public void onFailure(Call<Product> call, Throwable t) {
                    System.out.println("error: " + t.getMessage());
                }
            });
        }

        Button btnInsert = findViewById(R.id.buttonAddProduct);
        Button btnBack = findViewById(R.id.buttonBack);
        Button btnUpdate = findViewById(R.id.buttonUpdating);


        btnUpdate.setOnClickListener(UpdateProduct_clicked);
        btnInsert.setOnClickListener(AddProduct_clicked);
        btnBack.setOnClickListener(BackHome_clicked);

    }

    public void updateProduct(Product product,int id){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/WebsiteWatch/rest/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ProductService productService = retrofit.create(ProductService.class);
        Call<Void> call = productService.updateProduct(product,id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ManageProductActivity.this, "Cập nhật sản phẩm thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ManageProductActivity.this, "Cập nhật sản phẩm thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ManageProductActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void insertProduct(Product product){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/WebsiteWatch/rest/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ProductService productService = retrofit.create(ProductService.class);
        Call<Void> call = productService.insertProduct(product);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Thông báo thành công khi thêm sản phẩm
                    Toast.makeText(ManageProductActivity.this, "Thêm sản phẩm thành công", Toast.LENGTH_SHORT).show();
                } else {
                    // Thông báo thất bại khi thêm sản phẩm
                    Toast.makeText(ManageProductActivity.this, "Thêm sản phẩm thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Xử lý khi có lỗi kết nối
                Toast.makeText(ManageProductActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

