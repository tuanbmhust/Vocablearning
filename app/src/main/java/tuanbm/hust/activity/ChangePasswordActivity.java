package tuanbm.hust.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tuanbm.hust.R;
import tuanbm.hust.base.BaseActivity;
import tuanbm.hust.fragment.ProfileFragment;
import tuanbm.hust.object.ChangePwdUser;
import tuanbm.hust.object.LoginToken;
import tuanbm.hust.service.RetrofitService;
import tuanbm.hust.utils.Constant;
import tuanbm.hust.utils.SharedPreferencesSingleton;

public class ChangePasswordActivity extends BaseActivity {
    EditText etCurrPwd;
    EditText etNewPwd;
    EditText etNewPwdConfirm;
    Button btnChange;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_change_password;
    }

    @Override
    protected void findViewById() {
        etCurrPwd = findViewById(R.id.etOldPassword);
        etNewPwd = findViewById(R.id.etNewPassword);
        etNewPwdConfirm = findViewById(R.id.etNewPassConfirm);
        btnChange = findViewById(R.id.btnChange);
    }

    @Override
    protected void setupUI() {
        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get current password
                String currPwd = SharedPreferencesSingleton.getInstance().get(Constant.PASSWORD, String.class);

                //Check if type-in password is satisfied
                if (etCurrPwd.getText().toString().equals("") ||
                    etNewPwd.getText().toString().equals("") ||
                    etNewPwdConfirm.getText().toString().equals("")){
                    Toast.makeText(ChangePasswordActivity.this, "Please fill in all fields!", Toast.LENGTH_SHORT).show();
                } else if(etCurrPwd.getText().toString().equals(etNewPwd.getText().toString())){
                    Toast.makeText(ChangePasswordActivity.this, "New password cannot be the same with old one!", Toast.LENGTH_SHORT).show();
                } else if (!currPwd.equals(etCurrPwd.getText().toString())) {
                    Toast.makeText(ChangePasswordActivity.this, "Wrong current password!", Toast.LENGTH_SHORT).show();
                } else if (!etNewPwd.getText().toString().equals(etNewPwdConfirm.getText().toString())) {
                    Toast.makeText(ChangePasswordActivity.this, "Password and Confirmation must be the same!", Toast.LENGTH_SHORT).show();
                } else {
                    //Change password successfully
                    changePassword (currPwd,
                                    etNewPwd.getText().toString(),
                                    SharedPreferencesSingleton.getInstance().get(Constant.USER_EMAIL, String.class),
                                    SharedPreferencesSingleton.getInstance().get(Constant.LOGIN_TOKEN, String.class));
                }
            }
        });
    }

    void changePassword(String oldPwd, final String newPwd, String email, String token){
        Toast.makeText(this, "Changing...", Toast.LENGTH_SHORT).show();

        //Connect to server using Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Constant.API)
                .build();

        ChangePwdUser changePwdUser = new ChangePwdUser(oldPwd, newPwd, email);
        RetrofitService service = retrofit.create(RetrofitService.class);
        Call<LoginToken> tokenCall = service.re_pass("Bearer " + token, changePwdUser);

        tokenCall.enqueue(new Callback<LoginToken>() {
            @Override
            public void onResponse(Call<LoginToken> call, Response<LoginToken> response) {
                if(null != response.body()){
                    if(response.body().getSuccess() == true){
                        Toast.makeText(ChangePasswordActivity.this, "Password successfully changed!", Toast.LENGTH_SHORT).show();
                        SharedPreferencesSingleton.getInstance().put(Constant.PASSWORD, newPwd);
                        Intent intent = new Intent(ChangePasswordActivity.this, MainActivity.class);
                        startActivity(intent);
                        ChangePasswordActivity.this.finish();
                    }
                } else {
                    Toast.makeText(ChangePasswordActivity.this, "Server error!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginToken> call, Throwable t) {
                Toast.makeText(ChangePasswordActivity.this, "Server error!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ChangePasswordActivity.this, MainActivity.class);
        startActivity(intent);
        ChangePasswordActivity.this.finish();
    }
}
