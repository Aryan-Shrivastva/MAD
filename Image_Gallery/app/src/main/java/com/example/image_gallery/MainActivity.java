package com.example.image_gallery;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.GridView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.documentfile.provider.DocumentFile;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    GridView gridView;
    ArrayList<Uri> imageUris = new ArrayList<>();
    ImageUriAdapter imageAdapter;

    Uri selectedSaveFolderUri = null;
    Uri selectedLoadFolderUri = null;

    ActivityResultLauncher<Intent> selectSaveFolderLauncher;
    ActivityResultLauncher<Intent> selectLoadFolderLauncher;
    ActivityResultLauncher<Intent> selectImagesLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridView = findViewById(R.id.gridView);

        selectSaveFolderLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        selectedSaveFolderUri = result.getData().getData();
                        getContentResolver().takePersistableUriPermission(
                                selectedSaveFolderUri,
                                Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                        );
                        takePhoto();
                    }
                });

        selectLoadFolderLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        selectedLoadFolderUri = result.getData().getData();
                        getContentResolver().takePersistableUriPermission(
                                selectedLoadFolderUri,
                                Intent.FLAG_GRANT_READ_URI_PERMISSION
                        );
                        loadImagesFromFolder();
                    }
                });

        selectImagesLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        imageUris.clear();

                        if (result.getData().getClipData() != null) {
                            int count = result.getData().getClipData().getItemCount();
                            for (int i = 0; i < count; i++) {
                                Uri imageUri = result.getData().getClipData().getItemAt(i).getUri();
                                imageUris.add(imageUri);
                            }
                        } else if (result.getData().getData() != null) {
                            Uri imageUri = result.getData().getData();
                            imageUris.add(imageUri);
                        }

                        imageAdapter = new ImageUriAdapter(this, imageUris);
                        gridView.setAdapter(imageAdapter);
                    }
                });

        findViewById(R.id.TakePhoto).setOnClickListener(v -> openFolderPicker(true));
        findViewById(R.id.LoadImages).setOnClickListener(v -> openFolderPicker(false));
        findViewById(R.id.selectImages).setOnClickListener(v -> openImagePicker());

        gridView.setOnItemClickListener((adapterView, view, i, l) -> {
            Intent intent = new Intent(this, ImageDetailsActivity.class);
            intent.putExtra("imageUri", imageUris.get(i).toString());
            startActivity(intent);
        });
    }

    void openFolderPicker(boolean isSave) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        if (isSave) {
            selectSaveFolderLauncher.launch(intent);
        } else {
            selectLoadFolderLauncher.launch(intent);
        }
    }

    void takePhoto() {
        if (selectedSaveFolderUri == null) {
            Toast.makeText(this, "No save folder selected", Toast.LENGTH_SHORT).show();
            return;
        }

        String fileName = "IMG_" + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date()) + ".jpg";
        DocumentFile folder = DocumentFile.fromTreeUri(this, selectedSaveFolderUri);
        DocumentFile newFile = folder.createFile("image/jpeg", fileName);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, newFile.getUri());
        startActivity(intent);
    }

    void loadImagesFromFolder() {
        imageUris.clear();
        if (selectedLoadFolderUri == null) {
            Toast.makeText(this, "No folder selected", Toast.LENGTH_SHORT).show();
            return;
        }

        DocumentFile folder = DocumentFile.fromTreeUri(this, selectedLoadFolderUri);
        for (DocumentFile file : folder.listFiles()) {
            if (file.isFile() && file.getName().endsWith(".jpg")) {
                imageUris.add(file.getUri());
            }
        }

        imageAdapter = new ImageUriAdapter(this, imageUris);
        gridView.setAdapter(imageAdapter);
    }

    void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        selectImagesLauncher.launch(intent);
    }
}
