package cat.tecnocampus.mobileapps.practicafinal.albertcastanobret;

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

import java.util.ArrayList;
import java.util.Arrays;

public class AddBookActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView title, subtitle, authorTitle;
    private ArrayList<String> optionsList = new ArrayList<>(Arrays.asList("Read", "Currently Reading", "Want to read"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_book);

        Intent intent = getIntent();
        
        if (intent != null) {
            Book book = (Book) intent.getSerializableExtra("book");
            Setup(book);
        }

    }

    private void Setup(Book book){
        imageView = findViewById(R.id.imageView2);
        title = findViewById(R.id.bookTitle2);
        subtitle = findViewById(R.id.bookSubtitle2);
        authorTitle = findViewById(R.id.authorTitle2);

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

            }
        });
    }
}