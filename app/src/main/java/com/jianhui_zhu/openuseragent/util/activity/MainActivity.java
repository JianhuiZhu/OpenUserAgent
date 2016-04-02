package com.jianhui_zhu.openuseragent.util.activity;

import android.os.Bundle;

import com.jianhui_zhu.openuseragent.R;
import com.jianhui_zhu.openuseragent.util.FragmenUtil;
import com.jianhui_zhu.openuseragent.view.HomeView;

/**
 * Created by jianhuizhu on 2016-01-27
 */
public class MainActivity extends AbstractActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
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
}
