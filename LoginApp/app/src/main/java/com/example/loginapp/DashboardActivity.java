package com.example.loginapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static java.security.AccessController.getContext;

public class DashboardActivity extends AppCompatActivity {

    String EmailHolder;
    TextView Email;
    MaterialButton logoutButton ;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListner;
    FirebaseUser mUser;
    //@SuppressLint("SetTextI18n")
    public static final String TAG="LOGIN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Email = (TextView)findViewById(R.id.textView1);
         logoutButton = findViewById(R.id.logout_button);

        Intent intent = getIntent();
        // Set up the toolbar
        setUpToolbar();
        // Receiving User Email Send By MainActivity.
        EmailHolder = intent.getStringExtra(MainActivity.userEmail);


        // Setting up received email to TextView.
        //Email.setText(Email.getText().toString()+ EmailHolder);

        // Adding click listener to Log Out button.
        logoutButton.setOnClickListener(new View.OnClickListener() {
            // @Override
            public void onClick(View v) {
                //Finishing current DashBoard activity on button click.
                finish();
                Toast.makeText(DashboardActivity.this,"Log Out Successfull", Toast.LENGTH_LONG).show();
                //Intent intent=new Intent(DashboardActivity.this,login.class);
                //startActivity(intent);

                /*
                if (v.getId() == R.id.button1) {
                    AuthUI.getInstance()
                            .signOut(this)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                public void onComplete(@NonNull Task<Void> task) {
                                    // user is now signed out
                                    startActivity(new Intent(DashboardActivity.this, MainActivity.class));
                                    finish();
                                }
                            });
                }
                */

            }
        });


        // Set cut corner background for API 23+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            findViewById(R.id.product_grid).setBackgroundResource(R.drawable.shr_product_grid_background_shape);
        }
    }

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        // Scroll the Forward view down (so that we can see the bottom part) Animation 2
        toolbar.setNavigationOnClickListener(new NavigationIconClickListener(
                DashboardActivity.this,
                findViewById(R.id.product_grid),
                new AccelerateDecelerateInterpolator(),
                DashboardActivity.this.getDrawable(R.drawable.shr_branded_menu), // Menu open icon
                DashboardActivity.this.getResources().getDrawable(R.drawable.shr_close_menu))); // Menu close icon
    }

//    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.shr_toolbar_menu, menu);
//        super.onCreateOptionsMenu(menu, menuInflater);
    }

}