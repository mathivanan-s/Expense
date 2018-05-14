package com.example.mathi.expense.category;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mathi.expense.R;
import com.example.mathi.expense.Utils;

import java.util.ArrayList;


public class CategoryCustomAdapter extends ArrayAdapter<Category> {
    private final Activity context;
    ImageView status_image;

    public CategoryCustomAdapter(Activity context, ArrayList<Category> resource) {
        super(context, 0, resource);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Category category = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.category_listview_list, parent, false);
        }

        // Lookup view for data population
        TextView t_body = (TextView) convertView.findViewById(R.id.get_category);
        TextView t_id = (TextView) convertView.findViewById(R.id.get_id);
        status_image=(ImageView)convertView.findViewById(R.id.category_image);

        // Populate the data into the template view using the data object
        t_body.setText(category.category);
        t_id.setText(String.valueOf(category.id));
        t_id.setVisibility(View.INVISIBLE);
        String encodedImage = Utils.getSelectedIcon(context,category.category);
        if (!encodedImage.equalsIgnoreCase("")) {
            //Decoding the Image and display in ImageView
            byte[] b = Base64.decode(encodedImage, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
            status_image.setImageBitmap(bitmap);

        }
        // Return the completed view to render on screen
        return convertView;
    }

}
