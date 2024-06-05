package edu.cinec.mygrocerystore;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyAdapter2 extends RecyclerView.Adapter<MyAdapter2.MyViewHolder2> {

    Context context;
    ArrayList<Product> list;

    public MyAdapter2(Context context, ArrayList<Product> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        return new MyViewHolder2(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder2 holder, int position) {
        Product product = list.get(position);
        holder.productName.setText(product.getProductName());
        holder.productPrice.setText(product.getProductPrice());
        holder.productDescription.setText(product.getProductDescription());
        Picasso.get().load(product.getProductImage()).into(holder.productImage);

        holder.addTOCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

                Map<String, Object> map = new HashMap<>();
                map.put("productName", product.getProductName());
                map.put("productId", product.getProductId());
                map.put("productPrice", Double.parseDouble(product.getProductPrice()));
                map.put("productDescription", product.getProductDescription());
                map.put("productImage", product.getProductImage());
                map.put("quantity", 1);

                assert user != null;
                mDatabase.child("orders")
                        .child(user.getUid())
                        .child("items")
                        .child(product.getProductId()).setValue(map);

                mDatabase.child("orders")
                        .child(user.getUid())
                        .child("status").setValue("Not Confirmed");
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder2 extends RecyclerView.ViewHolder {
        TextView productName, productPrice, productDescription;
        ImageView productImage;

        ImageButton addTOCart;
        public MyViewHolder2(@NonNull View itemView) {
            super(itemView);

            productName = itemView.findViewById(R.id.product_name);
            productPrice = itemView.findViewById(R.id.product_price);
            productDescription = itemView.findViewById(R.id.product_description);
            productImage = itemView.findViewById(R.id.product_image);
            addTOCart = itemView.findViewById(R.id.add_to_cart);

        }
    }
}
