package com.hao.summaryproject;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RadioButton;

import com.hao.summaryproject.base.BaseActivity;
import com.hao.summaryproject.ui.find.FindFragment;
import com.hao.summaryproject.ui.home.HomeFragment;
import com.hao.summaryproject.ui.product.ProductFragment;
import com.hao.summaryproject.ui.user.UserFragment;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements View.OnClickListener{

    @BindView(R.id.rb_home)
    RadioButton rbHome;
    @BindView(R.id.rb_find)
    RadioButton rbFind;
    @BindView(R.id.rb_product)
    RadioButton rbProduct;
    @BindView(R.id.rb_user)
    RadioButton rbUser;

    private HomeFragment homeFragment;

    private FindFragment findFragment;

    private ProductFragment productFragment;

    private UserFragment userFragment;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        super.initView();

        showHomeFragment();
    }

    /**
     * 显示首页
     */
    private void showHomeFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        hideAllFragment(transaction);
        if (homeFragment == null) {
            homeFragment = new HomeFragment();
            transaction.add(R.id.main_container, homeFragment);
        }
        showAndCommitFragment(transaction, homeFragment);

    }

    /**
     * 显示找款页面
     */
    private void showFindFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        hideAllFragment(transaction);
        if (findFragment == null) {
            findFragment = new FindFragment();
            transaction.add(R.id.main_container, findFragment);
        }
        showAndCommitFragment(transaction, findFragment);
    }

    /**
     * 显示生产页面
     */
    private void showProductFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        hideAllFragment(transaction);
        if (productFragment == null) {
            productFragment = new ProductFragment();
            transaction.add(R.id.main_container, productFragment);
        }
        showAndCommitFragment(transaction, productFragment);
    }

    /**
     * 显示我的界面
     */
    private void showUserFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        hideAllFragment(transaction);
        if (userFragment == null) {
            userFragment = new UserFragment();
            transaction.add(R.id.main_container, userFragment);
        }
        showAndCommitFragment(transaction, userFragment);
    }

    /**
     * 显示并提交fragment
     * @param transaction
     * @param fragment
     */
    private void showAndCommitFragment(FragmentTransaction transaction, Fragment fragment) {
        transaction.show(fragment);
        transaction.commit();
    }

    /**
     * 先隐藏所有的fragment
     * @param transaction
     */
    private void hideAllFragment(FragmentTransaction transaction) {
        hideFragment(transaction, homeFragment);
        hideFragment(transaction, findFragment);
        hideFragment(transaction, productFragment);
        hideFragment(transaction, userFragment);
    }

    /**
     * 隐藏单个fragment
     * @param transaction
     * @param fragment
     */
    private void hideFragment(FragmentTransaction transaction, Fragment fragment) {
        if (fragment != null) {
            transaction.hide(fragment);
        }
    }

    @OnClick({R.id.rb_home, R.id.rb_find, R.id.rb_product, R.id.rb_user})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rb_home:
                showHomeFragment();
                break;
            case R.id.rb_find:
                showFindFragment();
                break;
            case R.id.rb_product:
                showProductFragment();
                break;
            case R.id.rb_user:
                showUserFragment();
                break;
        }
    }
}
