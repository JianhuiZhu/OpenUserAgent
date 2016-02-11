package com.jianhui_zhu.openuseragent.activity;

import android.os.Bundle;

import com.crashlytics.android.Crashlytics;
import com.jianhui_zhu.openuseragent.R;
import com.jianhui_zhu.openuseragent.util.AbstractActivity;
import com.jianhui_zhu.openuseragent.view.HomeView;
import com.jianhui_zhu.openuseragent.util.FragmenUtil;
import io.fabric.sdk.android.Fabric;

/**
 * Created by jianhuizhu on 2016-01-27
 */
public class MainActivity extends AbstractActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Fabric.with(this, new Crashlytics());
		setContentView(R.layout.activity_main);
		FragmenUtil.switchToFragment(this, new HomeView());

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
