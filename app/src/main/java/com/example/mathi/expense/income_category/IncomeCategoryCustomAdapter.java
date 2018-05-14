package com.example.mathi.expense.income_category;

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


public class IncomeCategoryCustomAdapter extends ArrayAdapter<Income_category> {

    ImageView status_image;
    Activity context;

    public IncomeCategoryCustomAdapter(Activity context, ArrayList<Income_category> resource) {
        super(context, 0, resource);
        this.context=context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Income_category income_category = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.income_category_listview_list, parent, false);
        }

        // Lookup view for data population
        TextView t_body = (TextView) convertView.findViewById(R.id.get_category);
        TextView t_id = (TextView) convertView.findViewById(R.id.get_id);
        status_image=(ImageView)convertView.findViewById(R.id.category_image);

        // Populate the data into the template view using the data object
        t_body.setText(income_category.category);
        t_id.setText(String.valueOf(income_category.id));
        t_id.setVisibility(View.INVISIBLE);
        String encodedImage = Utils.getSelectedIcon(context,income_category.category);
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
