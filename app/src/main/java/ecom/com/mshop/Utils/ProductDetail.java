package ecom.com.mshop.Utils;

/**
 * Created by Pandey on 16-11-2016.
 */

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class ProductDetail implements Serializable {
    private boolean status;
    private String message;

    @SerializedName("productsData")
    private ArrayList<ProductsData> productsData;


   public ProductDetail(){
       this.productsData=new ArrayList<>();
   }


    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<ProductsData> getProductsData() {
        return productsData;
    }

    public void setProductsData(ArrayList<ProductsData> productsData) {
        this.productsData = productsData;
    }

    public class ProductsData implements Serializable {
        private int itemID;
        private String barCodeNumber;
        private String itemDescription;
        private String itemArticleID;
        private float itemPrice;
        private int itemQuantity;
        private String itemIamgeURL;
        private String itemLocation;




        public String getItemDescription() {
            return itemDescription;
        }

        public void setItemDescription(String itemDescription) {
            this.itemDescription = itemDescription;
        }


        public float getItemPrice() {
            return itemPrice;
        }

        public void setItemPrice(float itemPrice) {
            this.itemPrice = itemPrice;
        }

        public int getItemQuantity() {
            return itemQuantity;
        }

        public void setItemQuantity(int itemQuantity) {
            this.itemQuantity = itemQuantity;
        }

        public String getItemIamgeURL() {
            return itemIamgeURL;
        }

        public void setItemIamgeURL(String itemIamgeURL) {
            this.itemIamgeURL = itemIamgeURL;
        }

        public String getItemLocation() {
            return itemLocation;
        }

        public void setItemLocation(String itemLocation) {
            this.itemLocation = itemLocation;
        }




        public int getItemID() {
            return itemID;
        }

        public void setItemID(int itemID) {
            this.itemID = itemID;
        }

        public String getBarCodeNumber() {
            return barCodeNumber;
        }

        public void setBarCodeNumber(String barCodeNumber) {
            this.barCodeNumber = barCodeNumber;
        }

        public String getItemArticleID() {
            return itemArticleID;
        }

        public void setItemArticleID(String itemArticleID) {
            this.itemArticleID = itemArticleID;
        }
    }
}
