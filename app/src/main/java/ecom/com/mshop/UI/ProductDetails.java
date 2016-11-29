package ecom.com.mshop.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import ecom.com.mshop.Database.DBHelper;
import ecom.com.mshop.Database.Items;
import ecom.com.mshop.R;
import ecom.com.mshop.Utils.ProductDetail;

/**
 * Created by Pandey on 24-11-2016.
 */
public class ProductDetails extends AppCompatActivity {

    private ImageView itemImage;
    private TextView itemname,itemPrice,itemDescription,address,countItem;
    private Button addtocart;
    private DBHelper db;
    private LinearLayout linearLayout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_details);
        final Items items = (Items) getIntent().getSerializableExtra("productDetail");
        db= new DBHelper(this);
        linearLayout=(LinearLayout)findViewById(R.id.add_remove);
        itemImage=(ImageView)findViewById(R.id.item_imageView);
        itemname=(TextView)findViewById(R.id.product_name);
        address=(TextView)findViewById(R.id.prod_address);
        itemDescription=(TextView)findViewById(R.id.item_name);
        itemPrice=(TextView)findViewById(R.id.prod_price);
        addtocart=(Button)findViewById(R.id.add_to_cart);
        countItem=(TextView)findViewById(R.id.countItem);
        Glide.with(this).load(items.getProductsData().getItemIamgeURL()).into(itemImage);
        itemname.setText(items.getProductsData().getItemName());
        address.setText(items.getProductsData().getItemLocation());
        itemDescription.setText(items.getProductsData().getItemDescription());
        itemPrice.setText(String.valueOf(items.getProductsData().getItemPrice()));
        String price= "$"+" " + items.getProductsData().getItemPrice();
        linearLayout.setVisibility(View.INVISIBLE);
        itemPrice.setText(price);
        addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                long res=db.inSertIntoFavCart(items.getProductsData().getItemName(),items.getProductsData().getItemIamgeURL(),items.getProductsData().getItemPrice(),items.getProductsData().getItemDescription(),items.getProductsData().getItemLocation(),items.getProductsData().getItemID(),Integer.valueOf(countItem.getText().toString()));
                if(res==0){
                    Log.d("ItemDetails","Failed Transaction");
                }else{
                    Intent intent= new Intent(ProductDetails.this,HomeScreen.class);
                    startActivity(intent);
                    Toast.makeText(ProductDetails.this,"Transaction Successfull",Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}
