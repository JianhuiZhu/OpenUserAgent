package com.jianhui_zhu.openuseragent.util;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.ndk.CrashlyticsNdk;
import com.facebook.drawee.backends.pipeline.Fresco;

import java.lang.Override;import io.fabric.sdk.android.Fabric;


/**
 * Created by jianhuizhu on 2016-01-27
 */
public class BrowserApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		Fabric.with(this, new Crashlytics(), new CrashlyticsNdk());
		WebUtil.instantiate(this);
		LocalDatabaseSingleton.instantiate(this);
		Setting.instantiate(getApplicationContext());
		TrafficStatisticUtil.instantiate();
		Fresco.initialize(this);
	}
}
