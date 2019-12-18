package tuanbm.hust.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tuanbm.hust.R;
import tuanbm.hust.activity.WordDetailActivity;
import tuanbm.hust.adapter.WordAdapter;
import tuanbm.hust.base.BaseFragment;
import tuanbm.hust.listener.RecyclerViewItemClickListener;
import tuanbm.hust.object.LoginToken;
import tuanbm.hust.object.ResponseTags;
import tuanbm.hust.object.ResponseWords;
import tuanbm.hust.object.Word;
import tuanbm.hust.service.RetrofitService;
import tuanbm.hust.utils.Constant;
import tuanbm.hust.utils.SharedPreferencesSingleton;

public class VocabFragment extends BaseFragment {
    private RecyclerView rvWords;
    List<Word> mWords;
    WordAdapter mWordAdapter;

    @Override
    protected void setupUI() {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Constant.API)
                .build();

        RetrofitService service = retrofit.create(RetrofitService.class);
        String token = "Bearer " + SharedPreferencesSingleton.getInstance().get(Constant.LOGIN_TOKEN, String.class);
        Call<JsonObject> call = service.getUserWord(token);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NotNull Call<JsonObject> call, @NotNull Response<JsonObject> response) {
                String s = "";
            }

            @Override
            public void onFailure(@NotNull Call<JsonObject> call, @NotNull Throwable t) {
                String s = "";
            }
        });

//        call.enqueue(new Callback<ResponseWords>() {
//            @Override
//            public void onResponse(@NotNull Call<ResponseWords> call, @NotNull Response<ResponseWords> response) {
//                try {
//                    if(response.body().isMesNullOrEmpty()){
//                        mWords.addAll(response.body().getWords());
//                        mWordAdapter.updateData(mWords);
//                    } else{
//                        Toast.makeText(getContext(), "You have no word marked", Toast.LENGTH_SHORT).show();
//                    }
//
//                } catch (NullPointerException e){
//                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseWords> call, Throwable t) {
//                Toast.makeText(getContext(), "Server error!", Toast.LENGTH_SHORT).show();
//            }
//        });

        setupRecyclerView();
    }

    @Override
    protected void findViewById(View view) {
        rvWords = view.findViewById(R.id.rvUserWord);
    }

    @Override
    protected int getLayoutInflaterId() {
        return R.layout.fragment_vocab;
    }

    private void setupRecyclerView() {
        mWords = new ArrayList<>();
        mWordAdapter = new WordAdapter(getContext(), mWords);
        rvWords.setAdapter(mWordAdapter);
        rvWords.setLayoutManager(new LinearLayoutManager(getContext()));
        rvWords.addOnItemTouchListener(new RecyclerViewItemClickListener(getContext(), rvWords,
                new RecyclerViewItemClickListener.OnItemClickListener() {
                    @Override
                    public void OnItemClick(View view, int position) {
                        Intent intent = new Intent(getActivity(), WordDetailActivity.class);
                        Word word = mWords.get(position);
                        String keyAndType = word.getKey();
                        if (!word.getType().equals("")) {
                            String type = word.getType().replaceAll("ừ", "ừ, ");
                            int len = type.length();
                            if (len > 0 && type.charAt(len - 1) == ' ')
                                type = type.substring(0, len-2);
                            keyAndType += " (" + type + ")";
                        }

                        StringBuilder traits = new StringBuilder();
                        for (String trait: word.getTrait())
                            traits.append(trait).append("\n");
                        String traitList = traits.toString();
                        StringBuilder meanings = new StringBuilder();

                        for (String meaning: word.getMeaning())
                            meanings.append(" - ").append(meaning).append("\n");
                        String meaningList = meanings.toString();

                        intent.putExtra(Constant.KEY, keyAndType);
                        intent.putExtra(Constant.WORD_ID, word.getId());
                        intent.putExtra(Constant.WORD, word.getKey());
                        intent.putExtra(Constant.TRAITS, traitList);
                        intent.putExtra(Constant.MEANING, meaningList);
                        startActivity(intent);
                        getActivity().finish();
                    }

                    @Override
                    public void OnItemLongClick(View view, int position) {

                    }
                }));
    }
}
