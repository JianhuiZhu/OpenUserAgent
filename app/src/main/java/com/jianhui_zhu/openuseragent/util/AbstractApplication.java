package com.jianhui_zhu.openuseragent.util;

import android.app.Application;
import android.os.Build;
import android.os.StrictMode;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.ndk.CrashlyticsNdk;
import com.firebase.client.Firebase;
import com.firebase.client.Logger;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.lang.Override;import io.fabric.sdk.android.Fabric;


/**
 * Created by jianhuizhu on 2016-01-27
 */
public class AbstractApplication extends Application {
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
//	private boolean checkPlayServices() {
//		int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
//		if (status != ConnectionResult.SUCCESS) {
//			if (GooglePlayServicesUtil.isUserRecoverableError(status)) {
//				showErrorDialog(status);
//			} else {
//				Toast.makeText(this, "This device is not supported.",
//						Toast.LENGTH_LONG).show();
//			}
//			return false;
//		}
//		return true;
//	}
}
