package com.jianhui_zhu.openuseragent.viewmodel;

import android.content.Context;
import android.graphics.PorterDuff;
import android.widget.ImageView;
import android.widget.TextView;

import com.jianhui_zhu.openuseragent.R;

/**
 * Created by jianhuizhu on 2016-05-05.
 */
public class ThirdPartyContentViewModel {
    public void changeIconStatus(Context context,boolean status, TextView textView, ImageView icon) {
        if (textView != null && icon != null) {
            if (status) {
                textView.setTextColor(context.getResources().getColor(R.color.mdtp_white));
                icon.clearColorFilter();
            } else {
                textView.setTextColor(context.getResources().getColor(R.color.mdtp_light_gray));
                icon.setColorFilter(R.color.cardview_shadow_end_color, PorterDuff.Mode.MULTIPLY);
            }
        }
    }
}
