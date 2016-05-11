package com.jianhui_zhu.openuseragent.util.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;

import com.jianhui_zhu.openuseragent.R;
import com.jianhui_zhu.openuseragent.util.FragmenUtil;
import com.jianhui_zhu.openuseragent.util.LocalDatabaseSingleton;
import com.jianhui_zhu.openuseragent.view.HomeView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by jianhuizhu on 2016-01-27
 */
public class MainActivity extends Activity {
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
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPostResume() {
		super.onPostResume();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
