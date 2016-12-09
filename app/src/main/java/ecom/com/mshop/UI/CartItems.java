package ecom.com.mshop.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;
import java.util.List;
import ecom.com.mshop.Adapters.CartlistAdapter;
import ecom.com.mshop.Database.DBHelper;
import ecom.com.mshop.Database.Items;
import ecom.com.mshop.R;
import java.security.SecureRandom;
import java.math.BigInteger;

/**
 * Created by Pandey on 21-11-2016.
 */
public class CartItems extends AppCompatActivity implements CartlistAdapter.OnSharingOptionSelected {
    private RecyclerView recyclerView;
    private CartlistAdapter cartlistAdapter;
    private DBHelper dbHelper;
    private SecureRandom random;
    private List<Object> itemsCart;
    private String coupon;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        random = new SecureRandom();
        dbHelper = new DBHelper(this);
        itemsCart = dbHelper.getItemFromCart();
        setContentView(R.layout.activity_items);
        recyclerView = (RecyclerView) findViewById(R.id.itemListView);
        coupon=nextSessionId();
        if(!itemsCart.isEmpty()){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    int size =itemsCart.size()-1;
                    int cart_count=itemsCart.size();
                    float cost=0;
                    float sum =0;
                    while(size>=0){
                        Items.ProductsData items = (Items.ProductsData) itemsCart.get(size);
                        cost = items.getItemQuantity() * items.getItemPrice();
                        sum = sum + cost;
                        size--;
                    }
                    CartCheckOut cartCheckOut = new CartCheckOut(cart_count,sum);
                    itemsCart.add(cartCheckOut);
                }
            });

            final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            cartlistAdapter = new CartlistAdapter(this, itemsCart,coupon);
            recyclerView.setAdapter(cartlistAdapter);
        } else{
            Intent intent = new Intent(this,EmptyCart.class);
            startActivity(intent);
            Toast.makeText(this,"Your Cart is empty!Please order.",Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onOptionSelected(int id, int position) {
        if(id== R.id.checkout){
            CartCheckOut cartCheckOut = (CartCheckOut) itemsCart.get(position);
            String random=nextSessionId();
            Intent intent = new Intent(this,CheckOut.class);
            intent.putExtra("coupon",random);
            intent.putExtra("total",String.valueOf(cartCheckOut.getTotalBill()));
            intent.putExtra("count",String.valueOf(cartCheckOut.getCount()));

            startActivity(intent);
        }else if(id== R.id.deleteItem){
            Items.ProductsData items = (Items.ProductsData) itemsCart.get(position);
            int itemId = items.getItemID();
            dbHelper.removeItem(itemId);
            itemsCart= dbHelper.getItemFromCart();
            if(!itemsCart.isEmpty()){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int size =itemsCart.size()-1;
                        int cart_count=itemsCart.size();
                        float cost=0;
                        float sum =0;
                        while(size>=0){
                            Items.ProductsData items = (Items.ProductsData) itemsCart.get(size);
                            cost = items.getItemQuantity() * items.getItemPrice();
                            sum = sum + cost;
                            size--;
                        }
                        CartCheckOut cartCheckOut = new CartCheckOut(cart_count,sum);
                        itemsCart.add(cartCheckOut);
                    }
                });
                cartlistAdapter = new CartlistAdapter(this, itemsCart,coupon);
                RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(cartlistAdapter);
            } else{
                Intent intent = new Intent(this,EmptyCart.class);
                startActivity(intent);
                Toast.makeText(this,"Your Cart is empty!Please order.",Toast.LENGTH_LONG).show();
            }


            cartlistAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(itemsCart.size()==0){
            Intent intent = new Intent(this,EmptyCart.class);
            startActivity(intent);
            Toast.makeText(this,"Your Cart is empty!Please order.",Toast.LENGTH_LONG).show();
        }
    }


        public String nextSessionId() {
            return new BigInteger(130, random).toString(32).substring(0,16);
        }

    public class CartCheckOut{
        private int count;
        private float totalBill;

        public CartCheckOut(int count,float totalBill){
            this.count=count;
            this.totalBill=totalBill;
        }

        public int getCount() {
            return count;
        }


        public float getTotalBill() {
            return totalBill;
        }


    }
}
