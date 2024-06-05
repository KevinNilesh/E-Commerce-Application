package edu.cinec.mygrocerystore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class CartActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference database;
    CartAdapter cartAdapter;
    ArrayList<CartProduct> list;
    TextView total, totalHeading;

    Button checkout;

    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        total = findViewById(R.id.total);
        totalHeading = findViewById(R.id.total_heading);
        checkout = findViewById(R.id.checkout);

        recyclerView = findViewById(R.id.cart_list);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        database = FirebaseDatabase.getInstance().getReference("orders/"+user.getUid()+"/items");
        DatabaseReference isConfirmed = FirebaseDatabase.getInstance().getReference("orders/"+user.getUid());
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        cartAdapter =  new CartAdapter(this, list);
        recyclerView.setAdapter(cartAdapter);

        isConfirmed.child("status").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            double totalPrice = 0;
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    Log.d("Order Status","Success");
                    String status = String.valueOf(task.getResult().getValue());
                    if (status.equals("Not Confirmed")) {
                        checkout.setVisibility(View.VISIBLE);
                        database.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                                    CartProduct cartProduct = dataSnapshot.getValue(CartProduct.class);
                                    assert cartProduct != null;
                                    double unitPrice = Double.parseDouble(cartProduct.getProductPrice());
                                    int quantity = Integer.parseInt(cartProduct.getQuantity());
                                    Log.d("quantity", String.valueOf(unitPrice));
                                    Log.d("quantity", String.valueOf(quantity));
                                    totalPrice += unitPrice * quantity;
                                    Log.d("total", String.valueOf(totalPrice));
                                    list.add(cartProduct);
                                }
                                cartAdapter.notifyDataSetChanged();
                                Log.d("total", String.valueOf(totalPrice));
                                totalHeading.setVisibility(View.VISIBLE);
                                total.setVisibility(View.VISIBLE);
                                total.setText(String.valueOf(totalPrice));
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        checkout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mDatabase.child("orders")
                                        .child(user.getUid())
                                        .child("status").setValue("Confirmed");
                                Toast.makeText(CartActivity.this, "Order Confirmed", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                    }
                } else {
                    Log.d("Order Status","Not confirmed");
                }
            }
        });

    }
}