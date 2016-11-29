package ecom.com.mshop.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import ecom.com.mshop.Utils.UserDetails;

public class DBHelper extends SQLiteOpenHelper{

    private static final String DB_NAME = "mShopDB";
    private static final int DATABASE_VERSION = 2;
    private static final String ITEM_TABLE_NAME = "ITEM_TABLE";
    private static final String FAV_CART="ITEM_IN_CART";
    private static final String USER_TABLE="USER_TABLE";

    private static final String CREATE_ITEM_TABLE_NAME_SQL = "CREATE TABLE " + ITEM_TABLE_NAME + "(" +
            "ITEM_BAR_CODE_NUM TEXT NOT NULL, " +
            "ITEM_ID INTEGER NOT NULL, " +
            "ITEM_ARTICLE_ID TEXT NOT NULL, " +
            "ITEM_NAME TEXT, " +
            "ITEM_PRICE REAL NOT NULL, " +
            "ITEM_QUANTITY INTEGER, " +
            "ITEM_DESCRIPTION TEXT, " +
            "ITEM_ADDRESS TEXT, " +
            "ITEM_IMAGE_URL TEXT " +
            ")";


    private static final String CREATE_ITEM_IN_CART= "CREATE TABLE " + FAV_CART + "(" +
            "ITEM_NAME TEXT, " +
            "ITEM_PRICE REAL NOT NULL, " +
            "ITEM_IMAGE_URL TEXT, " +
            "ITEM_DESCRIPTION TEXT, " +
            "ITEM_ADDRESS TEXT, " +
            "ITEM_COUNT, " +
            "ITEM_ID INTEGER PRIMARY KEY NOT NULL " +
            ")";

