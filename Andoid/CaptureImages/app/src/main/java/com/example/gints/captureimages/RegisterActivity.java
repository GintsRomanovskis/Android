package com.example.gints.captureimages;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import 	androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gints.captureimages.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText password;
    private EditText email;
    private Button registerBtn;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Inicialize mainigos
        password = (EditText) findViewById(R.id.passwordTextBox);
        email = (EditText) findViewById(R.id.emailTextBox);
        registerBtn = findViewById(R.id.registerBtn);

        //Change components color
        //signUp.setTextColor(getResources().getColor(R.color.link, null));
        registerBtn.setBackgroundColor(getResources().getColor(R.color.button_green, null));

        // Initialize Firebase Auth
        //  mAuth = FirebaseAuth.getInstance();
        mAuth = FirebaseAuth.getInstance(); //need firebase authentication instance

        registerBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //register user
                //String editedEmail = email.getText().toString().trim();
                mAuth.createUserWithEmailAndPassword(email.getText().toString().trim(), password.toString())
                        .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                //  Log.d(TAG, "New user registration: " + task.isSuccessful());

                                if (!task.isSuccessful()) {
                                    //   RegisterActivity.this.showToast("Authentication failed. " + task.getException());

                                    Context context = getApplicationContext();
                                    int duration = Toast.LENGTH_SHORT;

                                    Toast toast = Toast.makeText(context, "Authentication failed. " + task.getException(), duration);
                                    toast.show();
                                } else {
                                    RegisterActivity.this.startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                                    RegisterActivity.this.finish();
                                }
                            }
                        });

            }

        });
    }


}
