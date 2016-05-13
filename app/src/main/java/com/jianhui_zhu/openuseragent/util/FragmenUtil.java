package com.jianhui_zhu.openuseragent.util;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.jianhui_zhu.openuseragent.R;

/**
 * Created by Jianhuizhu on 2015-08-21.
 */
public final class FragmenUtil {
	private FragmenUtil() {
	}
	private static String FRAGMENT_TAG = "fragment_tag";
	private static String DIALOG_FRAGMENT_TAG = "dialog_fragment";
	public static void switchToFragment(Context context, Fragment fragment){
		if(context instanceof FragmentActivity){
			if(fragment instanceof DialogFragment){
				((DialogFragment) fragment).show(((FragmentActivity)context).getSupportFragmentManager(), DIALOG_FRAGMENT_TAG);
			} else{
				FragmentTransaction transaction = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
				transaction.addToBackStack(fragment.getClass().getSimpleName());
				transaction.add(R.id.container, fragment, fragment.getClass().getSimpleName());
				transaction.commit();
			}
		}
	}
	public static void backToPreviousFragment(Context context,Fragment fragment){
		((FragmentActivity)context).getSupportFragmentManager()
				.beginTransaction()
				.remove(fragment).commit();
		((Activity)context).getFragmentManager().popBackStack();
	}

}
