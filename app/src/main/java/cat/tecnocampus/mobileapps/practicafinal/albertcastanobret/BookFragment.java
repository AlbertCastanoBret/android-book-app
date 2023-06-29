package cat.tecnocampus.mobileapps.practicafinal.albertcastanobret;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class BookFragment extends Fragment {

    private ImageView imageView, storeView, preview;
    private Button wantToReadButton;
    private TextView title, subtitle, authorTitle, description;


    public BookFragment() {
    }

    public static BookFragment newInstance(String param1, String param2) {
        BookFragment fragment = new BookFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_book, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            Book book = (Book) bundle.getSerializable("book");
            SetupBook(view, book);
        }
    }

    private void SetupBook(View view, Book book){
        imageView = view.findViewById(R.id.bookImageFragment);
        title = view.findViewById(R.id.bookTitleFragment);
        subtitle = view.findViewById(R.id.bookSubtitleFragment);
        authorTitle = view.findViewById(R.id.bookAuthorFragment);
        wantToReadButton = view.findViewById(R.id.wantToReadFragmentButton);
        storeView = view.findViewById(R.id.storeIcon);
        preview = view.findViewById(R.id.previewIcon);
        description = view.findViewById(R.id.descriptionText);

        Glide.with(getContext())
                .load(book.getVolumeInfo().getImageLinks() != null ? book.getVolumeInfo().getImageLinks().getThumbnail() : R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(imageView);

        title.setText(book.getVolumeInfo().getTitle());
        String subtitleText = book.getVolumeInfo().getSubtitle();
        subtitle.setVisibility(TextUtils.isEmpty(subtitleText) ? View.GONE : View.VISIBLE);
        subtitle.setText(subtitleText);
        authorTitle.setText(book.getVolumeInfo().getAuthors() != null ? getString(R.string.by) + " " + TextUtils.join(", ", book.getVolumeInfo().getAuthors()) : "By: Unknown");

        wantToReadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AddBookActivity.class).putExtra("book", book));
            }
        });

        storeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(book.getVolumeInfo().getInfoLink()));
                startActivity(intent);
            }
        });

        preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(book.getVolumeInfo().getPreviewLink()));
                startActivity(intent);
            }
        });

        description.setText(book.getVolumeInfo().getDescription());
    }
}