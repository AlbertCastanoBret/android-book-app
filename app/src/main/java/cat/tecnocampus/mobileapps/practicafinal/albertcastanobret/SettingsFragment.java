package cat.tecnocampus.mobileapps.practicafinal.albertcastanobret;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.google.firebase.auth.FirebaseAuth;

public class SettingsFragment extends Fragment {
    private UserSettings userSettings;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private Button languageButton, signOutButton;

    public SettingsFragment() {
        // Constructor vacío requerido
    }

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        Setup(view);
        return view;
    }

    private void Setup(View view) {
        userSettings = (UserSettings) requireActivity().getApplication();
        languageButton = view.findViewById(R.id.languageButton);
        signOutButton = view.findViewById(R.id.signOutButton);

        LanguageManager languageManager = new LanguageManager(requireContext());
        languageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String[] languages = {getString(R.string.spanish), getString(R.string.english)};
                final String[] languageCodes = {UserSettings.SPANISH, UserSettings.ENGLISH};

                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setTitle(getString(R.string.title_language))
                        .setItems(languages, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                userSettings.setCurrentLanguage(languageCodes[i]);

                                SharedPreferences.Editor editor = requireActivity()
                                        .getSharedPreferences(UserSettings.PREFERENCES, Context.MODE_PRIVATE)
                                        .edit();
                                editor.putString(UserSettings.CURRENT_LANGUAGE, userSettings.getCurrentLanguage());
                                editor.apply();

                                languageManager.updateResource(languageCodes[i]);
                                requireActivity().recreate();
                            }
                        })
                        .setCancelable(false)
                        .show();
            }
        });

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                requireActivity().onBackPressed();
            }
        });
    }
}