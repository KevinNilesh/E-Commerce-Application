package edu.cinec.mygrocerystore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    RecyclerView recyclerView1, recyclerView2, recyclerView3;
    DatabaseReference database;
    MyAdapter myAdapter;
    MyAdapter2 myAdapter2;
    MyAdapter3 myAdapter3;
    ArrayList<Product> list;
    ArrayList<Product> list2;
    ArrayList<Product> list3;

    ImageButton cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        cart = findViewById(R.id.cart);
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });

        recyclerView1 = findViewById(R.id.product_list_cat1);
        recyclerView2 = findViewById(R.id.product_list_cat2);
        recyclerView3 = findViewById(R.id.product_list_cat3);
        database = FirebaseDatabase.getInstance().getReference("products");
        recyclerView1.hasFixedSize();
        recyclerView2.hasFixedSize();
        recyclerView3.hasFixedSize();
        recyclerView1.setLayoutManager(new LinearLayoutManager(this));
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));
        recyclerView3.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        list2 = new ArrayList<>();
        list3 = new ArrayList<>();

        myAdapter =  new MyAdapter(this, list);
        myAdapter2 =  new MyAdapter2(this, list2);
        myAdapter3 =  new MyAdapter3(this, list3);

        recyclerView1.setAdapter(myAdapter);
        recyclerView2.setAdapter(myAdapter2);
        recyclerView3.setAdapter(myAdapter3);

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    Product product = dataSnapshot.getValue(Product.class);
                    assert product != null;
                    Log.i("Cat", product.getCategory());
                    switch (product.getCategory()) {
                        case "clothes":
                            list.add(product);
                            break;
                        case "shoes":
                            list2.add(product);
                            break;
                        case "furniture":
                            list3.add(product);
                            break;
                    }
                }
                myAdapter.notifyDataSetChanged();
                myAdapter2.notifyDataSetChanged();
                myAdapter3.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}