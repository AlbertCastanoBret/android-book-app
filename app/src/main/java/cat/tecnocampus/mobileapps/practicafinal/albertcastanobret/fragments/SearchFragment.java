package cat.tecnocampus.mobileapps.practicafinal.albertcastanobret.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import cat.tecnocampus.mobileapps.practicafinal.albertcastanobret.R;

public class SearchFragment extends Fragment {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    private ArrayList<String> categories;

    public SearchFragment() {
    }
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
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
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Setup(view);
    }

    private void Setup(View view) {
        firebaseFirestore.collection("categories").document("BTypTpqaqjRWA6i4xc2o").get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            categories = (ArrayList<String>) documentSnapshot.get("categories");
                            LinearLayout searchLayout = view.findViewById(R.id.searchLayout);
                            if (isAdded()) {
                                for (String category : categories) {
                                    if (isAdded()) {
                                        Button button = CreateButton(category);
                                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                                LinearLayout.LayoutParams.MATCH_PARENT,
                                                LinearLayout.LayoutParams.WRAP_CONTENT
                                        );
                                        layoutParams.setMargins(0, 10, 0, 10);
                                        button.setLayoutParams(layoutParams);
                                        searchLayout.addView(button);
                                    }
                                }
                                searchLayout.setPadding(40, 0, 40, 0);
                            }
                        }
                    }
                });
    }

    private Button CreateButton(String category){
        Button button = new Button(requireContext());
        button.setHeight(80);
        button.setText(category);
        button.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
        button.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.custom_beige));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("argument", category);
                bundle.putInt("apicall", 2);

                Fragment booksListFragment = new BooksListFragment();
                booksListFragment.setArguments(bundle);

                FragmentManager fragmentManager = getParentFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, booksListFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
        return button;
    }
}