package tuanbm.hust.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tuanbm.hust.R;
import tuanbm.hust.activity.ChangePasswordActivity;
import tuanbm.hust.activity.LoginActivity;
import tuanbm.hust.activity.MainActivity;
import tuanbm.hust.base.BaseFragment;
import tuanbm.hust.object.LoginToken;
import tuanbm.hust.service.RetrofitService;
import tuanbm.hust.utils.Constant;
import tuanbm.hust.utils.SharedPreferencesSingleton;

public class ProfileFragment extends BaseFragment {
    TextView tvEmail;
    Button btnChangePassword;
    Button btnLogout;

    @Override
    protected void setupUI() {
        String emailBox = "Welcome, " + SharedPreferencesSingleton.getInstance().get(Constant.USER_EMAIL,String.class);
        tvEmail.setText(emailBox);
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChangePasswordActivity.class);
                startActivity(intent);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout(SharedPreferencesSingleton.getInstance().get(Constant.LOGIN_TOKEN,String.class));
            }
        });
    }

    @Override
    protected void findViewById(View view) {
        tvEmail = view.findViewById(R.id.tv_email);
        btnChangePassword = view.findViewById(R.id.btn_change_password);
        btnLogout = view.findViewById(R.id.btn_log_out);
    }

    @Override
    protected int getLayoutInflaterId() {
        return R.layout.fragment_profile;
    }

    void logout(String token){
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Constant.API)
                .build();
        RetrofitService service = retrofit.create(RetrofitService.class);
        Call<LoginToken> loginTokenCall = service.logout("Bearer " + token);
        loginTokenCall.enqueue(new Callback<LoginToken>() {
            @Override
            public void onResponse(Call<LoginToken> call, Response<LoginToken> response) {
                Toast.makeText(getActivity(), "Logged out", Toast.LENGTH_SHORT).show();
                SharedPreferencesSingleton.getInstance().put(Constant.LOG_IN_STATE, false);
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }

            @Override
            public void onFailure(Call<LoginToken> call, Throwable t) {
                Toast.makeText(getActivity(), "Server error!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
