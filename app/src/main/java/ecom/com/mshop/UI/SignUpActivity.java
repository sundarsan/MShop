package ecom.com.mshop.UI;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ecom.com.mshop.API.ApiClient;
import ecom.com.mshop.API.MshopApi;
import ecom.com.mshop.R;
import ecom.com.mshop.Utils.ProductDetail;
import ecom.com.mshop.Utils.RegisteredUser;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends Activity {

    private Button signUpButton ;
    private TextView loginLink;
    private ProgressDialog progressDialog;
    private EditText _nameText;
    private EditText _memberIDText;
    private EditText _emailText;
    private EditText _mobileText;
    private EditText _reEnterPasswordText;
    private EditText _passwordText;
    private MshopApi apiService;
    private static final String TAG = SignUpActivity.class.getSimpleName();
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signUpButton = (Button) findViewById(R.id.btn_signup);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });
        apiService= ApiClient.getClient().create(MshopApi.class);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        loginLink = (TextView) findViewById(R.id.link_login);
        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        _nameText = (EditText) findViewById(R.id.input_name);
        _memberIDText = (EditText) findViewById(R.id.input_memberID);
        _emailText = (EditText) findViewById(R.id.input_email);
        _mobileText = (EditText) findViewById(R.id.input_mobile);
        _passwordText = (EditText) findViewById(R.id.input_password);
        _reEnterPasswordText = (EditText) findViewById(R.id.input_reEnterPassword);
    }

    public void signup() {

        if (!validate()) {
            onSignupFailed();
            return;
        }

        signUpButton.setEnabled(false);

        progressDialog = new ProgressDialog(SignUpActivity.this,
                R.style.AppTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String name = _nameText.getText().toString();
        String memberId = _memberIDText.getText().toString();
        String email = _emailText.getText().toString();
        String mobile = _mobileText.getText().toString();
        String password = _passwordText.getText().toString();
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("name",name);
        editor.putString("memberId",memberId);
        editor.putString("email",email);
        editor.putString("mobile",mobile);
        editor.putString("password",password);
        editor.commit();



        // TODO: Implement your own signup logic here.
        Call<RegisteredUser> call= apiService.regUser(name,memberId,mobile,password,email);
        call.enqueue(new Callback<RegisteredUser>() {
            @Override
            public void onResponse(Call<RegisteredUser> call, Response<RegisteredUser> response) {
                RegisteredUser registeredUser = response.body();
                String responString = registeredUser.getMessage();
                if(responString.contains("User registration done successfully")){
                    onSignupSuccess();
                }else{
                    onSignupFailed();
                }
                Log.d(TAG, responString);

            }

            @Override
            public void onFailure(Call<RegisteredUser> call, Throwable t) {
                Log.d(TAG,t.getMessage());
                onSignupFailed();
            }
        });

    }


    public void onSignupSuccess() {
        signUpButton.setEnabled(true);
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        progressDialog.dismiss();
        signUpButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String address = _memberIDText.getText().toString();
        String email = _emailText.getText().toString();
        String mobile = _mobileText.getText().toString();
        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (address.isEmpty()) {
            _memberIDText.setError("Enter Valid memberID");
            valid = false;
        } else {
            _memberIDText.setError(null);
        }


        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (mobile.isEmpty() || mobile.length()!=10) {
            _mobileText.setError("Enter Valid Mobile Number");
            valid = false;
        } else {
            _mobileText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || reEnterPassword.length() > 10 || !(reEnterPassword.equals(password))) {
            _reEnterPasswordText.setError("Password Do not match");
            valid = false;
        } else {
            _reEnterPasswordText.setError(null);
        }

        return valid;
    }

}
