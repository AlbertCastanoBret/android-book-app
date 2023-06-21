package cat.tecnocampus.mobileapps.practicafinal.albertcastanobret;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        SignUpSetup();
        LogInSetup();
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
                                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                        builder.setTitle("Invalid input");
                                        builder.setMessage(task.getException().getMessage());
                                        AlertDialog dialog = builder.create();
                                        dialog.show();
                                        passwordEditText.setText("");
                                    }
                                }
                            });
                }
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setTitle("Invalid Input");
                    builder.setMessage("Some text field is empty");
                    AlertDialog dialog = builder.create();
                    dialog.show();
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
                        String role = documentSnapshot.getString("userType");
                        //if(role.equals("Creator")) startActivity(new Intent(LoginActivity.this, CreatorActivity.class).putExtra("id", id));
                        //else if(role.equals("Player")) startActivity(new Intent(LoginActivity.this, PlayerActivity.class).putExtra("id", id));
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