package com.example.devcash.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.devcash.Model.CategoryList;
import com.example.devcash.Model.EmployeeList;

import org.w3c.dom.Text;

import java.security.PrivateKey;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    //database name
    static String DATABASE = "devcashdb";


    //owner table
    static String OWNER = "tbl_owner";
    static String COL_OWNER_ID = "owner_id";
//    static String COL_ACCT_ID = "account_id"; //foreign key
//    static String COL_ENT_ID = "enterprise_id"; //foreign key
//    static String COL_TASK_ID = "task_id";
    static String COL_OWNER_LNAME = "owner_lname";
    static String COL_OWNER_FNAME = "owner_fname";
    static String COL_OWNER_IMAGE = "owner_image";
    static String COL_OWNER_DOB = "owner_dob";
    static String COL_OWNER_GENDER = "owner_gender";
    static String COL_OWNER_CTCNUM = "owner_ctcnum";

    //enterprise table
    static String ENTERPRISE = "tbl_enterprise";
    static String COL_ENT_ID = "enterprise_id";
//    static String COL_OWNER_ID = "owner_id"; //foreign key
    static String COL_ENT_NAME = "ent_name";
    static String COL_ENT_INDUSTRY_TYPE = "ent_industry_type";
    static String COL_ENT_ADDR = "ent_address";
    static String COL_ENT_TELNO = "ent_telno";
    static String COL_ENT_PERMIT = "ent_permit";
    static String COL_ENT_TIN = "ent_tin";

    //enterprise type
    static String ENTERPRISE_TYPE = "tbl_ent_type";
    static String COL_ENT_TYPE_ID = "ent_type_id";
//    static String COL_ENT_ID = "enterprise_id"; // foreign key
    static String COL_ENT_TYPE = "ent_type";
    static String COL_ENT_NO_EMP = "ent_tot_num_emp";

    //account table
    static String ACCOUNT = "tbl_account";
    static String COL_ACCT_ID = "acct_id";
    static String COL_ACCT_UNAME = "acct_uname";
    static String COL_ACCT_EMAIL = "acct_email";
    static String COL_ACCT_PASSW = "acct_passw";
    static String COL_ACCT_TYPE = "acct_type";
    static String COL_ACCT_STATUS = "acct_status";

    //employee table
    static String EMPLOYEE = "tbl_employee";
    static String COL_EMP_ID = "emp_id";
//    static String OWNER = "tbl_owner"; //foreign key
//static String COL_ACCT_ID = "acct_id"; //foreign key
//static String COL_TASK_ID = "task_id"; //foreign key
    static String COL_EMP_LNAME = "emp_lname";
    static String COL_EMP_FNAME = "emp_fname";
    static String COL_EMP_IMG = "emp_img";
    static String COL_EMP_DOB = "emp_dob";
    static String COL_EMP_GENDER = "emp_gender";
    static String COL_EMP_PHONE = "emp_phone";
    static String COL_EMP_TASK = "emp_task";

    //category table
    static String CATEGORY = "tbl_category";
    static String COL_CAT_ID = "category_id";
    static String COL_CAT_NAME = "category_name";
    static String COL_CAT_ITEM_COUNT = "category_item_count";

    //payment table
    static String OWNER_PAYMENT = "tbl_owner_payment";
    static String COL_PAYMENT_ID = "payment_id";
//    static String COL_OWNER_ID = "owner_id"; //foreign key
    static String COL_PAYMENT_DATE_GIVEN = "payment_date_given";
    static String COL_PAYMENT_START_DATE = "payment_start_date";
    static String COL_PAYMENT_END_DATE = "payment_end_date";
    static String COL_PAYMENT_DUE = "payment_due_date";
    static String COL_PAYMENT_AMOUNT = "payment_amount";
    static String COL_PAYMENT_PREV_PAYMENT_AMOUNT = "payment_prev_amt";
    static String COL_PAYMENT_STATUS = "payment_status";

    //subscription_type table
    static String SUBSCRIPTION = "tbl_subscription";
    static String COL_SUBS_TYPE = "subcription_type";
    static String COL_SUBS_VALUE = "subscription_value";
    static String COL_SUBS_AMT = "subscription_amt";

    //product table
    static String PRODUCT = "tbl_product";
    static String COL_PROD_ID = "prod_id";
