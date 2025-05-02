package com.example.docafit_app.fragments.profileUtils;

import com.example.docafit_app.R;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class editProfile_Act extends AppCompatActivity {

    private EditText nameEditText;
    private ImageView profileImageView;
    private Spinner genderSpinner;
    private Button saveButton;

    private FirebaseUser user;
    private DatabaseReference databaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_editprofile);

        nameEditText = findViewById(R.id.nameEditText);
        profileImageView = findViewById(R.id.profileImageView);
        genderSpinner = findViewById(R.id.genderSpinner);
        saveButton = findViewById(R.id.saveButton);

        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseRef = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());

        if (user != null) {
            nameEditText.setText(user.getDisplayName());
            if (user.getPhotoUrl() != null) {
                Glide.with(this).load(user.getPhotoUrl()).into(profileImageView);
            }
        }

        saveButton.setOnClickListener(v -> saveProfileChanges());
    }

    private void saveProfileChanges() {
        String newName = nameEditText.getText().toString().trim();
        String gender = genderSpinner.getSelectedItem().toString();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(newName)
                .build();

        user.updateProfile(profileUpdates).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                databaseRef.child("gender").setValue(gender);
                Toast.makeText(this, getString(R.string.profile_edit_suc), Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, getString(R.string.fail), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
