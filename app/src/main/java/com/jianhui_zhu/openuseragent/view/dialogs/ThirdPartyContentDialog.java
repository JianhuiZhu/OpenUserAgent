package com.jianhui_zhu.openuseragent.view.dialogs;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jianhui_zhu.openuseragent.R;
import com.jianhui_zhu.openuseragent.util.RxBus;
import com.jianhui_zhu.openuseragent.util.event.GlobalBlackListEvent;
import com.jianhui_zhu.openuseragent.util.event.ThirdPartyTabSpecificEvent;
import com.jianhui_zhu.openuseragent.util.event.WebViewRefreshEvent;
import com.jianhui_zhu.openuseragent.view.adapter.ThirdPartyContentAdapter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jianhuizhu on 2016-05-03.
 */
public class ThirdPartyContentDialog extends DialogFragment {
    public static final String GLOBAL_BLACKLIST_TAG ="GLOBAL_BLACKLIST";
    public static final String TAB_POLICY_TAG = "TAB_POLICY";
    public static final String DOMAIN_TAG = "DOMAIN";
    @Bind(R.id.third_party_dialog_title)
    TextView title;
    @Bind(R.id.global_block_hint_text)
    TextView hint;
    @Bind(R.id.global_block_icon)
    ImageView icon;
    @Bind(R.id.third_party_list)
    RecyclerView recyclerView;
    @OnClick(R.id.block_button_area)
    public void click(){
        Set<String> set = ((ThirdPartyContentAdapter)recyclerView.getAdapter()).getTobeAdded();
        GlobalBlackListEvent event = new GlobalBlackListEvent(set,GlobalBlackListEvent.ADD);
        RxBus.getInstance().send(event);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_third_party_content,container,false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        icon.setColorFilter(R.color.cardview_shadow_end_color, PorterDuff.Mode.MULTIPLY);
        hint.setTextColor(getResources().getColor(R.color.mdtp_light_gray));
        HashSet<String> globalBlackList = (HashSet<String>)getArguments().getSerializable(GLOBAL_BLACKLIST_TAG);
        HashMap<String,Boolean> tabPolicy = (HashMap<String, Boolean>)getArguments().getSerializable(TAB_POLICY_TAG);
        String domain = getArguments().getString(DOMAIN_TAG);
        if(domain!=null){
            title.setText(domain);
        }
        ThirdPartyContentAdapter adapter = new ThirdPartyContentAdapter(globalBlackList,tabPolicy,hint,icon);
        recyclerView.setAdapter(adapter);
    }
    public static ThirdPartyContentDialog newInstance(HashSet<String> globalBlackList, HashMap<String,Boolean> tabPolicy,String domain){
        ThirdPartyContentDialog dialog = new ThirdPartyContentDialog();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ThirdPartyContentDialog.GLOBAL_BLACKLIST_TAG,globalBlackList);
        bundle.putSerializable(ThirdPartyContentDialog.TAB_POLICY_TAG,tabPolicy);
        bundle.putString(ThirdPartyContentDialog.DOMAIN_TAG,domain);
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        RxBus.getInstance().send(new WebViewRefreshEvent());
    }
}
