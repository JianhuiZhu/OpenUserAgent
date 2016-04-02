package com.jianhui_zhu.openuseragent.util;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;

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
		if(context instanceof Activity){
			if(fragment instanceof AbstractDialogFragment){
				((AbstractDialogFragment) fragment).show(((Activity) context).getFragmentManager(), DIALOG_FRAGMENT_TAG);
			} else{
				FragmentTransaction transaction = ((Activity) context).getFragmentManager().beginTransaction();
				transaction.setCustomAnimations(R.animator.fragment_slide_right_enter,R.animator.fragment_slide_left_exit);
				transaction.addToBackStack(fragment.getClass().getSimpleName());
				transaction.add(R.id.container, fragment, fragment.getClass().getSimpleName());
				transaction.commit();
			}
		}
	}
	public static void backToPreviousFragment(Context context,Fragment fragment){
		((Activity)context).getFragmentManager()
				.beginTransaction()
				.setCustomAnimations(R.animator.fragment_slide_right_enter,R.animator.fragment_slide_left_exit)
				.remove(fragment).commit();
		((Activity)context).getFragmentManager().popBackStack();
	}

	public static void switchToRootFragment(Context context, Fragment fragment){
		if(context instanceof Activity){
			int backStackCounts = ((Activity) context).getFragmentManager().getBackStackEntryCount();
			while (backStackCounts>0){
				((Activity) context).getFragmentManager().popBackStackImmediate(FRAGMENT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
				backStackCounts--;
			}
			((Activity) context).getFragmentManager().beginTransaction().add(R.id.container,fragment).commit();
		}
	}
}
