package com.jianhui_zhu.openuseragent.util.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.lang.Override;


/**
 * Created by jianhuizhu on 2016-01-27
 */
public abstract class AbstractActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
	protected void onPause() {
		super.onPause();
	}
}
