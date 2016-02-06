package com.jianhui_zhu.openuseragent.util;

import android.app.Activity;
import android.os.Bundle;

import com.squareup.otto.Bus;


/**
 * Created by jianhuizhu on 2016-01-27
 */
public class AbstractActivity extends Activity {
//	private ObjectGraph mObjectGraph;

//	@Inject
	protected Bus mEventBus = new Bus();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		mObjectGraph = ObjectGraph.create(((AbstractApplication)getApplication()).getApplicationGraph());
//		mObjectGraph.inject(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPostResume() {
		super.onPostResume();
		mEventBus.register(this);

	}

	@Override
	protected void onPause() {
		super.onPause();
		mEventBus.unregister(this);
	}
}
