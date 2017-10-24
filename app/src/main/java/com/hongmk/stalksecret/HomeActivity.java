package com.hongmk.stalksecret;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.hongmk.stalksecret.fragment.HomeListFragment;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentStatePagerItemAdapter;

public class HomeActivity extends AppCompatActivity {

    Intent intent;
    //아래 Stirng 값들이 가지는 것은 단순한 0,1,3...등의 인덱스가 아닌 고유한 객체의 값임
    //다른데서 가져다쓸때는 MainActivity.tab10()[index] 로 가져올 수 있음
    public static int[] tab10() {
        return new int[] {
                R.string.home_tab_1,
                R.string.home_tab_2,
                R.string.home_tab_3,
                R.string.home_tab_4,
                R.string.home_tab_5
        };
    }

    private Toolbar toolbar;
    private ViewPager viewPager;
    private SmartTabLayout viewPagerTab;
    private FragmentPagerItems pages;
    //FragmentPagerItemAdapter 사용 시 첫 생성 할 때 Tab1, Tab2에 모두 position 0으로 생성하므로 FragmentStatePagerItemAdapter 사용함.
    private FragmentStatePagerItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //toolbar.setTitle(R.string.demo_title_indicator_trick1); //툴바 제목 표시여부
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);  //왼쪽상단 BackKey 표시여부

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPagerTab = (SmartTabLayout) findViewById(R.id.viewpagertab);

        pages = new FragmentPagerItems(this);

        for (int titleResId : tab10()) {
            //pages.add(FragmentPagerItem.of(getString(titleResId), DemoFragment.class));
            pages.add(FragmentPagerItem.of(getString(titleResId), HomeListFragment.class));
            Log.i("[SH]", "CREATE POSITION:"+ titleResId);
        }

        adapter = new FragmentStatePagerItemAdapter(getSupportFragmentManager(), pages);

        viewPager.setAdapter(adapter);
        viewPagerTab.setViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.home_menu_mypage) {
            intent = new Intent(HomeActivity.this, MypageActivity.class);
            startActivity(intent);
        } else if(item.getItemId() == R.id.home_menu_notice) {
            intent = new Intent(HomeActivity.this, NoticeActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public void createContentClick(View view){
        intent = new Intent(HomeActivity.this, CreateBoardActivity.class);
        startActivity(intent);
    }

}
