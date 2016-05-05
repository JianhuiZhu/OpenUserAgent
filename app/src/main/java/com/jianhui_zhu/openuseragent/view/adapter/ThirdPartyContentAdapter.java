package com.jianhui_zhu.openuseragent.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.jianhui_zhu.openuseragent.R;
import com.jianhui_zhu.openuseragent.util.RxBus;
import com.jianhui_zhu.openuseragent.util.event.ThirdPartyTabSpecificEvent;
import com.jianhui_zhu.openuseragent.viewmodel.ThirdPartyContentViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jianhuizhu on 2016-05-03.
 */
public class ThirdPartyContentAdapter extends RecyclerView.Adapter<ThirdPartyContentAdapter.ViewHolder> {
    private ThirdPartyContentViewModel viewModel = new ThirdPartyContentViewModel();
    private List<Map.Entry<String,Boolean>> policyList = new ArrayList<>();
    private HashSet<String> globalBlackList = new HashSet<>();

    public HashSet<String> getTobeAdded() {
        return tobeAdded;
    }

    private HashSet<String> tobeAdded = new HashSet<>();
    private TextView tv;
    private ImageView icon;
    public List<Map.Entry<String, Boolean>> getPolicyList() {
        return policyList;
    }
    public ThirdPartyContentAdapter(Set<String> globalBlackList, HashMap<String,Boolean> tabPolicyMap, TextView tv, ImageView icon){
        if(globalBlackList!=null) {
            this.globalBlackList.addAll(globalBlackList);
        }
        if(tabPolicyMap!=null) {
            this.policyList.addAll(tabPolicyMap.entrySet());
        }
        this.tv = tv;
        this.icon = icon;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_third_party,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Map.Entry<String,Boolean> item = policyList.get(position);
        holder.switcher.setChecked(item.getValue());
        holder.domain.setText(item.getKey());
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    tobeAdded.add(item.getKey());
                }else{
                    tobeAdded.remove(item.getKey());
                }
            }
        });
        if(globalBlackList.contains(item.getKey())){
            holder.checkBox.setChecked(true);
        }else{
            holder.checkBox.setChecked(false);
        }
    }

    @Override
    public int getItemCount() {return policyList.size();}

    public class ViewHolder extends RecyclerView.ViewHolder implements Switch.OnCheckedChangeListener{
        @Bind(R.id.third_party_dialog_global_checkbox)
        CheckBox checkBox;
        @Bind(R.id.third_party_domain)
        TextView domain;
        @Bind(R.id.third_party_block_switch)
        Switch switcher;
        @Bind(R.id.third_party_status)
        TextView status;

        public ViewHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this,itemView);
            switcher.setOnCheckedChangeListener(this);
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(isChecked){
                status.setText(R.string.block);
                RxBus.getInstance().send(new ThirdPartyTabSpecificEvent(domain.getText().toString(),true));
            }else{
                status.setText(R.string.allow);
                RxBus.getInstance().send(new ThirdPartyTabSpecificEvent(domain.getText().toString(),false));
            }
        }
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
    }

}
