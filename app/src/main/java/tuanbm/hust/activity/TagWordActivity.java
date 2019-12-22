package tuanbm.hust.activity;

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
import tuanbm.hust.adapter.WordAdapter;
import tuanbm.hust.base.BaseActivity;
import tuanbm.hust.listener.RecyclerViewItemClickListener;
import tuanbm.hust.object.TagWordPost;
import tuanbm.hust.object.Word;
import tuanbm.hust.service.RetrofitService;
import tuanbm.hust.utils.Constant;
import tuanbm.hust.utils.SharedPreferencesSingleton;

public class TagWordActivity extends BaseActivity {
    TextView tvTagName;
    RecyclerView rvWordList;
    private List<Word> mWords;
    private WordAdapter mWordAdapter;
    private Call<List<List<Word>>> wordsCallback;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_tag_word;
    }

    @Override
    protected void findViewById() {
        tvTagName = findViewById(R.id.tvTagName);
        rvWordList = findViewById(R.id.rv_word_list);
    }

    @Override
    protected void setupUI() {
        Intent intent = getIntent();
        String nameTag = intent.getStringExtra(Constant.TAG);
        tvTagName.setText(nameTag);

        wordsCallback = new Call<List<List<Word>>>() {
            @Override
            public Response<List<List<Word>>> execute() throws IOException {
                return null;
            }

            @Override
            public void enqueue(Callback<List<List<Word>>> callback) {

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
            public Call<List<List<Word>>> clone() {
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
        TagWordPost tagWordPost = new TagWordPost("en_vi", nameTag);
        String token = SharedPreferencesSingleton.getInstance().get(Constant.LOGIN_TOKEN, String.class);
        wordsCallback = service.getWords("Bearer " + token, tagWordPost);

        wordsCallback.enqueue(new Callback<List<List<Word>>>() {
            @Override
            public void onResponse(@NotNull Call<List<List<Word>>> call, @NotNull Response<List<List<Word>>> response) {
                if(null != response.body()){
                    mWords.clear();
                    for (List<Word> lsWord: response.body()) {
                        mWords.add(lsWord.get(0));
                    }
                    List<Word> newLs = new ArrayList<>(mWords);
                    mWordAdapter.updateData(newLs);
                } else {
                    Toast.makeText(TagWordActivity.this, "There's an error!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<List<List<Word>>> call, @NotNull Throwable t) {
                Toast.makeText(TagWordActivity.this, "Server error!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setupRecyclerView() {
        mWords = new ArrayList<>();
        mWordAdapter = new WordAdapter(TagWordActivity.this, mWords);
        rvWordList.setAdapter(mWordAdapter);
        rvWordList.setLayoutManager(new LinearLayoutManager(TagWordActivity.this));
        rvWordList.addOnItemTouchListener(new RecyclerViewItemClickListener(TagWordActivity.this, rvWordList,
                new RecyclerViewItemClickListener.OnItemClickListener() {
                    @Override
                    public void OnItemClick(View view, int position) {
                        Word word = mWords.get(position);
                        String keyAndType = getKeyAndType(word);

                        StringBuilder traits = new StringBuilder();
                        for (String trait: word.getTrait())
                            traits.append(trait).append("\n");
                        String traitList = traits.toString();
                        StringBuilder meanings = new StringBuilder();

                        for (String meaning: word.getMeaning())
                            meanings.append(" - ").append(meaning).append("\n");
                        String meaningList = meanings.toString();
                        //Call intent and transfer data
                        Intent intent = new Intent(TagWordActivity.this, WordDetailActivity.class);
                        intent.putExtra(Constant.KEY, keyAndType);
                        intent.putExtra(Constant.WORD_ID, word.getId());
                        intent.putExtra(Constant.WORD, word.getKey());
                        intent.putExtra(Constant.TRAITS, traitList);
                        intent.putExtra(Constant.MEANING, meaningList);
                        startActivity(intent);
                    }

                    @Override
                    public void OnItemLongClick(View view, int position) {
                    }
                }));
    }

    private String getKeyAndType(Word word){
        String s = word.getKey();
        if (!word.getType().equals("")) {
            String type = word.getType().replaceAll("ừ", "ừ, ");
            int len = type.length();
            if (len > 0 && type.charAt(len - 1) == ' ')
                type = type.substring(0, len-2);
            s += " (" + type + ")";
        }
        return s;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(TagWordActivity.this, MainActivity.class);
        intent.putExtra(Constant.SELECTED_FRAGMENT, 0);
        startActivity(intent);
    }
}
