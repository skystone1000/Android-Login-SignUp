package com.example.volleylogin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.android.volley.Request.Method.POST;

public class MainActivity extends AppCompatActivity {

    private EditText nameEditText, emailEditText, passwordEditText, conPasswordEditText, phoneEditText, organisationEditText;
    private Button signupBtn;
    private ProgressBar loading;
    private static String URL_REGISTER = "http://192.168.1.105/android_register_login/register.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loading =  findViewById(R.id.loading);
        nameEditText = findViewById(R.id.name_edit_text);
        emailEditText = findViewById(R.id.email_edit_text);
        phoneEditText = findViewById(R.id.phone_edit_text);
        organisationEditText = findViewById(R.id.organisation_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        conPasswordEditText = findViewById(R.id.conpass_edit_text);
        signupBtn = findViewById(R.id.signup_button);

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUp();
            }
        });
    }

    private void SignUp() {
        loading.setVisibility(View.VISIBLE);
        signupBtn.setVisibility(View.GONE);

        final String name = this.nameEditText.getText().toString().trim();
        final String email = this.emailEditText.getText().toString().trim();
        final String phone = this.phoneEditText.getText().toString().trim();
        final String organisation = this.organisationEditText.getText().toString().trim();
        final String password = this.passwordEditText.getText().toString().trim();
        final String conPassword = this.conPasswordEditText.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if(success.equals("1")) {
                                Toast.makeText(MainActivity.this, "Register Success", Toast.LENGTH_SHORT).show();
                                loading.setVisibility(View.GONE);
                                signupBtn.setVisibility(View.VISIBLE);
                            }

                        } catch(JSONException e){
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, "Register Error!" + e.toString(), Toast.LENGTH_SHORT).show();
                            loading.setVisibility(View.GONE);
                            signupBtn.setVisibility(View.VISIBLE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Register Error!" + error.toString(), Toast.LENGTH_SHORT).show();
                        loading.setVisibility(View.GONE);
                        signupBtn.setVisibility(View.VISIBLE);
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("email", email);
                params.put("phone", phone);
                params.put("organisation", organisation);
                params.put("password", password);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

}