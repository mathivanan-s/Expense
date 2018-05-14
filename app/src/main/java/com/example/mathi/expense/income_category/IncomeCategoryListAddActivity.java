package com.example.mathi.expense.income_category;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mathi.expense.CustomGridViewActivity;
import com.example.mathi.expense.DatabaseHelper;
import com.example.mathi.expense.R;
import com.example.mathi.expense.Utils;
import com.example.mathi.expense.category.Category;
import com.example.mathi.expense.category.CategoryCustomAdapter;
import com.example.mathi.expense.category.CategoryListAddActivity;
import com.example.mathi.expense.expense.ExpenseActivity;
import com.example.mathi.expense.income.IncomeActivity;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;


/**
 * Created by MATHI on 8/11/2017.
 */

public class IncomeCategoryListAddActivity extends AppCompatActivity{
    DatabaseHelper databaseHelper;
    EditText input,input1;
    ListView lv,listview;
    TextView t_id,t_category,t_get_text;
    IncomeCategoryCustomAdapter adapter;
    String category,option;
    int id;
    int[] icons = new int[]{R.drawable.icon1, R.drawable.icon2, R.drawable.icon3, R.drawable.icon4, R.drawable.icon5,
            R.drawable.icon6, R.drawable.icon7, R.drawable.icon8, R.drawable.icon9, R.drawable.icon10, R.drawable.icon11,
            R.drawable.icon12, R.drawable.icon13, R.drawable.icon14, R.drawable.icon15, R.drawable.icon16, R.drawable.icon17,
            R.drawable.icon18, R.drawable.icon19, R.drawable.icon20, R.drawable.icon21, R.drawable.icon22, R.drawable.icon23,
            R.drawable.icon24, R.drawable.icon25, R.drawable.icon26, R.drawable.icon27, R.drawable.icon28, R.drawable.icon29,
            R.drawable.icon30, R.drawable.icon31, R.drawable.icon32, R.drawable.icon33, R.drawable.icon34, R.drawable.icon35,
            R.drawable.icon36, R.drawable.icon37, R.drawable.icon38, R.drawable.icon39, R.drawable.icon40,R.drawable.icon41,
            R.drawable.icon42, R.drawable.icon43, R.drawable.icon44, R.drawable.icon45, R.drawable.icon46, R.drawable.icon47,
            R.drawable.icon48, R.drawable.icon49, R.drawable.icon50,R.drawable.icon51, R.drawable.icon52, R.drawable.icon53,
            R.drawable.icon54, R.drawable.icon55, R.drawable.icon56, R.drawable.icon57, R.drawable.icon58, R.drawable.icon59,
            R.drawable.icon60
    };
    String[] OPTION={"Edit","Delete","Change Icon","Remove Icon"};
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.income_category_list_add_activity);
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));


        databaseHelper = new DatabaseHelper(this);
        lv=(ListView)findViewById(R.id.category_add_list);

        ArrayList<Income_category> arrayOfItems = new ArrayList<Income_category>();
        adapter = new IncomeCategoryCustomAdapter(this, arrayOfItems);
        lv.setAdapter(adapter);
        fetchData(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                t_category = (TextView) view.findViewById(R.id.get_category);
                t_id = (TextView) view.findViewById(R.id.get_id);
                //dialog box
                editdeletedialog();
            }
        });
    }
    public void editdeletedialog(){
        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.
                Builder(IncomeCategoryListAddActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.edit_delete_listview, null);
        alertDialog.setView(convertView);

        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(IncomeCategoryListAddActivity.this,
                R.layout.edit_del_list,OPTION);
        listview =  (ListView) convertView.findViewById(R.id.edit_del_lv);
        listview.setAdapter(arrayAdapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                t_get_text = (TextView) view.findViewById(R.id.del_edit_text);
                option=t_get_text.getText().toString();
                switch(option) {
                    case "Edit":
                        editDialogeset();
                        break;
                    case "Delete":
                        removeItemFromList(position);
                        break;
                    case "Change Icon":
                        change_icon();
                        break;
                    case "Remove Icon":
                        Utils.removeSelectedIcon(IncomeCategoryListAddActivity.this,t_category.getText().toString());
                        finish();
                        overridePendingTransition( 0, 0);
                        startActivity(getIntent());
                        overridePendingTransition( 0, 0);
                        Toast.makeText(IncomeCategoryListAddActivity.this, "Icon Removed", Toast.LENGTH_SHORT).show();
                        break;
                }

            }
        });
        alertDialog.show();
    }public void change_icon(){
        final android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.
                Builder(IncomeCategoryListAddActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.icon_gridview, null);
        alertDialog.setView(convertView);

        CustomGridViewActivity adapterViewAndroid = new CustomGridViewActivity(IncomeCategoryListAddActivity.this,icons);
        final GridView androidGridView=(GridView)convertView.findViewById(R.id.gridview_android_example);
        androidGridView.setAdapter(adapterViewAndroid);
        androidGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int i, long id) {
                ImageView image = (ImageView) view.findViewById(R.id.icons);
                image.buildDrawingCache();
                Bitmap bitmap = image.getDrawingCache();
                ByteArrayOutputStream stream=new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
                byte[] image_byte=stream.toByteArray();
                String img_str = Base64.encodeToString(image_byte, 0);
                Utils.storeSelectedIcon(IncomeCategoryListAddActivity.this,img_str,t_category.getText().toString());
                finish();
                overridePendingTransition( 0, 0);
                startActivity(getIntent());
                overridePendingTransition( 0, 0);
            }
        });
        alertDialog.show();
    }

    public void editDialogeset(){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(IncomeCategoryListAddActivity.this);
        alertDialog.setTitle("CATEGORY");
        alertDialog.setMessage("Edit Category");

        input1 = new EditText(IncomeCategoryListAddActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input1.setLayoutParams(lp);
        alertDialog.setView(input1);
        input1.setText(""+t_category.getText().toString());

        alertDialog.setIcon(R.drawable.ic_edit_category);

        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if(input1.length()==0){
                            Toast.makeText(IncomeCategoryListAddActivity.this, "Please Enter Your Category", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            update_database();
                        }
                    }
                });
        alertDialog.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.show();

    }

    public void fetchData(IncomeCategoryCustomAdapter adapter){
        Cursor cursor;
        cursor = databaseHelper.getincomeCategoryData(databaseHelper.INCOME_CATEGORY_TABLE);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Income_category cartItem = new Income_category(category,id);
                cartItem.setCategory(cursor.getString(1));
                cartItem.setId(cursor.getInt(0));
                adapter.add(cartItem);
            }
            while (cursor.moveToNext());
        }
        else {
            Toast.makeText(IncomeCategoryListAddActivity.this, "No Data found", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.income_category_add_bar_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_income_category_add) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(IncomeCategoryListAddActivity.this);
            alertDialog.setTitle("CATEGORY");
            alertDialog.setMessage("Enter Category");

            input = new EditText(IncomeCategoryListAddActivity.this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            input.setLayoutParams(lp);
            alertDialog.setView(input);
            alertDialog.setIcon(R.drawable.ic_edit_category);
            alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    if(input.length()==0){
                        Toast.makeText(IncomeCategoryListAddActivity.this, "Please Enter Your Category", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        insert_database();
                    }
                }
            });

            alertDialog.setNegativeButton("NO",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            alertDialog.show();
        }
        return true;
    }
    protected void removeItemFromList(final int position) {
        int deletePosition = position;
        android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(
                IncomeCategoryListAddActivity.this);
        alert.setTitle("Delete");
        alert.setMessage("Do you want delete?");
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (!adapter.isEmpty()) {
                    final ProgressDialog progressdialog = new ProgressDialog(IncomeCategoryListAddActivity.this);
                    progressdialog.setMessage("Please wait..");
                    progressdialog.show();
                    databaseHelper.deleteincomeCategorylist(t_category.getText().toString());
                    new android.os.Handler().postDelayed(new Runnable() {
                        public void run() {
                            Toast.makeText(IncomeCategoryListAddActivity.this, "data deleted successfully", Toast.LENGTH_SHORT).show();
                            finish();
                            overridePendingTransition( 0, 0);
                            startActivity(getIntent());
                            overridePendingTransition( 0, 0);
                            progressdialog.dismiss();

                        }
                    },100);
                }
                else {
                    Toast.makeText(IncomeCategoryListAddActivity.this, "No data found", Toast.LENGTH_SHORT).show();
                }
            }
        });
        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                dialog.dismiss();
            }
        });
        alert.show();
    }
    public void insert_database(){
        boolean inserted = databaseHelper.insertincomeCategorylist(input.getText().toString());
        if(inserted)
        {final ProgressDialog progressdialog = new ProgressDialog(IncomeCategoryListAddActivity.this);
            progressdialog.setMessage("Please wait..");
            progressdialog.show();

            new android.os.Handler().postDelayed(new Runnable() {
                public void run() {
                    Toast.makeText(IncomeCategoryListAddActivity.this,"Data Saved", Toast.LENGTH_LONG).show();
                    finish();
                    overridePendingTransition( 0, 0);
                    startActivity(getIntent());
                    overridePendingTransition( 0, 0);
                    progressdialog.dismiss();

                }
            },100);
        }
        else
        {
            Toast.makeText(IncomeCategoryListAddActivity.this,"Data not Saved", Toast.LENGTH_LONG).show();
        }
    }
    public void update_database(){
        boolean inserted = databaseHelper.updateincomeCategorylist(input1.getText().toString(),t_id.getText().toString());
        if(inserted)
        {final ProgressDialog progressdialog = new ProgressDialog(IncomeCategoryListAddActivity.this);
            progressdialog.setMessage("Please wait..");
            progressdialog.show();

            new android.os.Handler().postDelayed(new Runnable()
            {
                public void run() {
                    Toast.makeText(IncomeCategoryListAddActivity.this,"Data Saved", Toast.LENGTH_LONG).show();
                    finish();
                    overridePendingTransition( 0, 0);
                    startActivity(getIntent());
                    overridePendingTransition( 0, 0);
                    progressdialog.dismiss();
                }
            },100);
        }
        else
        {
            Toast.makeText(IncomeCategoryListAddActivity.this,"Data not Saved", Toast.LENGTH_LONG).show();
        }
    }
}