//    static String COL_DISC_ID = "disc_id"; //foreign key
//    static String COL_CAT_ID = "category_id"; // foreign key
//    static String COL_INV_ID = "inv_id"; //foreign key
    static String COL_PROD_QRCODE = "prod_qrcode";
    static String COL_PROD_TITLE = "prod_title";
    static String COL_PROD_IMAGE = "prod_image";
    static String COL_PROD_PRICE = "prod_price";
    static String COL_PROD_QTY_PER_UNIT = "prod_qty_per_unit";
    static String COL_PROD_UNIT_MEASURE = "prod_unit_measure";
    static String COL_PROD_STATUS = "prod_status";
    static String COL_PROD_CONDITION = "prod_condition";
    static String COL_PROD_CONDITION_COUNT = "prod_condition_count";
    static String COL_PROD_ROP = "prod_rop";
    static String COL_PROD_EXP_DATE = "prod_exp_date";
    static String COL_PROD_EXP_DATE_COUNT = "prod_exp_date_count";

    //inventory table
    static String INVENTORY = "tbl_inventory";
    static String COL_INV_ID = "inventory_id";
//    static String PRODUCT = "tbl_product"; //foreign key
    static String COL_INV_STOCK = "inventory_stock";
    static String COL_INV_DATE_ADDED = "inventory_date_added";
    static String COL_INV_DATE_UPDATED = "inventory_date_updated";

    //service table
    static String SERVICE = "tbl_service";
//    static String COL_DISC_ID = "discount_id"; //foreign key
    static String COL_SERVICE_ID = "service_id";
    static String COL_SERVICE_QRCODE = "service_qrcode";
    static String COL_SERVICE_TITLE = "service_title";
    static String COL_SERVICE_PRICE = "service_price";
    static String COL_SERVICE_IMAGE = "service_image";

    //discount table
    static String DISCOUNT = "tbl_discount";
    static String COL_DISC_ID = "discount_id";
//    static String OWNER = "tbl_owner"; //foreign key
    static String COL_DISC_CODE = "discount_code";
    static String COL_DISC_VALUE = "discount_value";
    static String COL_DISC_TYPE = "discount_type";
    static String COL_DISC_STATUS = "discount_status";
    static String COL_DISC_DATE_CREATED = "discount_date_created";
    static String COL_DISC_START_DATE = "discount_start_date";
    static String COL_DISC_END_DATE = "discount_end_date";

    //purchase transaction table
    static String TRANSACTION = "tbl_trans";
    static String COL_TRANS_ID = "trans_id";
