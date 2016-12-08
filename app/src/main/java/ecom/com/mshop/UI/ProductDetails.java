package ecom.com.mshop.UI;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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


    private ImageView itemImage,addItem,removeItem;
    private TextView itemname,itemPrice,itemDescription,address,countItem;
    private Button addtocart;
    private DBHelper db;
    private LinearLayout linearLayout;
    private final Handler handler = new Handler();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_details);
        final Items item = (Items) getIntent().getSerializableExtra("productDetail");
        final Items.ProductsData items = item.getProductsData();
        db= new DBHelper(this);
        addItem=(ImageView)findViewById(R.id.addItem);
        removeItem=(ImageView)findViewById(R.id.removeItem);
        itemImage=(ImageView)findViewById(R.id.item_imageView);
        addItem=(ImageView)findViewById(R.id.addItem);
        removeItem=(ImageView)findViewById(R.id.removeItem);
        countItem=(TextView)findViewById(R.id.countItem);
        itemname=(TextView)findViewById(R.id.product_name);
        address=(TextView)findViewById(R.id.prod_address);
        itemDescription=(TextView)findViewById(R.id.item_name);
        itemPrice=(TextView)findViewById(R.id.prod_price);
        addtocart=(Button)findViewById(R.id.add_to_cart);
        Glide.with(this).load(items.getItemIamgeURL()).into(itemImage);
        itemname.setText(items.getItemName());
        address.setText(items.getItemLocation());
        itemDescription.setText(items.getItemDescription());
        itemPrice.setText(String.valueOf(items.getItemPrice()));
        String price= "$"+" " + items.getItemPrice();
        final int count= Integer.valueOf(countItem.getText().toString());
        itemPrice.setText(price);
        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        int newcount= Integer.valueOf(countItem.getText().toString()) +1;
                        countItem.setText(String.valueOf(newcount));
                    }
                });
            }
        });


        removeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.post(new Runnable() {
                    int local= Integer.valueOf(countItem.getText().toString());
                    @Override
                    public void run() {
                        if(local>1){
                            int newcount=local-1;
                            countItem.setText(String.valueOf(newcount));
                        }else{
                            countItem.setText("1");
                        }
                    }
                });

            }
        });

        addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                long res=db.inSertIntoFavCart(items.getItemName(),items.getItemIamgeURL(),items.getItemPrice(),items.getItemDescription(),items.getItemLocation(),items.getItemID(),Integer.valueOf(countItem.getText().toString()));
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
