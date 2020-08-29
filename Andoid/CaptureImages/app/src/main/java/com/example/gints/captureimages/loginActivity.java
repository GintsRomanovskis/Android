package com.example.gints.captureimages;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.gints.captureimages.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class loginActivity extends AppCompatActivity {
    private static final String TAG = "TAG";
    FirebaseAuth firebaseAuth;
    EditText usremail;
    EditText usrpassword;
    Button loginBtn;
    TextView signUp;
    ProgressBar progressBar;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseApp.initializeApp(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Hide ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        firebaseAuth = FirebaseAuth.getInstance(); //need firebase authentication instance

        //Initialize components
        usremail = findViewById(R.id.emailEditText);
        usrpassword = findViewById(R.id.passwlEditText);
        loginBtn = findViewById(R.id.registerBtn);
        signUp = findViewById(R.id.sign_up);

        //Change component colors
        signUp.setTextColor(getResources().getColor(R.color.link, null));
        loginBtn.setBackgroundColor(getResources().getColor(R.color.button_green, null));


        if (firebaseAuth.getCurrentUser() != null) {
            // User is logged in
            startActivity(new Intent(loginActivity.this, MainActivity.class));
            finish();
        }
        signUp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent openRegisterActivity = new Intent(loginActivity.this, com.example.gints.captureimages.RegisterActivity.class);
                startActivity(openRegisterActivity);


            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("StringFormatInvalid")
            @Override
            public void onClick(View v) {

                //login user
                final String useremail = usremail.getText().toString().trim();
                final String userpassword = usrpassword.getText().toString().trim();

                if (!useremail.isEmpty() && !userpassword.isEmpty()) {
                    //login user
                    firebaseAuth.signInWithEmailAndPassword(useremail, userpassword)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (!task.isSuccessful()) {

                                        try {
                                            throw task.getException();
                                        } catch (FirebaseAuthWeakPasswordException e) {
                                            usrpassword.setError(getString(R.string.error_weak_password));
                                            usrpassword.requestFocus();
                                        } catch (FirebaseAuthInvalidCredentialsException e) {
                                            usremail.setError(getString(R.string.error_invalid_email));
                                            usremail.requestFocus();
                                        } catch (FirebaseAuthUserCollisionException e) {
                                            usremail.setError(getString(R.string.error_user_exists));
                                            usremail.requestFocus();
                                        } catch (Exception e) {
                                            Log.v(TAG, e.getMessage());
                                        }
                                    } else {
                                        loginActivity.this.startActivity(new Intent(loginActivity.this, MainActivity.class));
                                        loginActivity.this.finish();
                                    }
                                }
                            });
                } else {
                    if (useremail.isEmpty()) {
                        usremail.setError(getString(R.string.error_empty_field, "Email address"));
                    } else if (userpassword.isEmpty()) {
                        usrpassword.setError(getString(R.string.error_empty_field, "Password"));
                    }
                }
            }
        });
    }
}



