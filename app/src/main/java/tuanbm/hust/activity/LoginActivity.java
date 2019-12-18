package tuanbm.hust.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tuanbm.hust.R;
import tuanbm.hust.base.BaseActivity;
import tuanbm.hust.object.LoginToken;
import tuanbm.hust.object.LoginUser;
import tuanbm.hust.service.RetrofitService;
import tuanbm.hust.utils.Constant;
import tuanbm.hust.utils.SharedPreferencesSingleton;

public class LoginActivity extends BaseActivity {
    EditText etEmail;
    EditText etPassword;
    Button btnLogin;
    TextView tvCreateAcc;


    @Override
    protected void setupUI() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etEmail.getText().toString().equals("") || etPassword.getText().toString().equals("")){
                    Toast.makeText(LoginActivity.this, "Email or Password cannot be blanked!", Toast.LENGTH_SHORT).show();
                }
                else login(etEmail.getText().toString(), etPassword.getText().toString());
            }
        });

        tvCreateAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
                LoginActivity.this.finish();
            }
        });
    }

    @Override
    protected void findViewById() {
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvCreateAcc = findViewById(R.id.tvCreateAccount);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    private void login(final String email, final String password) {
        Toast.makeText(LoginActivity.this, "Logging in...", Toast.LENGTH_LONG).show();

        //Connect to server to login using Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Constant.API)
                .build();
        RetrofitService service = retrofit.create(RetrofitService.class);
        LoginUser user = new LoginUser(email, password, true);
        Call<LoginToken> loginTokenCall = service.login(user);
        loginTokenCall.enqueue(new Callback<LoginToken>() {
            @Override
            public void onResponse(Call<LoginToken> call, Response<LoginToken> response) {
                if (response.body() != null) {
                    if (response.body().getMessage().equals("")) {
                        Toast.makeText(LoginActivity.this, "Login successfully", Toast.LENGTH_LONG).show();
                        SharedPreferencesSingleton.getInstance().put(Constant.LOGIN_TOKEN, response.body().getAccessToken());
                        SharedPreferencesSingleton.getInstance().put(Constant.LOG_IN_STATE, true);
                        SharedPreferencesSingleton.getInstance().put(Constant.USER_EMAIL, email);
                        SharedPreferencesSingleton.getInstance().put(Constant.PASSWORD, password);
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        LoginActivity.this.finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Wrong password or the account is not exist!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Wrong password or the account is not exist!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginToken> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Server error!", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        LoginActivity.this.finish();
    }

}
