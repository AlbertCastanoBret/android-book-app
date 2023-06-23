package cat.tecnocampus.mobileapps.practicafinal.albertcastanobret;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class MyBooksFragment extends Fragment {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    public MyBooksFragment() {
    }

    public static MyBooksFragment newInstance(String param1, String param2) {
        MyBooksFragment fragment = new MyBooksFragment();
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_books, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button readButton = (Button) view.findViewById(R.id.readButton);
        Button currentlyReadingButton = (Button) view.findViewById(R.id.artButton);
        Button wantToReadButton = (Button) view.findViewById(R.id.wantToReadButton);

        firebaseFirestore.collection("users").document(mAuth.getCurrentUser().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            List<String> read = (List<String>) documentSnapshot.get("read");
                            List<String> currentlyReading = (List<String>) documentSnapshot.get("currently_reading");
                            List<String> wantToRead = (List<String>) documentSnapshot.get("want_to_read");

                            if (read != null && currentlyReading != null && wantToRead != null) {
                                readButton.setText("Read: " + read.size() + " books");
                                currentlyReadingButton.setText("Currently Reading: " + currentlyReading.size() + " books");
                                wantToReadButton.setText("Want to Read: " + wantToRead.size() + " books");
                            }
                        }
                    }
                });
    }
}