package com.example.loginapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {

    TextInputEditText nameEditText;
    TextInputEditText emailEditText;
    TextInputEditText phoneEditText;
    TextInputEditText organisationEditText;
    TextInputEditText passwordEditText;
    TextInputEditText confirmPasswordEditText;

    String Name, Email, Phone, Organisation, Password;

    // Extras Check
    FirebaseAuth mAuth;
    DatabaseReference mdatabase;
    ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final TextInputLayout nameTextInput = findViewById(R.id.name_text_input);
        final TextInputLayout emailTextInput = findViewById(R.id.email_text_input);
        final TextInputLayout phoneTextInput = findViewById(R.id.phone_text_input);
        final TextInputLayout organisationTextInput = findViewById(R.id.organisation_text_input);
        final TextInputLayout passwordTextInput = findViewById(R.id.password_text_input);
        final TextInputLayout confirmPasswordTextInput = findViewById(R.id.confirm_password_text_input);

        nameEditText = findViewById(R.id.name_edit_text);
        emailEditText = findViewById(R.id.email_edit_text);
        phoneEditText = findViewById(R.id.phone_edit_text);
        organisationEditText = findViewById(R.id.organisation_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        confirmPasswordEditText = findViewById(R.id.confirm_password_edit_text);

        MaterialButton signupButton = findViewById(R.id.signup_button);

        // for authentication using FirebaseAuth.
        mAuth = FirebaseAuth.getInstance();
        mDialog = new ProgressDialog(this);
        mdatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        // See the UserRecord reference doc for the contents of userRecord.

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int noErrors = 0;
                noErrors += check(nameTextInput);
                noErrors += check(emailTextInput);
                noErrors += check(phoneTextInput);
                noErrors += check(organisationTextInput);
                noErrors += check(passwordTextInput);
                noErrors += check(confirmPasswordTextInput);

                if (!passwordEditText.getText().toString().trim().equals(confirmPasswordEditText.getText().toString().trim())) {
                    passwordTextInput.setError("Passwords Should Match");
                    Toast.makeText(RegisterActivity.this,"Passwords Should Match", Toast.LENGTH_SHORT).show();
                    noErrors += 1;
                }

                if (!isPasswordValid(passwordEditText.getText())) {
                    passwordTextInput.setError(getString(R.string.shr_error_password));
                    noErrors += 1;
                }

                if (noErrors == 0) {
                    // All fields are valid!
                    // Toast.makeText(RegisterActivity.this,"Registered", Toast.LENGTH_SHORT).show();
                    UserRegister();
                }
            }
        });

    }

    int check(TextInputLayout layout){
        String editTextString = layout.getEditText().getText().toString();
        if (editTextString.isEmpty()) {
            layout.setError(getResources().getString(R.string.error_string));
            return 1;
        } else {
            layout.setError(null);
            return 0;
        }
    }

    // "isPasswordValid" from "Navigate to the next Fragment" section method goes here
    private boolean isPasswordValid(@Nullable Editable text) {
        return text != null && text.length() >= 8;
    }

    private void UserRegister() {
        Name = nameEditText.getText().toString().trim();
        Email = emailEditText.getText().toString().trim();
        Phone = phoneEditText.getText().toString().trim();
        Organisation = organisationEditText.getText().toString().trim();
        Password = passwordEditText.getText().toString().trim();


        mDialog.setMessage("Creating User please wait...");
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
        mAuth.createUserWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(Task<AuthResult> task) {
                if (task.isSuccessful()){
                    VerifyPhoneNumber();
                    sendEmailVerification();
                    mDialog.dismiss();
                    OnAuth(task.getResult().getUser());
                    mAuth.signOut();
                }else{
                    Toast.makeText(RegisterActivity.this,"User already Exists! Please Login!!",Toast.LENGTH_SHORT).show();
                    //checkIfEmailVerified();
                    mDialog.dismiss();
                    finish();
                    startActivity(new Intent(RegisterActivity.this,MainActivity.class));
                }
            }
        });
    }

    //Email verification code using FirebaseUser object and using isSucccessful()function.
    private void sendEmailVerification() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user!=null){
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(RegisterActivity.this,"Check your Email for verification",Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                    }
                }
            });
        }
    }

    private void OnAuth(FirebaseUser user) {
        createAnewUser(user.getUid());
    }

    private void VerifyPhoneNumber(){
        String mobileNo = phoneEditText.getText().toString().trim();
        Log.d("MobileNO",mobileNo);
        if (mobileNo.isEmpty() || mobileNo.length() < 10) {
            phoneEditText.setError("Enter a valid Phone Number");
            phoneEditText.requestFocus();
            return;
        }
        Intent intent = new Intent(RegisterActivity.this, VerifyActivity.class);
        intent.putExtra("mobile", mobileNo);
        startActivity(intent);
    }

    private void createAnewUser(String uid) {
        User user = BuildNewuser();
        mdatabase.child(uid).setValue(user);
    }

    private User BuildNewuser(){
        return new User(
                getDisplayName(),
                getUserEmail(),
                new Date().getTime()
        );
    }

    public String getDisplayName() {
        return Name;
    }

    public String getUserEmail() {
        return Email;
    }

}

