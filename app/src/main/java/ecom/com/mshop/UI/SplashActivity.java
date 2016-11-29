package ecom.com.mshop.UI;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.ramotion.paperonboarding.PaperOnboardingEngine;
import com.ramotion.paperonboarding.PaperOnboardingPage;
import com.ramotion.paperonboarding.listeners.PaperOnboardingOnChangeListener;
import com.ramotion.paperonboarding.listeners.PaperOnboardingOnRightOutListener;

import java.util.ArrayList;

import ecom.com.mshop.Database.DBHelper;
import ecom.com.mshop.R;
import ecom.com.mshop.Utils.UserDetails;

public class SplashActivity extends AppCompatActivity {
    private  DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        dbHelper = new DBHelper(this);

        if(dbHelper.isUserLoggedIn()==1){
            UserDetails.UserData data = dbHelper.getUserdata();
            Intent intent = new Intent(this,HomeScreen.class);
            intent.putExtra("name",data.getUserName());
            intent.putExtra("email",data.getEmailID());
            startActivity(intent);
            finish();
        }else {
//            Intent loginIntent = new Intent(getApplicationContext() , LoginActivity.class);
//            startActivity(loginIntent);
//            finish();
//        }

            PaperOnboardingEngine engine = new PaperOnboardingEngine(findViewById(R.id.onboardingRootView), getDataForOnboarding(), getApplicationContext());

            engine.setOnChangeListener(new PaperOnboardingOnChangeListener() {
                @Override
                public void onPageChanged(int oldElementIndex, int newElementIndex) {
                    Toast.makeText(getApplicationContext(), "Swiped from " + oldElementIndex + " to " + newElementIndex, Toast.LENGTH_SHORT).show();

                }
            });

            engine.setOnRightOutListener(new PaperOnboardingOnRightOutListener() {
                @Override
                public void onRightOut() {
                    // Probably here will be your exit action
                    Toast.makeText(getApplicationContext(), "Swiped out right", Toast.LENGTH_SHORT).show();
                    Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(loginIntent);

                }
            });
        }
    }


    // Just example data for Onboarding
    private ArrayList<PaperOnboardingPage> getDataForOnboarding() {
        // prepare data
        PaperOnboardingPage scr1 = new PaperOnboardingPage("Scan", "Scan the barcode of items for easy checkout",
                Color.parseColor("#90B928"), R.drawable.ic_scan_barcode, R.drawable.ic_key_scanner_icon);
        PaperOnboardingPage scr2 = new PaperOnboardingPage("View", "View the listed items in a elegant grid view before buying",
                Color.parseColor("#FF4B50"), R.drawable.stores, R.drawable.wallet);
        PaperOnboardingPage scr3 = new PaperOnboardingPage("Cart", "Check the list of things before you proceed to payment",
                Color.parseColor("#03438E"), R.drawable.ic_bank_scanner_icon, R.drawable.shopping_cart);

        ArrayList<PaperOnboardingPage> elements = new ArrayList<>();
        elements.add(scr1);
        elements.add(scr2);
        elements.add(scr3);
        return elements;
    }
}
