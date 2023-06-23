package cat.tecnocampus.mobileapps.practicafinal.albertcastanobret;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Setup();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void Setup() {
        Button signUpButton = (Button) findViewById(R.id.signUpRegisterButton);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText nameEditText = (EditText) findViewById(R.id.registerTextName);
                EditText emailEditText = (EditText) findViewById(R.id.registerTextEmail);
                EditText passwordEditText = (EditText) findViewById(R.id.registerTextPassword);

                if (!nameEditText.getText().toString().isEmpty() && !emailEditText.getText().toString().isEmpty() && !passwordEditText.getText().toString().isEmpty()) {
                    mAuth.createUserWithEmailAndPassword(emailEditText.getText().toString(), passwordEditText.getText().toString())
                            .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Map<String, Object> user = new HashMap<>();
                                        user.put("name", nameEditText.getText().toString());
                                        user.put("email", mAuth.getCurrentUser().getEmail());
                                        user.put("read", new ArrayList<String>());
                                        user.put("currently_reading", new ArrayList<String>());
                                        user.put("want_to_read", new ArrayList<String>());

                                        firebaseFirestore.collection("users").document(mAuth.getCurrentUser().getUid())
                                                .set(user)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                                        startActivity(intent);
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                                        builder.setTitle(e.getMessage());
                                                        AlertDialog dialog = builder.create();
                                                        dialog.show();
                                                    }
                                                });

                                    } else {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                        builder.setTitle("Invalid Input");
                                        builder.setMessage(task.getException().getMessage());
                                        AlertDialog dialog = builder.create();
                                        dialog.show();
                                    }
                                }
                            });
                }
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    builder.setTitle("Invalid Input");
                    builder.setMessage("Some text field is empty");
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });
    }
}