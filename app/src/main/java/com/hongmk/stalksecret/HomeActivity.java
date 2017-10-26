package com.hongmk.stalksecret;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

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

    SwipeRefreshLayout homeSwipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //toolbar.setTitle(R.string.demo_title_indicator_trick1); //툴바 제목 표시여부
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);  //왼쪽상단 BackKey 표시여부

        viewPager = (ViewPager) findViewById(R.id.home_viewpager);
        viewPagerTab = (SmartTabLayout) findViewById(R.id.home_viewpagertab);

        pages = new FragmentPagerItems(this);

        for (int titleResId : tab10()) {
            //pages.add(FragmentPagerItem.of(getString(titleResId), DemoFragment.class));
            pages.add(FragmentPagerItem.of(getString(titleResId), HomeListFragment.class));
        }

        adapter = new FragmentStatePagerItemAdapter(getSupportFragmentManager(), pages);

        viewPager.setAdapter(adapter);
        viewPagerTab.setViewPager(viewPager);

        //viewPager에 선택, 스크롤 등 변화가 있을 때 리스너 호출됨
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position)
            {
                //최종선택된 위치를 pref변수에 항상 저장
                SharedPreferences lastTab = getSharedPreferences("LastTab", MODE_PRIVATE);
                SharedPreferences.Editor editor = lastTab.edit();
                editor.putInt("position", position);
                editor.commit();

            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        /*SwipeRefresh 와 ViewPager의 스크롤이 겹쳐서 오른쪽으로 이동할때도 Refresh가 됨.
        * 해당현상을 막기위해 아래로 스크롤(ACTION_UP)일 때만 SwipeRefreshLayout을 활성화함.
        */
        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() != MotionEvent.ACTION_UP) {
                    homeSwipe.setEnabled(false);
                } else {
                    homeSwipe.setEnabled(true);
                }
                return false;
            }
        });

        //저장된 마지막 위치를 가져와서 해당 탭이 처음보일 수 있도록함.
        SharedPreferences pref = getSharedPreferences("LastTab", Activity.MODE_PRIVATE);
        int lastTabPosition = pref.getInt("position", 0);
        viewPager.setCurrentItem(lastTabPosition);

        //리스트를 아래로 당겨 Refresh하면 Activity를 재생성하는 방식으로 진행
        homeSwipe = (SwipeRefreshLayout) findViewById(R.id.home_swipe);
        homeSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i("[SH]", "refresh!!");
                onResume(1);
                homeSwipe.setRefreshing(false);
            }
        });
    }

    //메뉴에 보일 레이아웃을 설정
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //메뉴 아이템이 클릭되면 호출됨
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

    //글작성 버튼 클릭 시 동작
    public void createContentClick(View view){
        intent = new Intent(HomeActivity.this, CreateBoardActivity.class);
        startActivity(intent);
    }

    //@Override
    protected void onResume(int resumeType){
        if(resumeType == 1) {
            Intent refresh = new Intent(this, HomeActivity.class);
            startActivity(refresh);
            this.finish();
        }
    }

    public void getContent(View view){
        //버튼이 생성될때 글의 row_id를 tag로 가지고있다가 클릭 시 불러옴
        //아래에서는 해당 row_id로 글을 불러와서 보여주도록 구현예정
        String tag = view.getTag().toString();
        Toast.makeText(this,  tag+"getContent", Toast.LENGTH_SHORT).show();
    }


}
