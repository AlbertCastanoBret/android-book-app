package cat.tecnocampus.mobileapps.practicafinal.albertcastanobret;

import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;

public class BooksListAdapter extends RecyclerView.Adapter<BooksListAdapter.ViewHolder> {
    public interface OnItemClickListener {
        void onClick(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView imageView;
        public Button title;
        public TextView subTitle;
        public TextView authorTitle;

        OnItemClickListener onItemClickListener;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            title = itemView.findViewById(R.id.bookTitleButton);
            subTitle = itemView.findViewById(R.id.bookSubtitle);
            authorTitle = itemView.findViewById(R.id.authorTitle);

            // Configurar el OnClickListener para el botón
            title.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onItemClickListener.onClick(view, getBindingAdapterPosition());
        }
    }

    private ArrayList<Book> booksList;
    private Context context;

    public BooksListAdapter(Context context, ArrayList<Book> booksList) {
        this.context = context;
        this.booksList = booksList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.list_books, parent, false);
        ViewHolder viewHolder = new ViewHolder(contactView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Book book = booksList.get(position);

        Glide.with(context)
                .load(book.getVolumeInfo().getImageLinks() != null ? book.getVolumeInfo().getImageLinks().getThumbnail() : R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(holder.imageView);

        holder.title.setText(book.getVolumeInfo().getTitle());
        String subtitle = book.getVolumeInfo().getSubtitle();
        holder.subTitle.setVisibility(TextUtils.isEmpty(subtitle) ? View.GONE : View.VISIBLE);
        holder.subTitle.setText(subtitle);
        holder.authorTitle.setText(book.getVolumeInfo().getAuthors() != null ? "By: " + TextUtils.join(", ", book.getVolumeInfo().getAuthors()) : "By: Unknown");

        holder.onItemClickListener = new OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                BookFragment bookFragment = new BookFragment();

                FragmentTransaction transaction = ((UserActivity) context).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, bookFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        };
    }

    @Override
    public int getItemCount() {
        return booksList.size();
    }
}