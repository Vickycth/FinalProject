package com.example.android.finalproject;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        RadioGroup.OnCheckedChangeListener,
        ViewPager.OnPageChangeListener {

    private ViewPager viewPager;
    private RadioGroup radioGroup;
    private List<Fragment> listFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InitView();
        // switch to default page when app start.
        radioGroup.check(R.id.rb_cats);
    }

    private void InitView() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        radioGroup = (RadioGroup) findViewById(R.id.rg_menu_bar);

        // create all fragments。 TODO: add FragmentCats, FragmentLike.
        // 这里三个Fragment都是用的FragmentAbout。
        listFragment = new ArrayList<>();
        listFragment.add(new FragmentCats());
        listFragment.add(new FragmentLike());
        listFragment.add(new FragmentAbout());

        //
        // RadioGroup Listener说明：
        // 这里的代码写法是：MainActivity类中实现RadioGroup.OnCheckedChangeListener，
        // OnCheckedChangeListener中只有一个方法，就是onCheckedChanged(RadioGroup group, int checkedId)
        // radioGroup.setOnCheckedChangeListener(this)这一句是给radioGroup控件加上Listener，这样的话，
        // RadioGroup中按钮选中后，就会调用MainActivity类中的onCheckedChanged()方法。
        //
        // ViewPager控件的OnPageChangeListener类似。
        //
        // ViewPager控件的adapter
        // FragmentPagerAdapter是将Fragment和ViewPager关联起来的适配器，用来告诉ViewPager一共有几个
        // Fragment（每个Fragment是一个页面内容），给ViewPager返回指定的Fragment。
        //
        //

        // RadioGroup Listener
        radioGroup.setOnCheckedChangeListener(this);
        // ViewPager listener
        viewPager.addOnPageChangeListener(this);
        // ViewPager adapter
        MyViewPagerAdapter adapter = new MyViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
    }

    //
    // this method implement RadioGroup OnCheckedChangeListener.
    //

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        // Sync current page after radio button check state changed.
        // RadioGroup中选中不同的RadioButton按钮后，同步设置ViewPager显示对应的Fragment。
        // viewPager.setCurrentItem(n)后，ViewPager会通过适配器取到对应的Fragment，然后显示它。
        switch (checkedId) {
            case R.id.rb_cats:
                viewPager.setCurrentItem(0);
                break;
            case R.id.rb_like:
                viewPager.setCurrentItem(1);
                break;
            case R.id.rb_about:
                viewPager.setCurrentItem(2);
                break;
        }
    }

    //
    // Next three methods implement ViewPager OnPageChangeListener:
    //   onPageSelected(int position)
    //   onPageScrolled(int position,float positionOffset, int positionOffsetPixels)
    //   onPageScrollStateChanged(int state)
    //

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        // Sync radio button check state after page changed.
        // radioGroup.check(radioGroup.getChildAt(position).getId());
        // ViewPager支持手指左右滑动。这个事件在滑动结束后触发，同步设置RadioGroup中选中对应的按钮。
        switch (position) {
            case 0:
                radioGroup.check(R.id.rb_cats);
                break;
            case 1:
                radioGroup.check(R.id.rb_like);
                break;
            case 2:
                radioGroup.check(R.id.rb_about);
                break;
            default:
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    //
    //  ViewPager Adapter link pager with fragment.
    //

    public class MyViewPagerAdapter extends FragmentPagerAdapter {
        public MyViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        //返回每个position对应的Fragment对象
        @Override
        public Fragment getItem(int position) {
            return listFragment.get(position);
        }

        //返回list的长度，也就是Fragment对象的个数
        @Override
        public int getCount() {
            return listFragment.size();
        }
    }
}
