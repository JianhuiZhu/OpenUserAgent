package com.jianhui_zhu.openuseragent;

import android.app.Activity;
import android.os.Bundle;

import de.greenrobot.event.EventBus;

/**
 * Created by jianhuizhu on 2016-01-27
 */
public class AbstractActivity extends Activity {
//	private ObjectGraph mObjectGraph;

//	@Inject
	protected EventBus mEventBus;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mEventBus = EventBus.getDefault();
//		mObjectGraph = ObjectGraph.create(((AbstractApplication)getApplication()).getApplicationGraph());
//		mObjectGraph.inject(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

//	@Override
//	protected void onPostResume() {
//		super.onPostResume();
//		mEventBus.register(this);
//
//	}
//
//	@Override
//	protected void onPause() {
//		super.onPause();
//		mEventBus.unregister(this);
//	}
}
