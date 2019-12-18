package tuanbm.hust.activity;

import android.content.Intent;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import tuanbm.hust.R;
import tuanbm.hust.base.BaseActivity;
import tuanbm.hust.utils.Constant;

public class WordDetailActivity extends BaseActivity {
    TextView tvKeyAndItsType;
    TextView tvTraits;
    TextView tvMeaning;
    private Toolbar toolbar;
    String word;
    String wordId;

    @Override
    protected void setupUI() {
        Intent intent = getIntent();
        word = intent.getStringExtra(Constant.WORD);
        wordId = intent.getStringExtra(Constant.WORD_ID);
        tvKeyAndItsType.setText(intent.getStringExtra(Constant.KEY));
        tvTraits.setText(intent.getStringExtra(Constant.TRAITS));
        tvMeaning.setText(intent.getStringExtra(Constant.MEANING));
        tvMeaning.setMovementMethod(new ScrollingMovementMethod());

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(intent.getStringExtra(Constant.WORD));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    protected void findViewById() {
        toolbar = findViewById(R.id.toolbar);
        tvKeyAndItsType = findViewById(R.id.tv_key_type);
        tvTraits = findViewById(R.id.tv_traits);
        tvMeaning = findViewById(R.id.tv_meaning);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_word_detail;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(WordDetailActivity.this, MainActivity.class);
        startActivity(intent);
        WordDetailActivity.this.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    //Handle action when tapping on star button on toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_favorite) {
            Intent intent = new Intent(WordDetailActivity.this, MarkWordActivity.class);
            intent.putExtra(Constant.WORD_ID, wordId);
            intent.putExtra(Constant.WORD, word);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
