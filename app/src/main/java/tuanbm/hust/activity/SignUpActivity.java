package tuanbm.hust.activity;

import android.content.Intent;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tuanbm.hust.R;
import tuanbm.hust.base.BaseActivity;
import tuanbm.hust.object.LoginToken;
import tuanbm.hust.object.SignupResponse;
import tuanbm.hust.object.SignupUser;
import tuanbm.hust.service.RetrofitService;
import tuanbm.hust.utils.Constant;

public class SignUpActivity extends BaseActivity {
    EditText etEmail;
    EditText etUsername;
    EditText etPassword;
    EditText etPasswordConfirmation;
    Button btnSignUp;

    @Override
    protected void setupUI() {
        //Handle type in
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etEmail.getText().toString().equals("") ||
                    etUsername.getText().toString().equals("") ||
                    etPassword.getText().toString().equals("") ||
                    etPasswordConfirmation.getText().toString().equals("")
                ){
                    Toast.makeText(SignUpActivity.this, "Please fill in all fields!", Toast.LENGTH_SHORT).show();
                } else if (!etPassword.getText().toString().equals(etPasswordConfirmation.getText().toString())){
                    Toast.makeText(SignUpActivity.this, "Password and Comfirmation must be the same!", Toast.LENGTH_SHORT).show();
                } else if (!isEmailValid(etEmail.getText().toString())){
                    Toast.makeText(SignUpActivity.this, "Invalid email!", Toast.LENGTH_SHORT).show();
                } else{
                    createAccount(
                            etUsername.getText().toString(),
                            etEmail.getText().toString(),
                            etPassword.getText().toString(),
                            etPasswordConfirmation.getText().toString()
                            );
                }
            }
        });
    }

    @Override
    protected void findViewById() {
        etEmail = findViewById(R.id.etEmail);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etPasswordConfirmation = findViewById(R.id.etPasswordConfirmation);
        btnSignUp = findViewById(R.id.btnSignUp);
    }

    boolean isEmailValid(CharSequence email){
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_signup;
    }

    private void createAccount(String username, String email, String password, String passwordConfirmation){
        SignupUser user = new SignupUser(username,email,password,passwordConfirmation);

        //Connect to server
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Constant.API)
                .build();

        RetrofitService service = retrofit.create(RetrofitService.class);
        Call<SignupResponse> tokenCall = service.signup(user);

        //On response
        tokenCall.enqueue(new Callback<SignupResponse>() {
            @Override
            public void onResponse(@NotNull Call<SignupResponse> call, @NotNull Response<SignupResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getMessage().equals("Successfully created user!")) {
                            Toast.makeText(
                                    SignUpActivity.this,
                                    "Sign Up successfully!\nNow you can login to your new account!",
                                    Toast.LENGTH_SHORT)
                                    .show();
                            Intent intent = new Intent(SignUpActivity.this,
                                    MainActivity.class);
                            startActivity(intent);
                            SignUpActivity.this.finish();
                        } else {
                            Toast.makeText(SignUpActivity.this,
                                    "This email has already been taken", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(SignUpActivity.this,
                            "This email has already been taken", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<SignupResponse> call, @NotNull Throwable t) {
                Toast.makeText(SignUpActivity.this, "Server did not response", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
        startActivity(intent);
        SignUpActivity.this.finish();
    }
}
