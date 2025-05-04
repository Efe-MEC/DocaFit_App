package com.example.docafit_app.fragments.profileUtils;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.docafit_app.R;
import com.example.docafit_app.database.ProfileDatabase;
import com.example.docafit_app.database.ProfilePicture;
import com.example.docafit_app.database.ProfilePictureDao;
import com.example.docafit_app.mainPage_Act;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class editProfile_Act extends AppCompatActivity {

    private EditText nameEditText;
    private ImageView profileImageView;
    private Spinner genderSpinner;
    private Button saveButton;
    private Button selectImageButton;

    private FirebaseUser user;
    private DatabaseReference databaseRef;

    private static final int IMAGE_PICKER_REQUEST_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_editprofile);

        nameEditText = findViewById(R.id.nameEditText);
        profileImageView = findViewById(R.id.profileImageView);
        genderSpinner = findViewById(R.id.genderSpinner);
        saveButton = findViewById(R.id.saveButton);
        selectImageButton = findViewById(R.id.selectImageButton);

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Log.e("Firebase", getString(R.string.error_user_not_logged_in));
            Toast.makeText(this, getString(R.string.error_user_not_logged_in), Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        databaseRef = FirebaseDatabase.getInstance("https://docafit-app-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("Users").child(user.getUid());

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(
                this,
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.gender_options)) {

            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                tv.setTextColor(getResources().getColor(position == 0 ? android.R.color.darker_gray : android.R.color.black));
                return view;
            }
        };

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(adapter);

        nameEditText.setText(user.getDisplayName());

        loadProfileImageFromRoom();

        databaseRef.child("gender").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String gender = snapshot.getValue(String.class);
                if (gender != null) {
                    int position = adapter.getPosition(gender);
                    genderSpinner.setSelection(position >= 0 ? position : 0);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(editProfile_Act.this, getString(R.string.error_data_retrieval), Toast.LENGTH_SHORT).show();
            }
        });

        saveButton.setOnClickListener(v -> saveProfileChanges());
        selectImageButton.setOnClickListener(v -> openImagePicker());
    }

    private void loadProfileImageFromRoom() {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            ProfileDatabase db = ProfileDatabase.getInstance(editProfile_Act.this);
            ProfilePictureDao dao = db.profilePictureDao();
            ProfilePicture picture = dao.getProfilePicture();

            runOnUiThread(() -> {
                if (picture != null && picture.getImageUrl() != null) {
                    Glide.with(editProfile_Act.this).load(picture.getImageUrl()).into(profileImageView);
                }
            });
        });
    }

    private void saveProfileChanges() {
        String newName = nameEditText.getText().toString().trim();
        String gender = genderSpinner.getSelectedItem().toString();

        if (newName.isEmpty()) {
            Toast.makeText(this, getString(R.string.error_name_empty), Toast.LENGTH_SHORT).show();
            return;
        }

        if (genderSpinner.getSelectedItemPosition() == 0) {
            Toast.makeText(this, getString(R.string.error_gender_unselected), Toast.LENGTH_SHORT).show();
            return;
        }

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(newName)
                .build();

        user.updateProfile(profileUpdates).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                databaseRef.child("gender").setValue(gender).addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        Toast.makeText(this, getString(R.string.profile_edit_suc), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(this, mainPage_Act.class);
                        intent.putExtra("showFragment", "profile");
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(this, getString(R.string.fail), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(this, getString(R.string.fail), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveProfileImageToRoom(String imageUrl) {
        ProfilePicture picture = new ProfilePicture();
        picture.setImageUrl(imageUrl);

        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            ProfileDatabase db = ProfileDatabase.getInstance(this);
            db.profilePictureDao().insertProfilePicture(picture);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICKER_REQUEST_CODE && data != null) {
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                Glide.with(this).load(selectedImageUri).into(profileImageView);
                saveProfileImageToRoom(selectedImageUri.toString());
            }
        }
    }

    public void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICKER_REQUEST_CODE);
    }
}
