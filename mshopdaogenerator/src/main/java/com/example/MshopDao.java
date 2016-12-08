package com.example;

import java.io.IOException;

import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.ToMany;


public class MshopDao {
    private static final String OUT_DIR = "../app/src/main/java";
    public static void main(String [] args) throws IOException {

        Schema schema = new Schema(1,"ecom.com.mshop.Dao");
        addTables(schema);
        try {
            new DaoGenerator().generateAll(schema,OUT_DIR);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addTables(Schema schema) {

        Entity user = schema.addEntity("User");
        user.addStringProperty("userName");
        user.addStringProperty("userEmail");
        user.addStringProperty("userMobile");
        user.addIntProperty("logStatus");

        Entity cart = schema.addEntity("Cart_Item");
        cart.addStringProperty("itemName");
        cart.addStringProperty("itemPrice");
        cart.addStringProperty("itemDescription");
        cart.addStringProperty("itemAddress");
        cart.addStringProperty("itemImage");
        cart.addIntProperty("itemCount");
        cart.addIntProperty("itemId");

        Entity items = schema.addEntity("Items");
        items.addStringProperty("itemName");
        items.addStringProperty("itemPrice");
        items.addStringProperty("itemDescription");
        items.addStringProperty("itemAddress");
        items.addStringProperty("itemImage");
        items.addStringProperty("itemArticleId");
        items.addIntProperty("itemCount");
        items.addIntProperty("itemId");


    }


}
