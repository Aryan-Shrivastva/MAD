package com.example.image_gallery;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;

public class ImageUriAdapter extends BaseAdapter {
    Context context; //context to access resources
    ArrayList<Uri> imageUris; // ArrayList holding the URIs of the images


    // Constructor to initialize context and image URIs list
    public ImageUriAdapter(Context context, ArrayList<Uri> imageUris) {
        this.context = context;
        this.imageUris = imageUris;
    }

    @Override public int getCount() {
        return imageUris.size(); // returns number of images
    }
    @Override public Object getItem(int i) {
        return imageUris.get(i); // return the image URI at position i
    }
    @Override public long getItemId(int i) {
        return i; // Return the index as the item id
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        ImageView imageView;

        // if there is no reused view, we create a new ImageView
        if (convertView == null) {
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(300, 300));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP); // cropping the image to fit the view
        } else {
            imageView = (ImageView) convertView; // for reusing the existing view
        }

        imageView.setImageURI(imageUris.get(i));  // set the image URI to the ImageView
        return imageView;
    }
}
