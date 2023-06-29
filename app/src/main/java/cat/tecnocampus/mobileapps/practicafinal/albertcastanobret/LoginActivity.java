package cat.tecnocampus.mobileapps.practicafinal.albertcastanobret;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {
    private UserSettings userSettings;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userSettings = (UserSettings) getApplication();
        LoadPreferences();

        setContentView(R.layout.activity_login);

        SignUpSetup();
        LogInSetup();

    }

    private void LoadPreferences(){
        LanguageManager languageManager = new LanguageManager(this);

        SharedPreferences sharedPreferences = getSharedPreferences(UserSettings.PREFERENCES, MODE_PRIVATE);
        String currentLanguage = sharedPreferences.getString(UserSettings.CURRENT_LANGUAGE, UserSettings.ENGLISH);
        userSettings.setCurrentLanguage(currentLanguage);

        languageManager.updateResource(currentLanguage);
    }

    private void SignUpSetup() {
        Button signUpButton = (Button) findViewById(R.id.signUpButton);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void LogInSetup() {
        Button signUpButton = (Button) findViewById(R.id.loginInButton);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText emailEditText = (EditText) findViewById(R.id.loginTextEmail);
                EditText passwordEditText = (EditText) findViewById(R.id.loginTextPassword);

                if (!emailEditText.getText().toString().isEmpty() && !passwordEditText.getText().toString().isEmpty()) {
                    mAuth.signInWithEmailAndPassword(emailEditText.getText().toString(), passwordEditText.getText().toString())
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        emailEditText.setText("");
                                        passwordEditText.setText("");
                                        StartApplication(mAuth.getUid());

                                    } else {
                                        if(task.getException().getMessage().contains("email")){
                                            emailEditText.setError(getString(R.string.error_email));
                                        }
                                        else{
                                            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                            builder.setTitle(getString(R.string.invalid_input));
                                            builder.setMessage(task.getException().getMessage());
                                            AlertDialog dialog = builder.create();
                                            dialog.show();
                                        }
                                        passwordEditText.setText("");
                                    }
                                }
                            });
                }
                else{
                    if (emailEditText.getText().toString().isEmpty()){
                        emailEditText.setError(getString(R.string.empty_email));
                    }
                    else if(passwordEditText.getText().toString().isEmpty()){
                        passwordEditText.setError(getString(R.string.empty_password));
                    }
                }
            }
        });
    }

    private void StartApplication(String id){
        firebaseFirestore.collection("users").document(id)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        startActivity(new Intent(LoginActivity.this, UserActivity.class));
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                        builder.setTitle(e.getMessage());
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                });
    }
}