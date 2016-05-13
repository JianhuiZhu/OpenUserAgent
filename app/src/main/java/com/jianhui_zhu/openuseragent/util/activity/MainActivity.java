package com.jianhui_zhu.openuseragent.util.activity;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.FragmentActivity;

import com.github.ikidou.fragmentBackHandler.BackHandlerHelper;
import com.jianhui_zhu.openuseragent.R;
import com.jianhui_zhu.openuseragent.util.FragmenUtil;
import com.jianhui_zhu.openuseragent.view.HomeView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by jianhuizhu on 2016-01-27
 */
public class MainActivity extends FragmentActivity {
	@Bind(R.id.container)
	CoordinatorLayout container;
	public CoordinatorLayout getContainer(){
		return container;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.bind(this);
		FragmenUtil.switchToFragment(this, HomeView.newInstance());
	}

	@Override
	public void onBackPressed() {
		if (!BackHandlerHelper.handleBackPress(this)) {
			super.onBackPressed();
		}
	}
}
