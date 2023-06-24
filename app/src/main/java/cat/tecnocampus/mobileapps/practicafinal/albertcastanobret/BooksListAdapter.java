package cat.tecnocampus.mobileapps.practicafinal.albertcastanobret;

import android.content.Context;
import android.media.Image;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;

import java.util.ArrayList;

public class BooksListAdapter extends RecyclerView.Adapter<BooksListAdapter.ViewHolder>{
    public interface OnItemClickListener {
        public void onClick(View view, int position);
    }

    OnItemClickListener listener;

    public void setClickListener(OnItemClickListener itemClickListener) {
        this.listener = itemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public ImageView imageView;
        public Button title;
        public TextView subTitle;
        public TextView authorTitle;

        OnItemClickListener onItemClickListener;
        @Override
        public void onClick(View view) {
            onItemClickListener.onClick(view, getBindingAdapterPosition());
        }
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            title = itemView.findViewById(R.id.bookTitleButton);
            subTitle = itemView.findViewById(R.id.bookSubtitle);
            authorTitle = itemView.findViewById(R.id.authorTitle);
        }
    }

    public ArrayList<Book> booksList;

    public BooksListAdapter(ArrayList<Book> booksList) {
        this.booksList = booksList;
    }

    @Override
    public BooksListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.list_books, parent, false);
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(BooksListAdapter.ViewHolder holder, int position) {
        Book book = booksList.get(position);

        Glide.with(holder.itemView)
                .load(book.getVolumeInfo().getImageLinks().getThumbnail())
                .error(R.drawable.ic_launcher_background)
                .into(holder.imageView);

        holder.title.setText(book.getVolumeInfo().getTitle());
        String subtitle = book.getVolumeInfo().getSubtitle();
        if (TextUtils.isEmpty(subtitle)) {
            holder.subTitle.setVisibility(View.GONE);
        } else {
            holder.subTitle.setText(subtitle);
            holder.subTitle.setVisibility(View.VISIBLE);
        }

        holder.authorTitle.setText("By " + String.join(", ", book.getVolumeInfo().getAuthors()));
    }

    @Override
    public int getItemCount() {
        return booksList.size();
    }

}
