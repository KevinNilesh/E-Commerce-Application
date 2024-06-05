package edu.cinec.mygrocerystore;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    Context context;
    ArrayList<CartProduct> list;

    public CartAdapter(Context context, ArrayList<CartProduct> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.cart_item, parent, false);
        return new CartViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, @SuppressLint("RecyclerView") int position) {
        CartProduct cartProduct = list.get(position);
        holder.productName.setText(cartProduct.getProductName());
        holder.productPrice.setText(cartProduct.getProductPrice());
        holder.productDescription.setText(cartProduct.getProductDescription());
        holder.quantity.setText(cartProduct.getQuantity());
        Picasso.get().load(cartProduct.getProductImage()).into(holder.productImage);

        holder.increase.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
               assert user != null;
               FirebaseDatabase.getInstance().getReference().child("orders")
                   .child(user.getUid())
                   .child("items")
                   .child(cartProduct.getProductId()).child("quantity").setValue(Integer.parseInt(cartProduct.getQuantity()) + 1)
                   .addOnSuccessListener(new OnSuccessListener<Void>() {
                       @Override
                       public void onSuccess(Void aVoid) {
                           ((Activity) holder.productName.getContext()).recreate();
                           Toast.makeText(holder.productName.getContext(), "Item updated", Toast.LENGTH_SHORT).show();
                       }
                   });

           }
        });

        holder.decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.parseInt(cartProduct.getQuantity()) > 1) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    assert user != null;
                    FirebaseDatabase.getInstance().getReference().child("orders")
                            .child(user.getUid())
                            .child("items")
                            .child(cartProduct.getProductId()).child("quantity").setValue(Integer.parseInt(cartProduct.getQuantity()) - 1)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    ((Activity) holder.productName.getContext()).recreate();
                                    Toast.makeText(holder.productName.getContext(), "Item updated", Toast.LENGTH_SHORT).show();
                                }
                            });
                }

            }
        });

        holder.deleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.productName.getContext());
                builder.setTitle("Are you sure?");
                builder.setMessage("Deleted Order can't be undo");

                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        assert user != null;
                        FirebaseDatabase.getInstance().getReference().child("orders")
                                .child(user.getUid())
                                .child("items")
                                .child(cartProduct.getProductId()).removeValue()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        // Remove the item from the data set
                                        list.remove(position);
                                        // Notify the adapter of the item removal
                                        notifyItemRemoved(position);
                                        ((Activity) holder.productName.getContext()).recreate();
                                        Toast.makeText(holder.productName.getContext(), "Item deleted", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(holder.productName.getContext(), "Cancelled", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productPrice, productDescription, quantity, total;
        ImageView productImage;

        ImageButton deleteItem;

        Button increase, decrease, checkout;
        public CartViewHolder(@NonNull View itemView) {
            super(itemView);

            productName = itemView.findViewById(R.id.product_name);
            productPrice = itemView.findViewById(R.id.product_price);
            productDescription = itemView.findViewById(R.id.product_description);
            productImage = itemView.findViewById(R.id.product_image);
            quantity = itemView.findViewById(R.id.quantity);
            deleteItem = itemView.findViewById(R.id.delete_item);
            increase = itemView.findViewById(R.id.increase);
            decrease = itemView.findViewById(R.id.decrease);
        }
    }


}
