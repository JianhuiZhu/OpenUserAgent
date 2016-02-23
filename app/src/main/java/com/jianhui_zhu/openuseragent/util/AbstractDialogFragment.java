package com.jianhui_zhu.openuseragent.util;

import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;import java.lang.Override;



/**
 * Created by jianhuizhu on 2016-01-27
 */
public class AbstractDialogFragment extends DialogFragment {

//	@Inject


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

//	@Override
//	public void onResume() {
//		super.onResume();
//		Log.d(this.getClass().getName(), "registered bus");
//		mEventBus.register(this);
//	}
//
//	@Override
//	public void onPause() {
//		super.onPause();
//		Log.d(this.getClass().getName(), "unregistered bus");
//		mEventBus.unregister(this);
//	}
}
