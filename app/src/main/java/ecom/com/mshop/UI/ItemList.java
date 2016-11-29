package ecom.com.mshop.UI;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.content.res.Resources;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import ecom.com.mshop.Adapters.ItemlistAdapter;
import ecom.com.mshop.Adapters.RecyclerItemClickListener;
import ecom.com.mshop.Database.DBHelper;
import ecom.com.mshop.Database.Items;
import ecom.com.mshop.R;
import ecom.com.mshop.Utils.ItemClickListener;
import ecom.com.mshop.Utils.ProductDetail;

/**
 * Created by Pandey on 20-11-2016.
 */
public class ItemList extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ItemlistAdapter itemlistAdapter;
    private ArrayList<Items.ProductsData> productsDatas;
    DBHelper dbHelper;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);
        dbHelper = new DBHelper(this);
        recyclerView=(RecyclerView)findViewById(R.id.itemListView);
        productsDatas= dbHelper.getItemsList();
        itemlistAdapter = new ItemlistAdapter(this,R.layout.content_product_list,productsDatas);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(itemlistAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Items.ProductsData productsData = productsDatas.get(position);
                String barCodeNum=productsData.getBarCodeNumber();
                Intent intent = new Intent(ItemList.this,ItemDetails.class);
                intent.putExtra("barCodeNumber",barCodeNum);
                startActivity(intent);
            }
        }));

    }


}
