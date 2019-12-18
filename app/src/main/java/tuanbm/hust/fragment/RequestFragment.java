package tuanbm.hust.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import tuanbm.hust.R;
import tuanbm.hust.activity.LoginActivity;
import tuanbm.hust.base.BaseFragment;

public class RequestFragment extends BaseFragment {
    TextView tvLogin;

    @Override
    protected void setupUI() {
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
    }

    @Override
    protected void findViewById(View view) {
        tvLogin = view.findViewById(R.id.tvLogin);
    }

    @Override
    protected int getLayoutInflaterId() {
        return R.layout.fragment_request;
    }
}
