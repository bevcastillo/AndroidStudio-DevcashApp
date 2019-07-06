package com.example.devcash.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.devcash.Model.CategoryList;
import com.example.devcash.Model.EmployeeList;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    static String DATABASE = "devcashdb";
    static String EMPLOYEE = "tbl_employee";
    static String COL_ID = "emp_id";
    static String COL_IMG = "emp_img";
    static String COL_LNAME = "emp_lname";
    static String COL_FNAME = "emp_fname";
    static String COL_EMAIL = "emp_email";
    static String COL_PHONE = "emp_phone";
    static String COL_TASK = "emp_task";

    static String CATEGORY = "tbl_category";
    static String COL_CAT_ID = "category_id";
    static String COL_CAT_NAME = "category_name";



    public DatabaseHelper(Context context) {
        super(context, DATABASE, null, 1);
    }

    //creating the table
    private static final String CREATE_TABLE_EMPLOYEES = " CREATE TABLE " + EMPLOYEE + "(" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_IMG + " TEXT, " + COL_LNAME + " TEXT, " + COL_FNAME + " TEXT, " + COL_EMAIL + " TEXT, " + COL_PHONE + " TEXT, " + COL_TASK +" TEXT);";
    private static final String CREATE_TABLE_CATEGORY = " CREATE TABLE " + CATEGORY + "(" + COL_CAT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_CAT_NAME + " TEXT);";

    //calling the sql query created from CREATE_TABLE_EMPLOYEES
    @Override
    public void onCreate(SQLiteDatabase db) {
        //creating employee table
        db.execSQL(CREATE_TABLE_EMPLOYEES);
        db.execSQL(CREATE_TABLE_CATEGORY);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS '" + EMPLOYEE + "' ");
        db.execSQL("DROP TABLE IF EXISTS '" + CATEGORY + "'");
        onCreate(db);

    }

    //method to insert data into the database
    public long addEmployee(String emp_lname, String emp_fname, String emp_email, String emp_phone){
        SQLiteDatabase db = this.getWritableDatabase();

        //creating the contentvalues
        ContentValues values = new ContentValues();
        values.put(COL_LNAME, emp_lname);
        values.put(COL_FNAME, emp_fname);
        values.put(COL_EMAIL, emp_email);
        values.put(COL_PHONE, emp_phone);

        //insert row in employee table
        long insert = db.insert(EMPLOYEE, null, values);

        return insert;
    }

    //method to insert data into category table
    public long addCategory(String category_name){
        SQLiteDatabase db = this.getWritableDatabase();

        //creating the contentvalues
        ContentValues values = new ContentValues();
        values.put(COL_CAT_NAME, category_name);

        //inserting the row in category table
        long insert = db.insert(CATEGORY, null, values);

        return insert;
    }

    //displaying all the data from employees table
    public ArrayList<EmployeeList> getAll(){
        ArrayList<EmployeeList> employeeListArrayList = new ArrayList<EmployeeList>();

        String selectQuery = "SELECT * FROM " + EMPLOYEE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        //looping through all rows and adding to list
        if(c.moveToFirst()){
            do{
                EmployeeList employeeList = new EmployeeList();
                employeeList.setEmp_id(c.getInt(c.getColumnIndex(COL_ID)));
                employeeList.setEmp_lname(c.getString(c.getColumnIndex(COL_LNAME)));
                employeeList.setEmp_fname(c.getString(c.getColumnIndex(COL_FNAME)));
                employeeList.setEmp_email(c.getString(c.getColumnIndex(COL_EMAIL)));
                employeeList.setEmp_phone(c.getString(c.getColumnIndex(COL_PHONE)));

                //adding to employee list
            }while (c.moveToNext());
        }

        return employeeListArrayList;
    }

    //display all category details
    public ArrayList<CategoryList> getAllCategory(){
        ArrayList<CategoryList> categoryListArrayList = new ArrayList<CategoryList>();

        String selectQuery = "SELECT * FROM " + CATEGORY;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        //looping through all rows and adding to list
        if(c.moveToFirst()){
            do{
                CategoryList categoryList = new CategoryList();
                categoryList.setCategory_id(c.getInt(c.getColumnIndex(COL_CAT_ID)));
                categoryList.setCategory_name(c.getString(c.getColumnIndex(COL_CAT_NAME)));

                //adding to category list
                categoryListArrayList.add(categoryList);
            }while (c.moveToNext());
        }

        db.close();
        return categoryListArrayList;
    }

}
