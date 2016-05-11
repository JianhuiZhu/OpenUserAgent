package com.jianhui_zhu.openuseragent.view.dialogs;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.jianhui_zhu.openuseragent.R;
import com.jianhui_zhu.openuseragent.util.RxBus;
import com.jianhui_zhu.openuseragent.util.WebUtil;
import com.jianhui_zhu.openuseragent.util.event.WebViewRefreshEvent;
import com.jianhui_zhu.openuseragent.view.HomeView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by jianhuizhu on 2016-05-10.
 */
public class AlertDialog extends DialogFragment {
    private boolean flag = true;
    public static final String WARNING_TAG ="WARNING";
    public static final String URL_TAG = "URL";
    public static final String STATUS_TAG = "STATUS";
    public static final String HOST_TAG = "HOST";
    private HomeView.CustomWebViewClient client;
    @Bind(R.id.alert_allow)
    TextView alertAllow;
    @Bind(R.id.alert_block)
    TextView alertBlock;
    @Bind(R.id.alert_switch)
    Switch trigger;
    @Bind(R.id.alert_url)
    TextView urlArea;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_alert,container,false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(flag) {
            final String url = getArguments().getString(URL_TAG);
            String host = getArguments().getString(HOST_TAG);
            boolean status = getArguments().getBoolean(STATUS_TAG);
            if (WebUtil.isThirdParty(host, url)) {
                urlArea.setText(url);
                if (status) {
                    trigger.setChecked(true);
                } else {
                    trigger.setChecked(false);
                }
            } else {
                urlArea.setText(R.string.alert);
                trigger.setVisibility(View.GONE);
                alertAllow.setVisibility(View.GONE);
                alertBlock.setVisibility(View.GONE);
            }
            trigger.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        client.getTabPolicy().put(url, true);
                    } else {
                        client.getTabPolicy().put(url, false);
                    }
                }
            });
        }else{
            urlArea.setText(getArguments().getString(WARNING_TAG));
            trigger.setVisibility(View.GONE);
            alertAllow.setVisibility(View.GONE);
            alertBlock.setVisibility(View.GONE);
        }
    }
    public static AlertDialog newInstance(String url, boolean status, String host, HomeView.CustomWebViewClient client){
        AlertDialog dialog = new AlertDialog();
        dialog.client = client;
        Bundle bundle = new Bundle();

        bundle.putString(URL_TAG,WebUtil.getDomain(url));
        bundle.putString(HOST_TAG,WebUtil.getDomain(host));
        bundle.putBoolean(STATUS_TAG,status);
        dialog.setArguments(bundle);
        return dialog;
    }
    public static AlertDialog newInstanceWithWarning(String warning){
        AlertDialog dialog = new AlertDialog();
        Bundle bundle = new Bundle();
        bundle.putString(WARNING_TAG,warning);
        dialog.flag = false;
        return dialog;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        RxBus.getInstance().send(new WebViewRefreshEvent());
    }
}
