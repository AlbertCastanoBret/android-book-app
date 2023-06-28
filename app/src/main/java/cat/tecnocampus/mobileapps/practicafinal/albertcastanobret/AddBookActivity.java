package cat.tecnocampus.mobileapps.practicafinal.albertcastanobret;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AddBookActivity extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private int indexOption;
    private ImageView leftIcon, imageView, rightIcon;
    private TextView title, subtitle, authorTitle;
    private ArrayList<String> optionsList = new ArrayList<>(Arrays.asList("Read", "Currently Reading", "Want to read"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_book);

        indexOption = -1;

        Intent intent = getIntent();

        if (intent != null) {
            Book book = (Book) intent.getSerializableExtra("book");
            Setup(book);
        }

    }

    private void Setup(Book book){
        leftIcon = findViewById(R.id.leftIcon);
        imageView = findViewById(R.id.imageView2);
        rightIcon = findViewById(R.id.rightIcon);
        title = findViewById(R.id.bookTitle2);
        subtitle = findViewById(R.id.bookSubtitle2);
        authorTitle = findViewById(R.id.authorTitle2);

        leftIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        rightIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(indexOption != -1){
                    String key = "";
                    switch (indexOption){
                        case 0:
                            key = "read";
                            break;
                        case 1:
                            key = "currently_reading";
                            break;
                        case  2:
                            key = "want_to_read";
                            break;
                    }

                    Map<String, Object> updates = new HashMap<>();
                    updates.put(key, FieldValue.arrayUnion(book.getId()));

                    ArrayList<String> otherKeys = new ArrayList<>(Arrays.asList("read", "currently_reading", "want_to_read"));
                    otherKeys.remove(key);

                    for (String otherKey : otherKeys) {
                        updates.put(otherKey, FieldValue.arrayRemove(book.getId()));
                    }

                    firebaseFirestore.collection("users").document(mAuth.getCurrentUser().getUid())
                            .update(updates)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    onBackPressed();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                }
                            });
                }
            }
        });

        Glide.with(getApplicationContext())
                .load(book.getVolumeInfo().getImageLinks() != null ? book.getVolumeInfo().getImageLinks().getThumbnail() : R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(imageView);

        title.setText(book.getVolumeInfo().getTitle());
        String subtitleText = book.getVolumeInfo().getSubtitle();
        subtitle.setVisibility(TextUtils.isEmpty(subtitleText) ? View.GONE : View.VISIBLE);
        subtitle.setText(subtitleText);
        authorTitle.setText(book.getVolumeInfo().getAuthors() != null ? "By: " + TextUtils.join(", ", book.getVolumeInfo().getAuthors()) : "By: Unknown");


        ListView listView = findViewById(R.id.optionsList);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(AddBookActivity.this, android.R.layout.simple_list_item_single_choice, optionsList);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                indexOption = i;
            }
        });
    }
}