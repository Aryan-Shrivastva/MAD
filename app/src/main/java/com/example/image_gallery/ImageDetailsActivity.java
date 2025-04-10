package com.example.image_gallery;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.Date;

public class ImageDetailsActivity extends AppCompatActivity {

    public static final String IMAGE_DELETED = "image_deleted"; // flag for deletion

    ImageView imageView;
    TextView textDetails;
    Button delete;
    String imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_details);

        imageView = findViewById(R.id.imageView);
        textDetails = findViewById(R.id.textDetails);
        delete = findViewById(R.id.delete);

        imagePath = getIntent().getStringExtra("imagePath");
        assert imagePath != null;
        File imgFile = new File(imagePath);

        if (imgFile.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            imageView.setImageBitmap(bitmap);

            String info = "Name: " + imgFile.getName() + "\nPath: " + imgFile.getAbsolutePath() + "\nSize: "
                    + (imgFile.length() / 1024) + " KB"
                    + "\nLast Modified: " + new Date(imgFile.lastModified());
            textDetails.setText(info);
        }

        delete.setOnClickListener(v -> new AlertDialog.Builder(this)
                .setTitle("Delete Image")
                .setMessage("Are you sure you want to delete this image?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    if (imgFile.delete()) {
                        Toast.makeText(this, "Image Deleted", Toast.LENGTH_SHORT).show();

                        Intent resultIntent = new Intent();
                        resultIntent.putExtra(IMAGE_DELETED, true);
                        setResult(RESULT_OK, resultIntent);
                        finish();
                    } else {
                        Toast.makeText(this, "Failed to delete", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No", null)
                .show());
    }
}
