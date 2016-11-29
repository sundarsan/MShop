package ecom.com.mshop.UI;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.math.BigInteger;
import java.util.ArrayList;

import ecom.com.mshop.API.ApiClient;
import ecom.com.mshop.API.MshopApi;
import ecom.com.mshop.Adapters.CarouselImageAdapter;
import ecom.com.mshop.Adapters.GridViewAdapter;
import ecom.com.mshop.Database.DBHelper;
import ecom.com.mshop.Database.Items;
import ecom.com.mshop.R;
import ecom.com.mshop.Utils.CirclePageIndicator;
import ecom.com.mshop.Utils.PageIndicator;
import ecom.com.mshop.Utils.ProductDetail;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeScreen extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ViewPager mViewPager;
    private Intent intent;
    CarouselImageAdapter mCarouselImageAdapter;
    private int[] bannerImage = {
            R.drawable.carousel1,
            R.drawable.carousel2,
            R.drawable.carousel3,
            R.drawable.carousel4,
            R.drawable.carousel5
    };

    String[] gridViewString = {
            "cold_drink", "milk", "rice", "snacks", "spices",
            "noodles", "peanuts", "ketchup", "peanuts",
            "ice_cream", "cutlery", "bank", "rice", "ice", "milk",

    } ;
    int[] gridViewImageId = new int[]{
            R.drawable.ic_coldrink_scanner_icon,R.drawable.ic_milk_scanner_icon,R.drawable.ic_rice_scanner_icon,R.drawable.ic_snacks_scanner_icon,
            R.drawable.ic_spices_scanner_icon,R.drawable.ic_noodles_scanner_icon,R.drawable.ic_ketchup_scanner_icon,R.drawable.ic_peanuts_scanner_icon,
            R.drawable.ic_ice_cream_scanner_icon,R.drawable.ic_vegetables_scanner_icon,R.drawable.ic_key_scanner_icon,R.drawable.ic_hotels_scanner_icon,
            R.drawable.ic_cutlery_scanner_icon,R.drawable.ic_bank_scanner_icon ,R.drawable.ic_rice_scanner_icon,R.drawable.ic_ice_cream_scanner_icon
    };




    private DrawerLayout drawer;
    PageIndicator mIndicator;
    private GridView gridView;
    private TextView username,email;

    private NavigationView navigationView;
    private DBHelper dbHelper;
    private ActionBarDrawerToggle toggle;
    private FloatingActionButton fab;
    private MshopApi mshopApi;
    private ProgressDialog dialog;
    private final static String TAG=HomeScreen.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dbHelper = new DBHelper(this);
        mshopApi= ApiClient.getClient().create(MshopApi.class);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        dialog = new ProgressDialog(this);
        navigationView = (NavigationView) findViewById(R.id.nav_view_home);
        navigationView.setNavigationItemSelectedListener(this);
        View header=navigationView.getHeaderView(0);
        username=(TextView)header.findViewById(R.id.nav_username_home);
        email=(TextView)header.findViewById(R.id.nav_email_home);
        intent = getIntent();
        String name=intent.getStringExtra("name");
        String useremail= intent.getStringExtra("email");
        username.setText(name);
        email.setText(useremail);
        populateView();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                dialog.setMessage("Loading.... ");
                dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                dialog.setIndeterminate(true);
                dialog.show();
                Call<ProductDetail> call = mshopApi.getAllProduct();
                call.enqueue(new Callback<ProductDetail>() {
                    @Override
                    public void onResponse(Call<ProductDetail> call, Response<ProductDetail> response) {
                        ArrayList<ProductDetail.ProductsData> productLists = response.body().getProductsData();
                        int size=0;
                        while(size!=productLists.size()){
                            String barCodeNumber=productLists.get(size).getBarCodeNumber();
                            String itemArticleID=productLists.get(size).getItemArticleID();
                            String []itemName=productLists.get(size).getItemDescription().split(" ");
                            float itemPrice=productLists.get(size).getItemPrice();
                            int itemId= productLists.get(size).getItemID();
                            int itemQuantity=productLists.get(size).getItemQuantity();
                            String itemDescription=productLists.get(size).getItemDescription();
                            String prodName= itemName[0] + itemName[1];
                            String imageUrl = productLists.get(size).getItemIamgeURL();
                            String location = productLists.get(size).getItemLocation();
                            dbHelper.insertItemIntoSQLTable(itemId,barCodeNumber,itemArticleID,prodName,itemPrice,itemQuantity,itemDescription,location,imageUrl);
                            size++;
                        }
                        Intent intent = new Intent(HomeScreen.this, ItemList.class);
                        startActivity(intent);

                    }

                    @Override
                    public void onFailure(Call<ProductDetail> call, Throwable t) {
                        Log.d(TAG,t.getMessage());
                    }
                });
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        dialog.dismiss();
    }

    @Override
    protected  void onStart(){
        super.onStart();
        String name=dbHelper.getUserdata().getUserName();
        String useremail=dbHelper.getUserdata().getEmailID();
        username.setText(name);
        email.setText(useremail);
    }
    @Override
    protected void onStop() {
        super.onStop();
        dialog.dismiss();
    }


    private void populateView() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mViewPager = (ViewPager) findViewById(R.id.view_pager);
                mCarouselImageAdapter = new CarouselImageAdapter(HomeScreen.this,bannerImage);
                mViewPager.setAdapter(mCarouselImageAdapter);
                mIndicator = (CirclePageIndicator) findViewById(R.id.indicator);

                mIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {

                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {
                        if (state == ViewPager.SCROLL_STATE_IDLE) {

                        }
                    }
                });
                mIndicator.setViewPager(mViewPager);
                gridView = (GridView) findViewById(R.id.gridview);
                gridView.setAdapter(new GridViewAdapter(HomeScreen.this , gridViewString , gridViewImageId));

            }
        });
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_myOrders:
                if (dbHelper.getItemFromCart().size() != 0) {
                    Intent intent = new Intent(this, CartItems.class);
                    startActivity(intent);
                    return true;
                } else {
                    Toast.makeText(this, "No Items in the Cart!Please add.", Toast.LENGTH_LONG).show();
                    Intent intent1 = new Intent(this,EmptyCart.class);
                    startActivity(intent1);
                    return true;
                }

            case R.id.action_updateProfile:
                Intent intent= new Intent(this,UpdateProfile.class);
                startActivity(intent);
            case R.id.action_logout:
                Intent intent1= new Intent(this,LoginActivity.class);
                startActivity(intent1);
            default:
                return super.onOptionsItemSelected(item);

        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        addActionsToControls();
        String name=dbHelper.getUserdata().getUserName();
        String useremail= dbHelper.getUserdata().getEmailID();
        username.setText(name);
        email.setText(useremail);
    }

    private void addActionsToControls () {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchBarCodeScanningScreen();
            }
        });

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent intent = new Intent(this,HomeScreen.class);
            startActivity(intent);

        } else if (id == R.id.nav_cart) {
            if(dbHelper.getItemFromCart().size()!=0){
                Intent intent = new Intent(this,CartItems.class);
                startActivity(intent);
            }else{
                Toast.makeText(this,"Item Cart is Empty!Please shop first",Toast.LENGTH_LONG).show();
                Intent intent1 = new Intent(this,EmptyCart.class);
                startActivity(intent1);
            }

        } else if (id == R.id.nav_update) {
            Intent intent= new Intent(this,UpdateProfile.class);
            startActivity(intent);

        }  else if (id == R.id.nav_logout) {
            Intent intent= new Intent(this,LoginActivity.class);
            startActivity(intent);

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        final IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null) {
            // handle scan result
            Log.v("BarcodeActivity Result", scanResult.getContents());
            Call<Items> call = mshopApi.getProduct(scanResult.getContents());
            call.enqueue(new Callback<Items>() {
                @Override
                public void onResponse(Call<Items> call, Response<Items> response) {
                    Items items = response.body();
                    Intent itemDetailScreenIntent = new Intent(getApplicationContext() , ProductDetails.class);
                    itemDetailScreenIntent.putExtra("productDetail",items);
                    startActivity(itemDetailScreenIntent);
                    Log.d("Check this succes","Please check this Login success");
                }

                @Override
                public void onFailure(Call<Items> call, Throwable t) {

                }
            });

        } else {
            // else continue with any other code you need in the method
            Log.v("BarcodeActivity", "No result");
        }
    }

    private void launchBarCodeScanningScreen () {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        integrator.setPrompt("Scan a barcode");
        integrator.setCameraId(0);  // Use a specific camera of the device
        integrator.setBeepEnabled(false);
        integrator.initiateScan();
    }

}
