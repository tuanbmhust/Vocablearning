package tuanbm.hust.fragment;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.internal.EverythingIsNonNull;
import tuanbm.hust.R;
import tuanbm.hust.activity.WordDetailActivity;
import tuanbm.hust.adapter.WordAdapter;
import tuanbm.hust.base.BaseFragment;
import tuanbm.hust.listener.RecyclerViewItemClickListener;
import tuanbm.hust.object.Word;
import tuanbm.hust.service.RetrofitService;
import tuanbm.hust.utils.Constant;

public class SearchFragment extends BaseFragment {
    private static final String TAG = "hehehe";
    private FloatingSearchView searchView;
    private List<Word> mWords;
    private RecyclerView rvWords;
    private WordAdapter mWordAdapter;
    private TextView tvAlt;
    private Call<List<Word>> wordsCallback;

    @Override
    protected void setupUI() {
        tvAlt.setVisibility(View.VISIBLE);
        wordsCallback = new Call<List<Word>>() {
            @Override
            public Response<List<Word>> execute() throws IOException {
                return null;
            }

            @Override
            public void enqueue(Callback<List<Word>> callback) {

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
            public Call<List<Word>> clone() {
                return null;
            }

            @Override
            public Request request() {
                return null;
            }
        };

        setupRecyclerView();

        searchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {
            }

            @Override
            public void onSearchAction(String currentQuery) {
                searchView.showProgress();
                getSearchedResults(currentQuery, true);
            }
        });

        searchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, String newQuery) {
                searchView.showProgress();
                wordsCallback.cancel();
                if (newQuery.equals("")) {
                    mWords.clear();
                    mWordAdapter.updateData(mWords);
                    searchView.hideProgress();
                    tvAlt.setVisibility(View.VISIBLE);
                } else {
                    getSearchedResults(newQuery, false);
                }
            }
        });
    }

    private void getSearchedResults(String query, final boolean isSearchBehavior){
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Constant.API)
                .build();
        RetrofitService service = retrofit.create(RetrofitService.class);
        wordsCallback = service.getAutoComplete(query);
        wordsCallback.enqueue(new Callback<List<Word>>() {
            @EverythingIsNonNull
            @Override
            public void onResponse(Call<List<Word>> call, Response<List<Word>> response) {
                tvAlt.setVisibility(View.INVISIBLE);
                mWords.clear();
                mWords = response.body();
                if (mWords.size() == 0 && isSearchBehavior) {
                    Toast.makeText(getContext(), "Cannot find this word!", Toast.LENGTH_SHORT).show();
                    tvAlt.setVisibility(View.VISIBLE);
                }
                searchView.hideProgress();
                mWordAdapter.updateData(mWords);
            }

            @EverythingIsNonNull
            @Override
            public void onFailure(Call<List<Word>> call, Throwable t) {
                if (isSearchBehavior) {
                    Toast.makeText(getContext(),
                            "Server error!", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
                        String keyAndType = getKeyAndType(word);

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
    protected void findViewById(View view) {
        searchView = view.findViewById(R.id.floating_search_view);
        rvWords = view.findViewById(R.id.rv_word_list);
        tvAlt = view.findViewById(R.id.tvAlt);
    }

    @Override
    protected int getLayoutInflaterId() {
        return R.layout.fragment_search;
    }
}
