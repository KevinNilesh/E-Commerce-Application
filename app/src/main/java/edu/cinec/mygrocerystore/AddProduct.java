package edu.cinec.mygrocerystore;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class AddProduct extends AppCompatActivity {

    Uri imageUrl;
    StorageReference storageRef;
    DatabaseReference db;
    RadioGroup radioGroup;
    RadioButton radioButton;

    boolean isPicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        MaterialButton pickImgBtn = findViewById(R.id.pick_image);
        MaterialButton addProdBtn = findViewById(R.id.add_product);

        radioGroup =  findViewById(R.id.radio_group);



//        checkStoragePermissions();

        pickImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });


        addProdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText prodId = findViewById(R.id.new_prod_id);
                EditText prodName = findViewById(R.id.new_prod_name);
                EditText prodDesc = findViewById(R.id.new_prod_description);
                EditText prodPrice = findViewById(R.id.new_prod_price);


                String id = prodId.getText().toString();
                String name = prodName.getText().toString();
                String desc = prodDesc.getText().toString();
                String price = prodPrice.getText().toString();

                int radioId =  radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(radioId);

                Log.d("output", String.valueOf(id.isEmpty()));

                if (id.isEmpty() || name.isEmpty() || desc.isEmpty() || price.isEmpty() || !isPicked) {
                    Toast.makeText(getApplicationContext(),"All fields are mandatory",Toast.LENGTH_SHORT).show();
                } else {
                    storageRef = FirebaseStorage.getInstance().getReference();
                    storageRef.child(id+".png").putFile(imageUrl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            storageRef.child(id+".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    db = FirebaseDatabase.getInstance().getReference("products");
                                    Map<String, Object> values = new HashMap<>();
                                    values.put("productId", id);
                                    values.put("productDescription", desc);
                                    values.put("productName", name);
                                    values.put("productPrice", Double.parseDouble(price));
                                    values.put("productImage", uri.toString());
                                    values.put("category", radioButton.getText().toString());

                                    db.child(id).setValue(values).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(getApplicationContext(), "Product Added",Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    });
                                }
                            });
                        }
                    });
                }




            }
        });



    }

    public void checkButton(View v) {
        int radioId =  radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);
    }

    private void selectImage() {

        Intent i =  new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i, 100);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && data != null && data.getData() != null)  {
            isPicked = true;
            imageUrl = data.getData();
            ImageView prodImg = findViewById(R.id.new_prod_img);
            prodImg.setImageURI(imageUrl);
            prodImg.setVisibility(View.VISIBLE);

        } else {
            isPicked = false;
        }
    }
}