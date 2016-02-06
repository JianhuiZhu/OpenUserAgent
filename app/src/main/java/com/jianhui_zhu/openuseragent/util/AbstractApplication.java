package com.jianhui_zhu.openuseragent.util;

import android.app.Application;


/**
 * Created by jianhuizhu on 2016-01-27
 */
public class AbstractApplication extends Application {
//	protected ObjectGraph mApplicationGraph;
private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;


	@Override
	public void onCreate() {
		super.onCreate();
//		manager.registerGcm(this).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.newThread()).subscribe(new Action1<Boolean>() {
//			@Override
//			public void call(Boolean succeed) {
//				if (succeed) {
//					Toast.makeText(AbstractApplication.this, "device registered", Toast.LENGTH_SHORT).show();
//				}
//			}
//		}, new Action1<Throwable>() {
//			@Override
//			public void call(Throwable throwable) {
//				Toast.makeText(AbstractApplication.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
//
//			}
//		});

//		// Initiates Dagger
//		mApplicationGraph = ObjectGraph.create(DaggerManager.getDaggerModules().toArray());
//		mApplicationGraph.inject(this);
	}

//	public ObjectGraph getApplicationGraph(){
//		return mApplicationGraph;
//	}

//	/**
//	 * Check the device to make sure it has the Google Play Services APK. If
//	 * it doesn't, display a dialog that allows users to download the APK from
//	 * the Google Play Store or enable it in the device's system settings.
//	 */
//	private boolean checkPlayServices() {
//		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
//		if (resultCode != ConnectionResult.SUCCESS) {
//			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
//				Log.i(this.getClass().getName(), "This device is not supported.");
//			} else {
//				Log.i(this.getClass().getName(), "This device is not supported.");
//			}
//			return false;
//		}
//		return true;
//	}
}