    private static final String CREATE_USER= "CREATE TABLE " + USER_TABLE + "(" +
            "USER_NAME TEXT NOT NULL, " +
            "USER_EMAIL TEXT NOT NULL, "+
            "USER_MOBILE TEXT NOT NULL, "+
            "USER_LOGGED_IN INTEGER DEFAULT 0 " +
            ")";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL("DROP TABLE IF EXISTS " + ITEM_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + FAV_CART);
            db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);

            onCreate(db);
        }catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL("DROP TABLE IF EXISTS " + ITEM_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + FAV_CART);
            db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
            onCreate(db);
        }catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(CREATE_ITEM_TABLE_NAME_SQL);
            db.execSQL(CREATE_ITEM_IN_CART);
            db.execSQL(CREATE_USER);
        }catch (Exception Ex){
            Ex.printStackTrace();
            Log.e("DBHelper", Ex.getMessage());
        }
    }

    //*****************************Begin Item database*******************************************************//

    public long insertItemIntoSQLTable(int itemId,String barCodeNumber, String itemArticleID, String itemName, float itemPrice,
                                       int itemQuantity, String itemDescription, String location, String imageUrl){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ITEM_ID",itemId);
        contentValues.put("ITEM_BAR_CODE_NUM", barCodeNumber);
        contentValues.put("ITEM_ARTICLE_ID", itemArticleID);
        contentValues.put("ITEM_NAME", itemName);
        contentValues.put("ITEM_PRICE", itemPrice);
        contentValues.put("ITEM_QUANTITY", itemQuantity);
        contentValues.put("ITEM_DESCRIPTION", itemDescription);
        contentValues.put("ITEM_ADDRESS",location);
        contentValues.put("ITEM_IMAGE_URL",imageUrl);
        long rowID = db.insert(ITEM_TABLE_NAME, null, contentValues);
        db.close();
        return rowID;
    }

    public Cursor getItems() {
        String sql = "SELECT * FROM " + ITEM_TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(sql, null, null);
    }

    //*****************************End Item database************************************************************//

    public ArrayList<Items.ProductsData> getItemsList() {

        ArrayList<Items.ProductsData> list = new ArrayList<>();

        String sql = "SELECT * FROM " + ITEM_TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor itemQuery = db.rawQuery(sql, null);
        if (itemQuery.moveToFirst()) {
            do {
                Items itemInstance = new Items();
                Items.ProductsData productsData = itemInstance.new ProductsData();

                String itemBarCodeNo = itemQuery.getString(itemQuery.getColumnIndex("ITEM_BAR_CODE_NUM"));
                String itemArticleid = itemQuery.getString(itemQuery.getColumnIndex("ITEM_ARTICLE_ID"));
                String itemName = itemQuery.getString(itemQuery.getColumnIndex("ITEM_NAME"));
                float itemPriceperquantity = itemQuery.getFloat(itemQuery.getColumnIndex("ITEM_PRICE"));
                int itemQuantity = itemQuery.getInt(itemQuery.getColumnIndex("ITEM_QUANTITY"));
                String itemDescription = itemQuery.getString(itemQuery.getColumnIndex("ITEM_DESCRIPTION"));
                int itemId=itemQuery.getInt(itemQuery.getColumnIndex("ITEM_ID"));
                String itemLocation=itemQuery.getString(itemQuery.getColumnIndex("ITEM_ADDRESS"));
                String imageUrl= itemQuery.getString(itemQuery.getColumnIndex("ITEM_IMAGE_URL"));
                productsData.setItemName(itemName);
                productsData.setItemArticleID(itemArticleid);
                productsData.setBarCodeNumber(itemBarCodeNo);
                productsData.setItemDescription(itemDescription);
                productsData.setItemPrice(itemPriceperquantity);
                productsData.setItemQuantity(itemQuantity);
                productsData.setItemID(itemId);
                productsData.setItemIamgeURL(imageUrl);
                productsData.setItemLocation(itemLocation);

                list.add(productsData);
            } while (itemQuery.moveToNext());
        }
        itemQuery.close();
        return list;
    }

    public Items.ProductsData getItem(String itemBarCode) {
        String sql = "SELECT * FROM " + ITEM_TABLE_NAME + " WHERE ITEM_BAR_CODE_NUM = '" + itemBarCode + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor itemQuery = db.rawQuery(sql, null);
        if (itemQuery.moveToFirst()) {
            Items itemInstance = new Items();
            Items.ProductsData productsData = itemInstance.new ProductsData();

            String itemBarCodeNo = itemQuery.getString(itemQuery.getColumnIndex("ITEM_BAR_CODE_NUM"));
            String itemArticleid = itemQuery.getString(itemQuery.getColumnIndex("ITEM_ARTICLE_ID"));
            String itemName = itemQuery.getString(itemQuery.getColumnIndex("ITEM_NAME"));
            float itemPriceperquantity = itemQuery.getFloat(itemQuery.getColumnIndex("ITEM_PRICE"));
            int itemQuantity = itemQuery.getInt(itemQuery.getColumnIndex("ITEM_QUANTITY"));
            String itemDescription = itemQuery.getString(itemQuery.getColumnIndex("ITEM_DESCRIPTION"));
            int itemId=itemQuery.getInt(itemQuery.getColumnIndex("ITEM_ID"));
            String itemLocation=itemQuery.getString(itemQuery.getColumnIndex("ITEM_ADDRESS"));
            String imageUrl= itemQuery.getString(itemQuery.getColumnIndex("ITEM_IMAGE_URL"));
            productsData.setItemName(itemName);
            productsData.setItemArticleID(itemArticleid);
            productsData.setBarCodeNumber(itemBarCodeNo);
            productsData.setItemDescription(itemDescription);
            productsData.setItemPrice(itemPriceperquantity);
            productsData.setItemQuantity(itemQuantity);
            productsData.setItemID(itemId);
            productsData.setItemIamgeURL(imageUrl);
            productsData.setItemLocation(itemLocation);
            return productsData;
        }
        itemQuery.close();
        return null;
    }
