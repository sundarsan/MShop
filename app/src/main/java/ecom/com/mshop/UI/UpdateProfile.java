package ecom.com.mshop.UI;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import ecom.com.mshop.API.ApiClient;
import ecom.com.mshop.API.MshopApi;
import ecom.com.mshop.R;
import ecom.com.mshop.Utils.RegisteredUser;
import ecom.com.mshop.Utils.UserUpdated;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Pandey on 21-11-2016.
 */
public class UpdateProfile extends AppCompatActivity {

    private Button signUpButton;
    private TextView loginLink;
    private ProgressDialog progressDialog;
    private EditText _nameText;
    private EditText _memberIDText;
    private EditText _emailText;
    private EditText _mobileText;
    private EditText _reEnterPasswordText;
    private EditText _passwordText;
    private MshopApi apiService;
    public static final String MyPREFERENCES = "MyPrefs";
    private static final String TAG = UpdateProfile.class.getSimpleName();
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        signUpButton = (Button) findViewById(R.id.btn_signup);
        signUpButton.setText("Update Profile");
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });
        apiService = ApiClient.getClient().create(MshopApi.class);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        loginLink = (TextView) findViewById(R.id.link_login);
        loginLink.setText("");
        _nameText = (EditText) findViewById(R.id.input_name);
        _memberIDText = (EditText) findViewById(R.id.input_memberID);
        _emailText = (EditText) findViewById(R.id.input_email);
        _mobileText = (EditText) findViewById(R.id.input_mobile);
        _passwordText = (EditText) findViewById(R.id.input_password);
        _reEnterPasswordText = (EditText) findViewById(R.id.input_reEnterPassword);
        String name = sharedpreferences.getString("name", "");
        String memberId = sharedpreferences.getString("memberId", "");
        String email = sharedpreferences.getString("email", "");
        String password = sharedpreferences.getString("password", "");
        String mobile = sharedpreferences.getString("mobile", "");
        _nameText.setText(name);
        _memberIDText.setText(memberId);
        _emailText.setText(email);
        _mobileText.setText(mobile);
        _passwordText.setText(password);

    }

    private void signup() {


        progressDialog = new ProgressDialog(UpdateProfile.this,
                R.style.AppTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String name = _nameText.getText().toString();
        String memberId = _memberIDText.getText().toString();
        String email = _emailText.getText().toString();
        String mobile = _mobileText.getText().toString();
        String password = _passwordText.getText().toString();

        Call<UserUpdated> call = apiService.updateUser(name, memberId, mobile, email);
        call.enqueue(new Callback<UserUpdated>() {
            @Override
            public void onResponse(Call<UserUpdated> call, Response<UserUpdated> response) {
                UserUpdated updatedUser = response.body();
                String responString = updatedUser.getMessage();
                if (responString.contains("User updation done successfully")) {
                    progressDialog.dismiss();
                    Intent intent = new Intent(UpdateProfile.this,HomeScreen.class);
                    startActivity(intent);
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(UpdateProfile.this, "User not Updated!Try again", Toast.LENGTH_LONG).show();
                }
                Log.d(TAG, responString);

            }

            @Override
            public void onFailure(Call<UserUpdated> call, Throwable t) {
                Log.d(TAG, t.getMessage());

            }
        });
    }
}
