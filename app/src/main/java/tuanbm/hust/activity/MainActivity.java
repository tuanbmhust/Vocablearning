package tuanbm.hust.activity;

import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import tuanbm.hust.R;
import tuanbm.hust.adapter.FragmentAdapter;
import tuanbm.hust.base.BaseActivity;
import tuanbm.hust.fragment.ProfileFragment;
import tuanbm.hust.fragment.RequestFragment;
import tuanbm.hust.fragment.SearchFragment;
import tuanbm.hust.fragment.VocabFragment;
import tuanbm.hust.utils.Constant;
import tuanbm.hust.utils.SharedPreferencesSingleton;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";
    BottomNavigationView botNavView;
    ViewPager viewPager;

    @Override
    protected void findViewById() {
        botNavView = findViewById(R.id.botNavigation);
        viewPager = findViewById(R.id.viewPagerMain);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void setupUI() {
        //Check if user is logged in or not
        final boolean isLoggedIn = SharedPreferencesSingleton.getInstance().get(Constant.LOG_IN_STATE, Boolean.class);

        //Set view for fragment
        //If user is not logged in, he can only use search function
        //Otherwise, he can use all function
        botNavView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.navigation_vocab:
                        viewPager.setCurrentItem(isLoggedIn? 0: 3);
                        return true;
                    case R.id.navigation_search:
                        viewPager.setCurrentItem(1);
                        return true;
                    case R.id.navigation_profile:
                        viewPager.setCurrentItem(isLoggedIn? 2: 3);
                        return true;
                }
                return false;
            }
        });
        setupFragment(getSupportFragmentManager(), viewPager);
        botNavView.setSelectedItemId(R.id.navigation_search);
        viewPager.setCurrentItem(1);
        viewPager.setOnPageChangeListener(new PageChange());
    }

    //Set up fragment
    public static void setupFragment(FragmentManager fragmentManager, ViewPager viewPager) {
        FragmentAdapter Adapter = new FragmentAdapter(fragmentManager);
        Adapter.add(new VocabFragment(), "Vocabulary");
        Adapter.add(new SearchFragment(), "Dictionary");
        Adapter.add(new ProfileFragment(), "Profile");
        Adapter.add(new RequestFragment(), "Request Login");
        viewPager.setAdapter(Adapter);
    }

    public class PageChange implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        //Action on select fragment
        @Override
        public void onPageSelected(int position) {
            switch (position){
                case 0:
                    botNavView.setSelectedItemId(R.id.navigation_vocab);
                    break;
                case 1:
                    botNavView.setSelectedItemId(R.id.navigation_search);
                    break;
                case 2:
                    botNavView.setSelectedItemId(R.id.navigation_profile);
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

}
