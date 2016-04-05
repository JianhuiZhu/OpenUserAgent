package com.jianhui_zhu.openuseragent.util;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;import java.lang.Override;



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
	}

	@Override
	public void onPause() {
		super.onPause();
	}
}
