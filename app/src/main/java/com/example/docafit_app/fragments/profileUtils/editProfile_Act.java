package com.example.docafit_app.fragments.profileUtils;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.docafit_app.fragments.profile_Frag;
import android.content.Intent;

import com.bumptech.glide.Glide;
import com.example.docafit_app.R;
import com.example.docafit_app.mainPage_Act;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
        if (user == null) {
            Log.e("Firebase", "Kullanıcı giriş yapmamış");
            Toast.makeText(this, "Kullanıcı giriş yapmamış", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        databaseRef = FirebaseDatabase.getInstance("https://docafit-app-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("Users").child(user.getUid());


        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(
                this, android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.gender_options)) {

            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    tv.setTextColor(getResources().getColor(android.R.color.darker_gray));
                } else {
                    tv.setTextColor(getResources().getColor(android.R.color.black));
                }
                return view;
            }
        };

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(adapter);

        nameEditText.setText(user.getDisplayName());
        if (user.getPhotoUrl() != null) {
            Glide.with(this).load(user.getPhotoUrl()).into(profileImageView);
        }

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
                Toast.makeText(editProfile_Act.this, "Veri alınamadı", Toast.LENGTH_SHORT).show();
            }
        });

        saveButton.setOnClickListener(v -> saveProfileChanges());
    }

    private void saveProfileChanges() {
        String newName = nameEditText.getText().toString().trim();
        String gender = genderSpinner.getSelectedItem().toString();

        if (newName.isEmpty()) {
            Toast.makeText(this, "İsim boş olamaz", Toast.LENGTH_SHORT).show();
            return;
        }

        if (genderSpinner.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Lütfen bir cinsiyet seçin", Toast.LENGTH_SHORT).show();
            return;
        }

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(newName)
                .build();

        user.updateProfile(profileUpdates).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                databaseRef.child("gender").setValue(gender).addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        Log.d("ProfileUpdate", "Gender başarıyla kaydedildi.");
                        Toast.makeText(this, getString(R.string.profile_edit_suc), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(editProfile_Act.this, mainPage_Act.class);
                        intent.putExtra("showFragment", "profile");
                        startActivity(intent);
                        finish();
                    } else {
                        Log.e("ProfileUpdate", "Gender kaydedilemedi: ", task1.getException());
                        Toast.makeText(this, getString(R.string.fail), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(this, getString(R.string.fail), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
