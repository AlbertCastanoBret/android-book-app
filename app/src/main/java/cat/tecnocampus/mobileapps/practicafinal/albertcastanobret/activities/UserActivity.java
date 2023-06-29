package cat.tecnocampus.mobileapps.practicafinal.albertcastanobret.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import cat.tecnocampus.mobileapps.practicafinal.albertcastanobret.R;
import cat.tecnocampus.mobileapps.practicafinal.albertcastanobret.databinding.ActivityUserBinding;
import cat.tecnocampus.mobileapps.practicafinal.albertcastanobret.fragments.BooksListFragment;
import cat.tecnocampus.mobileapps.practicafinal.albertcastanobret.fragments.MyBooksFragment;
import cat.tecnocampus.mobileapps.practicafinal.albertcastanobret.fragments.SearchFragment;
import cat.tecnocampus.mobileapps.practicafinal.albertcastanobret.fragments.SettingsFragment;

public class UserActivity extends AppCompatActivity {
    private ActivityUserBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        binding = ActivityUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences preferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor = preferences.edit();
        String fragment = preferences.getString("fragment", "");

        if(fragment.equals("settings")){
            ChangeFragment(new SettingsFragment());
            editor.putString("fragment", "mybooks");
            editor.apply();
        }
        else{
            ChangeFragment(new MyBooksFragment());
        }

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.books:
                    ChangeFragment(new MyBooksFragment());
                    break;
                case R.id.search:
                    ChangeFragment(new SearchFragment());
                    break;
                case R.id.settings:
                    ChangeFragment(new SettingsFragment());
                    break;
            }
            return true;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_menu, menu);

        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint(getString(R.string.hint_query));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Bundle bundle = new Bundle();
                bundle.putString("argument", query);
                bundle.putInt("apicall", 1);

                Fragment booksListFragment = new BooksListFragment();
                booksListFragment.setArguments(bundle);

                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, booksListFragment)
                        .addToBackStack(null)
                        .commit();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void ChangeFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}