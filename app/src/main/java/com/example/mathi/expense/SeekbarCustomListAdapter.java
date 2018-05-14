package com.example.mathi.expense;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class SeekbarCustomListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] strArr;
    private final String[] strArr1;
    ImageView status_image;
    TextView machine,aeff;


    public SeekbarCustomListAdapter(Activity context, String[] strArr, String[] strArr1)
    {

        super(context, R.layout.seekbar_listview_list, strArr);

        this.context=context;
        this.strArr=strArr;
        this.strArr1=strArr1;

    }
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.seekbar_listview_list, null,true);


         machine = (TextView) rowView.findViewById(R.id.get_price);
         aeff = (TextView) rowView.findViewById(R.id.get_category);

        status_image=(ImageView)rowView.findViewById(R.id.stop);

        machine.setText(strArr[position]);
        aeff.setText(strArr1[position]);
        String encodedImage = Utils.getSelectedIcon(context,strArr1[position]);
        if (!encodedImage.equalsIgnoreCase("")) {
            //Decoding the Image and display in ImageView
            byte[] b = Base64.decode(encodedImage, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
            status_image.setImageBitmap(bitmap);

        }


        return rowView;

    };

}
