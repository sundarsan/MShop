package ecom.com.mshop.UI;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
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
    private List<Items.ProductsData> cart;
    private DBHelper dbHelper;
    private int size=0;
    private int total=0;
    private SecureRandom random;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        random = new SecureRandom();
        dbHelper = new DBHelper(this);
        cart = dbHelper.getItemFromCart();
        setContentView(R.layout.activity_items);
        recyclerView = (RecyclerView) findViewById(R.id.itemListView);
        if(!cart.isEmpty()){
            cartlistAdapter = new CartlistAdapter(this, cart);
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
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
            int size =cart.size();
            int cart_count=size;
            float sum =0;
            while(size>0){
                Items.ProductsData items = cart.get(position);
                float cost = items.getItemQuantity() * items.getItemPrice();
                sum = sum + cost;
                size--;
            }
            String random=nextSessionId();
            Intent intent = new Intent(this,CheckOut.class);
            intent.putExtra("coupon",random);
            intent.putExtra("total",String.valueOf(sum));
            intent.putExtra("count",String.valueOf(cart_count));

            startActivity(intent);
        }else if(id== R.id.deleteItem){
            dbHelper.removeItem(cart.get(position).getItemID());
            cart= dbHelper.getItemFromCart();
            if(!cart.isEmpty()){
                cartlistAdapter = new CartlistAdapter(this, cart);
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
        if(cart.size()==0){
            Intent intent = new Intent(this,EmptyCart.class);
            startActivity(intent);
            Toast.makeText(this,"Your Cart is empty!Please order.",Toast.LENGTH_LONG).show();
        }
    }


        public String nextSessionId() {
            return new BigInteger(130, random).toString(32);
        }
}
