package tuanbm.hust.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tuanbm.hust.R;
import tuanbm.hust.activity.TagWordActivity;
import tuanbm.hust.activity.WordDetailActivity;
import tuanbm.hust.adapter.TagAdapter;
import tuanbm.hust.base.BaseFragment;
import tuanbm.hust.listener.RecyclerViewItemClickListener;
import tuanbm.hust.object.ResponseTags;
import tuanbm.hust.service.RetrofitService;
import tuanbm.hust.utils.Constant;
import tuanbm.hust.utils.SharedPreferencesSingleton;

public class VocabFragment extends BaseFragment {
    private static final String TAG = "VOCAB_FRAGMENT";
    private TextView tvAlt;
    private RecyclerView rvTags;
    private List<String> mTags;
    private TagAdapter mTagAdapter;
    private Call<ResponseTags> tagsCallback;

    @Override
    protected void setupUI() {
        tvAlt.setVisibility(View.VISIBLE);
        tagsCallback = new Call<ResponseTags>() {
            @Override
            public Response<ResponseTags> execute() throws IOException {
                return null;
            }

            @Override
            public void enqueue(Callback<ResponseTags> callback) {

            }

            @Override
            public boolean isExecuted() {
                return false;
            }

            @Override
            public void cancel() {

            }

            @Override
            public boolean isCanceled() {
                return false;
            }

            @Override
            public Call<ResponseTags> clone() {
                return null;
            }

            @Override
            public Request request() {
                return null;
            }
        };

        setupRecyclerView();

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Constant.API)
                .build();
        RetrofitService service = retrofit.create(RetrofitService.class);
        tagsCallback = service.getTags("Bearer " + SharedPreferencesSingleton.getInstance().get(Constant.LOGIN_TOKEN,String.class));
        tagsCallback.enqueue(new Callback<ResponseTags>() {
            @Override
            public void onResponse(@NotNull Call<ResponseTags> call, @NotNull Response<ResponseTags> response) {
                if(null != response.body()){
                    if(null != response.body().getMes()){
                        Toast.makeText(getContext(), "Error!", Toast.LENGTH_SHORT).show();
                    } else{
                        tvAlt.setVisibility(View.INVISIBLE);
                        mTags = response.body().getTags();
                        mTagAdapter.updateData(mTags);
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseTags> call, @NotNull Throwable t) {
                Toast.makeText(getContext(), "Server error!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void findViewById(View view) {
        tvAlt = view.findViewById(R.id.tvAlt);
        rvTags = view.findViewById(R.id.rvUserTags);
    }

    @Override
    protected int getLayoutInflaterId() {
        return R.layout.fragment_vocab;
    }

    private void setupRecyclerView() {
        mTags = new ArrayList<>();
        mTagAdapter = new TagAdapter(getContext(), mTags);
        rvTags.setAdapter(mTagAdapter);
        rvTags.setLayoutManager(new LinearLayoutManager(getContext()));
        rvTags.addOnItemTouchListener(new RecyclerViewItemClickListener(getContext(), rvTags,
                new RecyclerViewItemClickListener.OnItemClickListener() {
                    @Override
                    public void OnItemClick(View view, int position) {
                        String tag = mTags.get(position);
                        //On tag click, showing list of words in TAG field
                        Intent intent = new Intent(getActivity(), TagWordActivity.class);
                        intent.putExtra(Constant.TAG,tag);
                        startActivity(intent);
                        getActivity().finish();
                    }

                    @Override
                    public void OnItemLongClick(View view, int position) {

                    }
                }));
    }

}
