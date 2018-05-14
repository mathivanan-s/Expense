package com.example.mathi.expense;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.ImageView;

import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

/**
 * Created by  MATHI on 8/1/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Expense.dp";
    public static final String EXPENSE_NAME = "EXPENSETABLE";
    public static final String INCOME_NAME = "INCOMETABLE";
    public static final String CATEGORY_TABLE = "CATEGORYTABLE";
    public static final String INCOME_CATEGORY_TABLE = "INCOMECATEGORYTABLE";
    public static final String THIRD_TABLE_NAME = "UserDetailsTable";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "PRICE";
    public static final String COL_3 = "CATEGORY";
    public static final String COL_4 = "DESCRIPTION";
    public static final String COL_5 = "DATE";

    public static final String COL_6 = "ID";
    public static final String COL_7 = "PRICE";
    public static final String COL_8 = "CATEGORY";
    public static final String COL_88 = "DESCRIPTION";
    public static final String COL_9 = "DATE";

    public static final String COL_10 = "ID";
    public static final String CATEGORY_COL_1 = "ITEM1";

    public static final String COL_11 = "ID";
    public static final String CATEGORY_COL_2 = "ITEM2";

    public static final String USER_TABLE_COL_1 = "ID";
    public static final String USER_TABLE_COL_2 = "IS_AUTHENTICATED";
    public static final String USER_TABLE_COL_3 = "USER_NAME";
    public static final String USER_TABLE_COL_4 = "JOB";
    public static final String USER_TABLE_COL_5 = "COMPANY";
    public static final String USER_TABLE_COL_6 = "SALARY";
    public static final String USER_TABLE_COL_7 = "EMAIL";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 3);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + EXPENSE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "PRICE TEXT, CATEGORY TEXT,DESCRIPTION TEXT,DATE DATETIME DEFAULT CURRENT_TIMESTAMP)");
        sqLiteDatabase.execSQL("create table " + INCOME_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "PRICE TEXT, CATEGORY TEXT,DESCRIPTION TEXT,DATE DATETIME DEFAULT CURRENT_TIMESTAMP)");
        sqLiteDatabase.execSQL("create table " + CATEGORY_TABLE + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "ITEM1 TEXT)");
        sqLiteDatabase.execSQL("create table " + INCOME_CATEGORY_TABLE + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "ITEM2 TEXT)");
        sqLiteDatabase.execSQL("create table " + THIRD_TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "IS_AUTHENTICATED TEXT ,USER_NAME TEXT ,JOB TEXT ,COMPANY TEXT, SALARY TEXT, EMAIL TEXT )");
        sqLiteDatabase.execSQL("INSERT INTO " + CATEGORY_TABLE+ "(ID, ITEM1 ) VALUES ('1','Fuel'),('2','Food')," +
                "('3','Mobile'),('4','Travel'),('5','Bike service'),('6','Education'),('7','Fun'),('8','Health'),('9','Pets'),('10','Others')");
        sqLiteDatabase.execSQL("INSERT INTO " + INCOME_CATEGORY_TABLE+ "(ID, ITEM2 ) VALUES ('1','Advance'),('2','Salary')," +
                "('3','Credit card'),('4','Debit card'),('5','Loan')");
        sqLiteDatabase.execSQL("INSERT INTO " + THIRD_TABLE_NAME + "(" + USER_TABLE_COL_2 + ") VALUES ('false')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + EXPENSE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + INCOME_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CATEGORY_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + INCOME_CATEGORY_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + THIRD_TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
    public Boolean isAuthenticated() {
        String[] variable = {USER_TABLE_COL_1, USER_TABLE_COL_2};
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.query(THIRD_TABLE_NAME, variable,null,null,null,null,null);
        cursor.moveToFirst();
        return Boolean.valueOf(cursor.getString(1));
    }
    public String updateUserDetails(String column_name, Boolean state){
        String error = "no_error";
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues dataToInsert = new ContentValues();
        dataToInsert.put(column_name, state.toString());
        try{
            sqLiteDatabase.update(THIRD_TABLE_NAME, dataToInsert, "ID=1", null);
        }
        catch (Exception e){
            error =  e.getMessage().toString();
        }
        return error;
    }

    public boolean insertData(String price, String category, String description, String date) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, price);
        contentValues.put(COL_3, category);
        contentValues.put(COL_4, description);
        contentValues.put(COL_5, date);
        long result=sqLiteDatabase.insert(EXPENSE_NAME, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    public boolean insertUserDetail(String name, String job_title, String company, String salary,String email) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_TABLE_COL_3, name);
        contentValues.put(USER_TABLE_COL_4, job_title);
        contentValues.put(USER_TABLE_COL_5, company);
        contentValues.put(USER_TABLE_COL_6, salary);
        contentValues.put(USER_TABLE_COL_7, email);
        long result=sqLiteDatabase.insert(THIRD_TABLE_NAME, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    public boolean updateUserDetail(String name, String job_title, String company, String salary,String email,int id) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_TABLE_COL_3, name);
        contentValues.put(USER_TABLE_COL_4, job_title);
        contentValues.put(USER_TABLE_COL_5, company);
        contentValues.put(USER_TABLE_COL_6, salary);
        contentValues.put(USER_TABLE_COL_7, email);
        long result=sqLiteDatabase.update(THIRD_TABLE_NAME, contentValues,"ID="+id,null);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    public boolean insertCategorylist(String catelist) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CATEGORY_COL_1, catelist);
        long result=sqLiteDatabase.insert(CATEGORY_TABLE, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    public boolean updateCategorylist(String catelist,String id) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CATEGORY_COL_1, catelist);
        long result=sqLiteDatabase.update(CATEGORY_TABLE, contentValues,"ID="+id,null );
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    public boolean insertincomeCategorylist(String catelist) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CATEGORY_COL_2, catelist);
        long result=sqLiteDatabase.insert(INCOME_CATEGORY_TABLE, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    public boolean updateincomeCategorylist(String catelist,String id) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CATEGORY_COL_2, catelist);
        long result=sqLiteDatabase.update(INCOME_CATEGORY_TABLE, contentValues,"ID="+id,null );
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    public boolean insertIncomeData(String price,String category,String description,String date) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_7, price);
        contentValues.put(COL_8, category);
        contentValues.put(COL_88, description);
        contentValues.put(COL_9, date);
        long result=sqLiteDatabase.insert(INCOME_NAME, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    public boolean updateIncomeData(String price,String category,String description,String date,String id) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_7, price);
        contentValues.put(COL_8, category);
        contentValues.put(COL_88, description);
        contentValues.put(COL_9, date);
        long result=sqLiteDatabase.update(INCOME_NAME,contentValues,"ID="+id, null);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    public boolean updateExpenseData(String price,String category,String description,String date,String id) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, price);
        contentValues.put(COL_3, category);
        contentValues.put(COL_4, description);
        contentValues.put(COL_5, date);
        long result=sqLiteDatabase.update(EXPENSE_NAME,contentValues,"ID="+id, null);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    public Cursor getLast10Data() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor res = sqLiteDatabase.rawQuery("select * from  EXPENSETABLE order by id desc limit 0,10", null);
        return res;
    }
    public ArrayList<String> getentrydata(String month){
        Cursor cursor = getReadableDatabase().rawQuery("SELECT sum(PRICE) PRICE FROM EXPENSETABLE " +
                "where strftime('%m', DATE) ='"+month+"' group by CATEGORY order by CATEGORY ", null);
        cursor.moveToFirst();
        ArrayList<String> names = new ArrayList<String>();
        while(!cursor.isAfterLast()) {
            names.add(cursor.getString(cursor.getColumnIndex("PRICE")));
            cursor.moveToNext();
        }
        cursor.close();
        return names;
    }
    public ArrayList<String> getLabledata(String month){
        Cursor cursor = getReadableDatabase().rawQuery("SELECT CATEGORY FROM EXPENSETABLE  " +
                "where strftime('%m', DATE) ='"+month+"' group by CATEGORY order by CATEGORY", null);
        cursor.moveToFirst();
        ArrayList<String> names = new ArrayList<String>();
        while(!cursor.isAfterLast()) {
            names.add(cursor.getString(cursor.getColumnIndex("CATEGORY")));
            cursor.moveToNext();
        }
        cursor.close();
        return names;
    }
    public ArrayList<String> getentrydata1(String month){
        Cursor cursor = getReadableDatabase().rawQuery("SELECT sum(PRICE) PRICE FROM INCOMETABLE " +
                "where strftime('%m', DATE) ='"+month+"' group by CATEGORY order by CATEGORY ", null);
        cursor.moveToFirst();
        ArrayList<String> names = new ArrayList<String>();
        while(!cursor.isAfterLast()) {
            names.add(cursor.getString(cursor.getColumnIndex("PRICE")));
            cursor.moveToNext();
        }
        cursor.close();
        return names;
    }
    public ArrayList<String> getLabledata1(String month){
        Cursor cursor = getReadableDatabase().rawQuery("SELECT CATEGORY FROM INCOMETABLE  " +
                "where strftime('%m', DATE) ='"+month+"' group by CATEGORY order by CATEGORY", null);
        cursor.moveToFirst();
        ArrayList<String> names = new ArrayList<String>();
        while(!cursor.isAfterLast()) {
            names.add(cursor.getString(cursor.getColumnIndex("CATEGORY")));
            cursor.moveToNext();
        }
        cursor.close();
        return names;
    }
    public ArrayList<String> getgroupofsumofprice(String table,String month){
        Cursor cursor = getReadableDatabase().rawQuery("SELECT sum(PRICE) PRICE FROM "+table+" " +
                "where strftime('%m', DATE) ='"+month+"' group by CATEGORY order by CATEGORY ", null);
        cursor.moveToFirst();
        ArrayList<String> names = new ArrayList<String>();
        while(!cursor.isAfterLast()) {
            names.add(cursor.getString(cursor.getColumnIndex("PRICE")));
            cursor.moveToNext();
        }
        cursor.close();
        return names;
    }
    public ArrayList<String> getgroupofcategory(String table,String month){
        Cursor cursor = getReadableDatabase().rawQuery("SELECT CATEGORY FROM " +table+ "  " +
                "where strftime('%m', DATE) ='"+month+"' group by CATEGORY order by CATEGORY", null);
        cursor.moveToFirst();
        ArrayList<String> names = new ArrayList<String>();
        while(!cursor.isAfterLast()) {
            names.add(cursor.getString(cursor.getColumnIndex("CATEGORY")));
            cursor.moveToNext();
        }
        cursor.close();
        return names;
    }
    public ArrayList<String> getcategorylist(){
        Cursor cursor = getReadableDatabase().rawQuery("SELECT ITEM1 FROM "+CATEGORY_TABLE, null);
        cursor.moveToFirst();
        ArrayList<String> names = new ArrayList<String>();
        while(!cursor.isAfterLast()) {
            names.add(cursor.getString(cursor.getColumnIndex("ITEM1")));
            cursor.moveToNext();
        }
        cursor.close();
        return names;
    }

    public ArrayList<String> getincomecategorylist(){
        Cursor cursor = getReadableDatabase().rawQuery("SELECT ITEM2 FROM "+INCOME_CATEGORY_TABLE, null);
        cursor.moveToFirst();
        ArrayList<String> names = new ArrayList<String>();
        while(!cursor.isAfterLast()) {
            names.add(cursor.getString(cursor.getColumnIndex("ITEM2")));
            cursor.moveToNext();
        }
        cursor.close();
        return names;
    }

    public Cursor getTotalexpense(String mnth){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT SUM(PRICE) FROM " + EXPENSE_NAME + " WHERE strftime('%m', DATE) ='"+mnth+"'";
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        c.getInt(0);
        return c;
    }
    public Cursor getallExpense(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT SUM(PRICE) FROM " + EXPENSE_NAME;
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        c.getInt(0);
        return c;
    }
    public Cursor getallIncome(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT SUM(PRICE) FROM " + INCOME_NAME;
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        c.getInt(0);
        return c;
    }
    public Cursor getTotalincome(String mnth){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT SUM(PRICE) FROM " + INCOME_NAME + " WHERE strftime('%m', DATE) ='"+mnth+"'";
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        c.getInt(0);
        return c;
    }

    public Cursor getexpenseData(String month) {
        String[] variable = {COL_1, COL_2, COL_3,COL_4,COL_5};
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.query(EXPENSE_NAME + " where strftime('%m', DATE) ='"+month+"'",
                variable,null,null,null,null,null);
        return cursor;

    }
    public Cursor getincomeData(String month) {
        String[] variable = {COL_6, COL_7, COL_8,COL_88,COL_9};
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.query(INCOME_NAME + " where strftime('%m', DATE) ='"+month+"'",
                variable,null,null,null,null,null);
        return cursor;
    }

    public Cursor getCategoryData(String table_name) {
        String[] variable = {COL_10,CATEGORY_COL_1};
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.query(table_name, variable,null,null,null,null,null);
        return cursor;

    }
    public Cursor getincomeCategoryData(String table_name) {
        String[] variable = {COL_11,CATEGORY_COL_2};
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.query(table_name, variable,null,null,null,null,null);
        return cursor;

    }
    public void deleteAllexpense(String table_name){
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.delete(table_name, null, null);
        } catch (Exception e)
        {
        }
        db.close();
    }

    public void deleteExpense(String id) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("DELETE FROM " + EXPENSE_NAME + " WHERE " + COL_1 + "= '" + id + "'");
        database.close();
    }
    public void deleteIncome(String id) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("DELETE FROM " + INCOME_NAME + " WHERE " + COL_6 + "= '" + id + "'");
        database.close();
    }
    public void deleteCategorylist(String category) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("DELETE FROM " + CATEGORY_TABLE + " WHERE " + CATEGORY_COL_1 + "= '" + category + "'");
        database.close();
    }
    public void deleteincomeCategorylist(String category) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("DELETE FROM " + INCOME_CATEGORY_TABLE + " WHERE " + CATEGORY_COL_2 + "= '" + category + "'");
        database.close();
    }

    public Cursor getcategorywisedata(String category,String date) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor res = sqLiteDatabase.rawQuery("select * from  " + EXPENSE_NAME + " WHERE " + COL_3 +
                "= '" + category + "' and strftime('%m', DATE) ='"+date+"' ", null);
        return res;
    }
    public Cursor getindividualData(String table,String month,String category) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor res = sqLiteDatabase.rawQuery("select * from  " + table + " WHERE CATEGORY = '" +
                category + "' and strftime('%m', DATE) ='"+month+"' ", null);
        return res;
    }
    public String getusername(){
            Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM "+THIRD_TABLE_NAME, null);
            cursor.moveToFirst();
            String names = null;
            while(!cursor.isAfterLast()) {
                names=cursor.getString(cursor.getColumnIndex("USER_NAME"));
                cursor.moveToNext();
            }
            cursor.close();
            return names;
        }
    public String getusermail(){
        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM "+THIRD_TABLE_NAME, null);
        cursor.moveToFirst();
        String names = null;
        while(!cursor.isAfterLast()) {
            names=cursor.getString(cursor.getColumnIndex("EMAIL"));
            cursor.moveToNext();
        }
        cursor.close();
        return names;
    }
    public Cursor getuserdetail() {
        String[] variable = {USER_TABLE_COL_1,USER_TABLE_COL_3,USER_TABLE_COL_4,USER_TABLE_COL_5,USER_TABLE_COL_6,USER_TABLE_COL_7};
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.query(THIRD_TABLE_NAME, variable,null,null,null,null,null);
        return cursor;

    }



}
