package com.jianhui_zhu.openuseragent.util;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;import java.lang.Override;

//import de.greenrobot.event.EventBus;


/**
 * Created by jianhuizhu on 2016-01-27
 */
public class AbstractFragment extends Fragment {

//	@Inject
	//protected EventBus mEventBus;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//mEventBus = EventBus.getDefault();
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.d(this.getClass().getName(), "registered bus");
	//	mEventBus.register(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		Log.d(this.getClass().getName(), "unregistered bus");
	//	mEventBus.unregister(this);
	}
}
