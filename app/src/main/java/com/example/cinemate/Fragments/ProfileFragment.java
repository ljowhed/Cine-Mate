package com.example.cinemate.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.cinemate.R;
import com.example.cinemate.activities.LoginActivity;
import com.example.cinemate.database.DBHelper;
import com.example.cinemate.utils.SessionManager;

public class ProfileFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;
    private EditText editName;
    private ImageView imageProfile;
    private Button btnChoosePhoto, btnSaveName;
    private Uri selectedImageUri;

    private SessionManager sessionManager;
    private DBHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Inisialisasi
        sessionManager = new SessionManager(requireContext());
        dbHelper = new DBHelper(requireContext());

        String currentUsername = sessionManager.getUserName();
        String currentPassword = sessionManager.getPassword();

        // View Password Change Section
        Button btnChangePassword = view.findViewById(R.id.btnChangePassword);
        LinearLayout passwordFormLayout = view.findViewById(R.id.passwordFormLayout);
        EditText etOld = view.findViewById(R.id.etOldPassword);
        EditText etNew = view.findViewById(R.id.etNewPassword);
        EditText etConfirm = view.findViewById(R.id.etConfirmPassword);
        Button btnSubmit = view.findViewById(R.id.btnSubmitPasswordChange);

        // Toggle Form Password
        btnChangePassword.setOnClickListener(v -> {
            if (passwordFormLayout.getVisibility() == View.GONE) {
                passwordFormLayout.setVisibility(View.VISIBLE);
                btnChangePassword.setText("Cancel");
            } else {
                passwordFormLayout.setVisibility(View.GONE);
                btnChangePassword.setText("Change Password");
            }
        });

        // Submit Password Change
        btnSubmit.setOnClickListener(v -> {
            String oldPass = etOld.getText().toString();
            String newPass = etNew.getText().toString();
            String confirmPass = etConfirm.getText().toString();

            if (oldPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
                Toast.makeText(getContext(), "Semua field harus diisi", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean isCorrect = dbHelper.checkUser(currentUsername, oldPass);
            if (!isCorrect) {
                Toast.makeText(getContext(), "Password lama salah!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!newPass.equals(confirmPass)) {
                Toast.makeText(getContext(), "Password baru tidak cocok", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean updated = dbHelper.updatePassword(currentUsername, newPass);
            if (updated) {
                sessionManager.saveLoginSession(currentUsername, sessionManager.getImageUri(), newPass);
                Toast.makeText(getContext(), "Password berhasil diubah", Toast.LENGTH_SHORT).show();
                passwordFormLayout.setVisibility(View.GONE);
                btnChangePassword.setText("Change Password");
            } else {
                Toast.makeText(getContext(), "Gagal mengubah password", Toast.LENGTH_SHORT).show();
            }
        });

        // View untuk nama dan gambar profil
        editName = view.findViewById(R.id.editName);
        btnSaveName = view.findViewById(R.id.btnSaveName);
        btnChoosePhoto = view.findViewById(R.id.btnChoosePhoto);
        imageProfile = view.findViewById(R.id.imageProfile);

        // Set nama dan gambar dari session
        editName.setText(currentUsername);
        String imageUri = sessionManager.getImageUri();
        if (imageUri != null && !imageUri.isEmpty()) {
            Glide.with(this).load(Uri.parse(imageUri)).into(imageProfile);
        }


        // Edit nama
        editName.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                btnSaveName.setVisibility(View.VISIBLE);
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        btnSaveName.setOnClickListener(v -> {
            String newName = editName.getText().toString().trim();
            if (!newName.isEmpty()) {
                sessionManager.setUserName(newName);
                btnSaveName.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Nama disimpan", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Nama tidak boleh kosong", Toast.LENGTH_SHORT).show();
            }
        });

        // Pilih foto
        btnChoosePhoto.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });

        // Menu
        updateMenuLabels(view);
        setMenuListener(view, R.id.menuEditName, "Edit Nama");
        setMenuListener(view, R.id.menuFavorites, "Film Favorit");
        setMenuListener(view, R.id.menuChangePassword, "Ganti Password");
        setMenuListener(view, R.id.menuPreferences, "Preferensi");

        // Logout
        view.findViewById(R.id.menuLogout).setOnClickListener(v -> {
            sessionManager.logout();
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, new HomeFragment());
            transaction.addToBackStack(null);
            transaction.commit();

        });

        return view;
    }

    private void updateMenuLabels(View view) {
        setMenuLabel(view, R.id.menuEditName);
        setMenuLabel(view, R.id.menuFavorites);
        setMenuLabel(view, R.id.menuChangePassword);
        setMenuLabel(view, R.id.menuPreferences);
        setMenuLabel(view, R.id.menuLogout);
    }

    private void setMenuListener(View parent, int menuId, String toastMsg) {
        View menu = parent.findViewById(menuId);
        menu.setOnClickListener(v ->
                Toast.makeText(getContext(), toastMsg, Toast.LENGTH_SHORT).show()
        );
    }

    private void setMenuLabel(View parent, int menuId) {
        View menuView = parent.findViewById(menuId);
        if (menuView != null && menuView.getTag() != null) {
            String label = menuView.getTag().toString();
            TextView textMenu = menuView.findViewById(R.id.textMenu);
            if (textMenu != null) {
                textMenu.setText(label);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                sessionManager.setImageUri(selectedImageUri.toString());
                Glide.with(this).load(selectedImageUri).into(imageProfile);
            }
        }
    }
}
