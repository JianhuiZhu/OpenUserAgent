package com.jianhui_zhu.openuseragent.util;

import android.app.Application;
import android.os.Build;
import android.os.StrictMode;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.ndk.CrashlyticsNdk;
import com.firebase.client.Firebase;
import com.firebase.client.Logger;

import java.lang.Override;import io.fabric.sdk.android.Fabric;


/**
 * Created by jianhuizhu on 2016-01-27
 */
public class AbstractApplication extends Application {
//	protected ObjectGraph mApplicationGraph;
private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;


	@Override
	public void onCreate() {
		super.onCreate();
		Fabric.with(this, new Crashlytics(), new CrashlyticsNdk());
		Firebase.setAndroidContext(this);
		WebUtil.instantiate(this);
		LocalDatabaseSingleton.instantiate(this);
		RemoteDatabaseSingleton.instantiate(this);
		Firebase.getDefaultConfig().setLogLevel(Logger.Level.DEBUG);
		Firebase.getDefaultConfig().enablePersistence();
		SettingSingleton.getInstance(getApplicationContext());
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyLog().build());
		}
	}
}
