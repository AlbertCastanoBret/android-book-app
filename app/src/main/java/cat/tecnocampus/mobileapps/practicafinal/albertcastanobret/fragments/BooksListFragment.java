package cat.tecnocampus.mobileapps.practicafinal.albertcastanobret.fragments;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cat.tecnocampus.mobileapps.practicafinal.albertcastanobret.BooksListAdapter;
import cat.tecnocampus.mobileapps.practicafinal.albertcastanobret.R;
import cat.tecnocampus.mobileapps.practicafinal.albertcastanobret.domain.Book;

public class BooksListFragment extends Fragment {
    private ArrayList<Book> bookList;
    private BooksListAdapter booksListAdapter;
    RecyclerView rvListBooks;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public BooksListFragment() {
    }

    public static BooksListFragment newInstance(String param1, String param2) {
        BooksListFragment fragment = new BooksListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
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
        return inflater.inflate(R.layout.fragment_books_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bookList = new ArrayList<>();

        Bundle bundle = this.getArguments();

        if(bundle.getInt("apicall")==0) SetupApiMyBooks(bundle.getStringArrayList("bookslist"));
        else SetupApiSearch(bundle.getString("argument"), bundle.getInt("apicall"));
        SetupAdapter(view);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private void SetupApiSearch(String argument, int apiCall){
        RequestQueue queue = Volley.newRequestQueue(getContext());

        String url = "";

        switch (apiCall){
            case 1:
                url = "https://www.googleapis.com/books/v1/volumes?q=" + argument;
                break;
            case 2:
                url = "https://www.googleapis.com/books/v1/volumes?q=subject:" + argument;
                break;
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            Gson gson = new Gson();
                            JSONArray results = response.getJSONArray("items");
                            for(int i = 0; i < results.length(); i++){
                                JSONObject JSONObject = results.getJSONObject(i);
                                Book book = gson.fromJson(JSONObject.toString(), Book.class);
                                bookList.add(book);
                                booksListAdapter.notifyDataSetChanged();
                            }
                        } catch (Exception e) {
                            Log.e("TAG", "Error: ", e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        request.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(request);
    }

    private void SetupApiMyBooks(ArrayList<String> booksList){
        for(int i=0; i<booksList.size(); i++){
            RequestQueue queue = Volley.newRequestQueue(getContext());

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, "https://www.googleapis.com/books/v1/volumes/" + booksList.get(i), null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Gson gson = new Gson();
                                Book book = gson.fromJson(response.toString(), Book.class);
                                bookList.add(book);
                                booksListAdapter.notifyDataSetChanged();
                            } catch (Exception e) {
                                Log.e("TAG", "Error: ", e);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("TAG", "Error: " + error);
                        }
                    });
            request.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(request);
        }
    }

    private void SetupAdapter(View view){
        rvListBooks = (RecyclerView) view.findViewById(R.id.rvListBooks);
        rvListBooks.setLayoutManager(new LinearLayoutManager(getContext()));
        booksListAdapter = new BooksListAdapter(getContext(), bookList);
        rvListBooks.setAdapter(booksListAdapter);
        booksListAdapter.notifyDataSetChanged();
    }
}