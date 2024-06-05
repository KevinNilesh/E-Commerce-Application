package edu.cinec.mygrocerystore;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    LinearLayout loginBtn;
    LinearLayout registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginBtn = (LinearLayout) findViewById(R.id.login_page_btn);
        registerBtn =(LinearLayout) findViewById(R.id.reg_page_btn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });
    }

    public void login(View view) {
    }


//    public void login(View view) {
//        // Intent to start a new activity for login
//        Intent intent = new Intent(this, LoginActivity.class);
//        startActivity(intent);
//    }
//
//    public void registration(View view) {
//        // Intent to start a new activity for registration
//        Intent intent = new Intent(this, RegistrationActivity.class);
//        startActivity(intent);
//    }
}