//    static String COL_PROD_ID = "prod_id"; //foreign key
//    static String COL_SERVICE_ID = "service_id"; //foreign key
//    static String COL_EMP_ID = "emp_id"; //foreign key
//    static String COL_ENT_ID = "enterprise_id"; //foreign key
    static String COL_TRANS_CUST_TYPE = "trans_cust_type";
    static String COL_TRANS_CUST_CTCNUM = "trans_cust_ctcnum";
    static String COL_TRANS_CUST_EMAIL = "trans_cust_email";
    static String COL_TRANS_DATE = "trans_date";
    static String COL_TRANS_PURCH_QTY = "trans_purch_qty";
    static String COL_TRANS_PURCH_AMT = "trans_purch_amt";
    static String COL_TRANS_TOT_PURCH_QTY = "trans_tot_purch_qty";
    static String COL_TRANS_TOT_PRICE = "trans_tot_price";
    static String COL_TRANS_VAT_SALES = "trans_vat_sales";
    static String COL_TRANS_VAT_AMT = "trans_vat_amt";
    static String COL_TRANS_VAT_VALUE = "trans_vat_value";
    static String COL_TRANS_VAT_EXEMPT = "trans_vat_exempt";
    static String COL_TRANS_ZERO_RATED = "trans_zero_rated";

    public DatabaseHelper(Context context) {
        super(context, DATABASE, null, 1);
    }

    private static final String CREATE_TABLE_CATEGORY = " CREATE TABLE "
            + CATEGORY + "("
            + COL_CAT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_CAT_NAME + " TEXT);";

    private static final String CREATE_TABLE_ENTERPRISE = " CREATE TABLE "
            + ENTERPRISE + "("
            + COL_ENT_ID + "INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_ENT_NAME + "TEXT NOT NULL,"
            + COL_ENT_INDUSTRY_TYPE + "TEXT, "
            + COL_ENT_ADDR + "TEXT,"
            + COL_ENT_TELNO + "INTEGER, "
            + COL_ENT_PERMIT + "TEXT, "
            + COL_ENT_TIN + "INTEGER,"
            + COL_OWNER_ID + "INTEGER REFERENCES "+OWNER+");";

    private static final String CREATE_TABLE_OWNER = " CREATE TABLE "
            + OWNER + "("
            + COL_OWNER_ID + "INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COL_OWNER_LNAME + "TEXT NOT NULL,"
            + COL_OWNER_FNAME + "TEXT NOT NULL, "
            + COL_OWNER_DOB + "DATE,"
            + COL_OWNER_GENDER + "TEXT,"
            + COL_OWNER_CTCNUM + "INTEGER,"
            + "FOREIGN KEY("+ COL_ACCT_ID +") REFERENCES "+ ACCOUNT +"("+ COL_ACCT_ID +"),"
            + "FOREIGN KEY("+ COL_ENT_ID+") REFERENCES" + ENTERPRISE + "("+ COL_ENT_ID+ "));";

    private static final String CREATE_TABLE_ACCOUNT = " CREATE TABLE "
            + ACCOUNT + "("
            + COL_ACCT_ID + "INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COL_ACCT_UNAME + "TEXT,"
            + COL_ACCT_EMAIL + "TEXT,"
            + COL_ACCT_PASSW + "TEXT,"
            + COL_ACCT_TYPE + "TEXT,"
            + COL_ACCT_STATUS + "TEXT);";

    private static final String CREATE_TABLE_EMPLOYEE = " CREATE TABLE "
            + EMPLOYEE + "("
            + COL_EMP_ID + "INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COL_EMP_LNAME + "TEXT,"
            + COL_EMP_FNAME + "TEXT,"
            + COL_EMP_IMG + "INTEGER,"
            + COL_EMP_DOB + "DATE,"
            + COL_EMP_GENDER + "TEXT,"
            + COL_EMP_PHONE + "INTEGER,"
            + COL_EMP_TASK + "TEXT,"
            + "FOREIGN KEY("+ COL_OWNER_ID +") REFERENCES "+ OWNER +"("+ COL_OWNER_ID +"), "
            + "FOREIGN KEY("+ COL_ACCT_ID +") REFERENCES "+ ACCOUNT +"("+ COL_ACCT_ID +"));";

    private static final String CREATE_TABLE_OWNER_PAYMENT = " CREATE TABLE"
            + OWNER_PAYMENT + "(  "
            + COL_PAYMENT_ID + "INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COL_PAYMENT_DATE_GIVEN + "DATE,"
            + COL_PAYMENT_START_DATE + "DATE,"
            + COL_PAYMENT_END_DATE + "DATE,"
            + COL_PAYMENT_DUE + "DATE,"
            + COL_PAYMENT_AMOUNT + "DOUBLE,"
            + COL_PAYMENT_PREV_PAYMENT_AMOUNT + "DOUBLE,"
            + COL_PAYMENT_STATUS + "TEXT,"
            + "FOREIGN KEY("+ COL_OWNER_ID +") REFERENCES "+ OWNER +"("+ COL_OWNER_ID+"));";

    private static final String CREATE_TABLE_ENT_TYPE = " CREATE TABLE "
            + ENTERPRISE_TYPE + "("
            + COL_ENT_TYPE_ID + "INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COL_ENT_TYPE + "TEXT,"
            + COL_ENT_NO_EMP + "INTEGER);";

    private static final String CREATE_TABLE_PRODUCT = " CREATE TABLE "
            + PRODUCT + "( "
            + COL_PROD_ID + "INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COL_PROD_QRCODE + "TEXT,"
            + COL_PROD_TITLE + "TEXT,"
            + COL_PROD_IMAGE + "INTEGER,"
            + COL_PROD_PRICE + "DOUBLE,"
            + COL_PROD_QTY_PER_UNIT + "INTEGER,"
            + COL_PROD_UNIT_MEASURE + "TEXT,"
            + COL_PROD_STATUS + "TEXT,"
            + COL_PROD_CONDITION + "TEXT,"
            + COL_PROD_CONDITION_COUNT + "INTEGER,"
            + COL_PROD_ROP + "INTEGER,"
            + COL_PROD_EXP_DATE + "DATE,"
            + COL_PROD_EXP_DATE_COUNT + "DATE,"
            + "FOREIGN KEY("+ COL_DISC_ID +") REFERENCES "+ DISCOUNT +"("+ COL_DISC_ID +"),"
            + "FOREIGN KEY("+ COL_CAT_ID +") REFERENCES " + CATEGORY + "("+ COL_CAT_ID +"), "
            + "FOREIGN KEY("+ COL_INV_ID +") REFERENCES " + INVENTORY + "("+ COL_INV_ID +"));";

    private static final String CREATE_TABLE_INVENTORY = " CREATE TABLE "
            + INVENTORY + "( "
            + COL_INV_ID + "INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COL_INV_STOCK + "INTEGER,"
            + COL_INV_DATE_ADDED + "DATE,"
            + COL_INV_DATE_UPDATED + "DATE,"
            + "FOREIGN KEY("+ COL_PROD_ID +") REFERENCES " + PRODUCT +"("+ COL_PROD_ID +"))";

    private static final String CREATE_TABLE_SERVICES = " CREATE TABLE "
            + SERVICE + "("
            + COL_SERVICE_ID + "INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COL_SERVICE_QRCODE + "INTEGER,"
            + COL_SERVICE_TITLE + "TEXT,"
            + COL_SERVICE_PRICE + "DECIMAL,"
            + COL_SERVICE_IMAGE + "INTEGER,"
            + "FOREIGN KEY("+ COL_DISC_ID +") REFERENCES " + DISCOUNT + "("+ COL_DISC_ID +"));";

    private static final String CREATE_TABLE_TRANSACTION = " CREATE TABLE "
            + TRANSACTION + "("
            + COL_TRANS_ID + "INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COL_TRANS_CUST_TYPE + "TEXT,"
            + COL_TRANS_CUST_CTCNUM + "INTEGER,"
            + COL_TRANS_CUST_EMAIL + "TEXT,"
            + COL_TRANS_DATE + "DATE,"
            + COL_TRANS_PURCH_QTY + "INTEGER,"
            + COL_TRANS_PURCH_AMT + "DOUBLE,"
            + COL_TRANS_TOT_PURCH_QTY + "DOUBLE,"
            + COL_TRANS_TOT_PRICE + "DOUBLE,"
            + COL_TRANS_VAT_SALES + "DOUBLE,"
            + COL_TRANS_VAT_AMT + "DOUBLE,"
            + COL_TRANS_VAT_VALUE + "DOUBLE,"
            + COL_TRANS_VAT_EXEMPT + "DOUBLE,"
            + COL_TRANS_ZERO_RATED + "DOUBLE,"
            + "FOREIGN KEY("+ COL_PROD_ID +") REFERENCES " + PRODUCT +"("+ COL_PROD_ID +"),"
            + "FOREIGN KEY("+ COL_SERVICE_ID +") REFERENCES " + SERVICE +"("+ COL_SERVICE_ID +"),"
            + "FOREIGN KEY("+ COL_EMP_ID +") REFERENCES "+ EMPLOYEE +"("+ COL_EMP_ID +"),"
            + "FOREIGN KEY("+ COL_ENT_ID +") REFERENCES "+ ENTERPRISE +"("+ COL_ENT_ID +"));";




    //calling the sql query created from CREATE_TABLE_EMPLOYEES
    @Override
    public void onCreate(SQLiteDatabase db) {
        //creating employee table
        db.execSQL(CREATE_TABLE_CATEGORY);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS '" + CATEGORY + "'");
        onCreate(db);

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
