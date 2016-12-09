package ecom.com.mshop.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import ecom.com.mshop.R;

/**
 * Created by Pandey on 25-11-2016.
 */
public class CheckOut extends AppCompatActivity {
    private TextView price,quantity,finalprice;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        price= (TextView) findViewById(R.id.chk_price_button);
        quantity=(TextView)findViewById(R.id.chk_total_items);
        finalprice=(TextView)findViewById(R.id.final_price);
        Intent intent = getIntent();
        float total= Float.valueOf(intent.getStringExtra("total"));
        float finalPrice= total - (total/5);
        String count =  intent.getStringExtra("count");
        price.setText("$ " + String.valueOf(total));
        quantity.setText(count);
        finalprice.setText("$" + String.valueOf(finalPrice));
    }
}