//****************************************************Favourite Item Cart*************************************************//

    public long inSertIntoFavCart(String itemName, String image,float itemPrice,String description,String location,int ItemId,int count){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT * FROM " + FAV_CART + " WHERE ITEM_ID = '" + ItemId + "'";
        Cursor cursor = db.rawQuery(sql,null);
        ContentValues values= new ContentValues();
        long result = 0;
        if(cursor.moveToFirst()){
                int qauntity = cursor.getInt(cursor.getColumnIndex("ITEM_COUNT")) + count;
                values.put("ITEM_NAME",itemName);
                values.put("ITEM_PRICE",itemPrice);
                values.put("ITEM_IMAGE_URL",image);
                values.put("ITEM_DESCRIPTION",description);
                values.put("ITEM_ADDRESS",location);
                values.put("ITEM_COUNT",qauntity);
               // values.put("ITEM_ID",ItemId);
            db.beginTransaction();
            try{
               result= db.update(FAV_CART,values,null,null);
            }catch (SQLException e){
                e.printStackTrace();
            }finally {
                db.endTransaction();
                // End the transaction.
                db.close();
                // Close database
                return result;
            }

        }else{
            db.beginTransaction();
            try{
                values.put("ITEM_NAME",itemName);
                values.put("ITEM_PRICE",itemPrice);
                values.put("ITEM_IMAGE_URL",image);
                values.put("ITEM_DESCRIPTION",description);
                values.put("ITEM_ADDRESS",location);
                values.put("ITEM_COUNT",count);
                values.put("ITEM_ID",ItemId);
                result= db.insert(FAV_CART,null,values);
                db.setTransactionSuccessful();
            }catch (SQLiteException e){
                e.printStackTrace();
            }
            finally {
                db.endTransaction();
                // End the transaction.
                db.close();
                // Close database
                return result;
            }
        }
    }

    public List<Items.ProductsData> getItemFromCart(){
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + FAV_CART;
        Cursor cursor = db.rawQuery(sql,null);
        ArrayList<Items.ProductsData> cart= new ArrayList<>();
        if(cursor.moveToFirst()){
            do{
                Items items = new Items();
                Items.ProductsData productsData = items.new ProductsData();
                String name = cursor.getString(cursor.getColumnIndex("ITEM_NAME"));
                String image = cursor.getString(cursor.getColumnIndex("ITEM_IMAGE_URL"));
                float price = cursor.getFloat(cursor.getColumnIndex("ITEM_PRICE"));
                String location= cursor.getString(cursor.getColumnIndex("ITEM_ADDRESS"));
                String description = cursor.getString(cursor.getColumnIndex("ITEM_DESCRIPTION"));
                int itemId = cursor.getInt(cursor.getColumnIndex("ITEM_ID"));
                int count = cursor.getInt(cursor.getColumnIndex("ITEM_COUNT"));
                productsData.setItemName(name);
                productsData.setItemPrice(price);
                productsData.setItemIamgeURL(image);
                productsData.setItemDescription(description);
                productsData.setItemLocation(location);
                productsData.setItemID(itemId);
                productsData.setItemQuantity(count);
                cart.add(productsData);
            }while (cursor.moveToNext());
        }

        return cart;
    }



    public int removeItem(int itemId){
        SQLiteDatabase database = this.getWritableDatabase();
        return database.delete(FAV_CART,"ITEM_ID = ?",new String[]{String.valueOf(itemId)});
    }
/*********************************************USER TABLE********************************************************************/

    public long insertIntoUserTable(String username,String email,String mobile,int isLogIn) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.beginTransaction();
        long result = 0;
        try {
            ContentValues values = new ContentValues();
            values.put("USER_NAME", username);
            values.put("USER_EMAIL", email);
            values.put("USER_MOBILE", mobile);
            values.put("USER_LOGGED_IN", isLogIn);
            result = database.insert(USER_TABLE, null, values);
            database.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            database.endTransaction();
            // End the transaction.
            database.close();
            // Close database
            return result;
        }
    }

    public int isUserLoggedIn(){
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + USER_TABLE ;
        Cursor cursor = db.rawQuery(sql,null);
        if(cursor.moveToFirst()){
            int loggedin= cursor.getInt(cursor.getColumnIndex("USER_LOGGED_IN"));
            db.close();
            cursor.close();
            return  loggedin;
        }else{
            db.close();
            cursor.close();
            return 0;
        }

    }

    public UserDetails.UserData getUserdata(){
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + USER_TABLE;
        Cursor cursor = db.rawQuery(sql,null);
        if(cursor.moveToFirst()){
            UserDetails user= new UserDetails();
            UserDetails.UserData data= user.new UserData();
            String name= cursor.getString(cursor.getColumnIndex("USER_NAME"));
            String email = cursor.getString(cursor.getColumnIndex("USER_EMAIL"));
            data.setEmailID(email);
            data.setUserName(name);
            return data;
        }else{
            return null;
        }
    }

}