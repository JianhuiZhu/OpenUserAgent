package com.jianhui_zhu.openuseragent.view;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jianhui_zhu.openuseragent.R;
import com.jianhui_zhu.openuseragent.model.HistoryManager;
import com.jianhui_zhu.openuseragent.util.AbstractFragment;
import com.jianhui_zhu.openuseragent.util.FragmenUtil;
import com.jianhui_zhu.openuseragent.util.activity.MainActivity;
import com.jianhui_zhu.openuseragent.view.adapter.HistoryAdapter;
import com.jianhui_zhu.openuseragent.view.interfaces.HomeViewInterface;
import com.jianhui_zhu.openuseragent.viewmodel.HistoryViewModel;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jianhuizhu on 2016-02-16.
 */
public class HistoryView extends AbstractFragment implements DatePickerDialog.OnDateSetListener{
    HomeViewInterface viewInterface;
    CoordinatorLayout container;
    HistoryView historyView;
    HistoryManager manager = new HistoryManager();
    HistoryViewModel viewModel = new HistoryViewModel();
    @Bind(R.id.general_tool_bar_title)
    TextView toolbarTitle;
    @Bind(R.id.history_list)
    RecyclerView list;
    @Bind(R.id.delete_btn)
    public ImageView delete;
    @OnClick({R.id.date_picker_btn,R.id.history_go_back,R.id.delete_btn})
    public void click(View view){
        switch (view.getId()) {
            case R.id.date_picker_btn:
            Calendar now = Calendar.getInstance();
            DatePickerDialog dpd = DatePickerDialog.newInstance(
                    this,
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH));
            dpd.show(getFragmentManager(), "Datepickerdialog");
                break;
            case R.id.history_go_back:
                FragmenUtil.backToPreviousFragment(getActivity(),this);
                break;
            case R.id.delete_btn:
                adapter =(HistoryAdapter)list.getAdapter();
                boolean isEmpty = adapter.getSelected().isEmpty();
                if(isEmpty==false){
                    viewModel.deleteSelectedHistories(manager.deleteSelectedHistories(adapter.getSelected()),container,adapter);
                }
        }
    }
    HistoryAdapter adapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        historyView=this;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.view_history,container,false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbarTitle.setText("History");
        delete.setColorFilter(R.color.mdtp_light_gray, PorterDuff.Mode.MULTIPLY);
        list.setLayoutManager(new LinearLayoutManager(getActivity()));
        list.setItemAnimator(new DefaultItemAnimator());
        list.setHasFixedSize(true);
        container =((MainActivity)getActivity()).getContainer();
        adapter=new HistoryAdapter(getActivity(), viewInterface,historyView);
        list.setAdapter(adapter);
        viewModel.inflateAllHistories(manager.getAllHistory(),adapter);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int month, int day) {
        viewModel.inflateHistoriesByDate(manager.getHistoryByDate(year,month,day),adapter);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
    public static HistoryView newInstance(HomeViewInterface viewInterface){
        HistoryView view = new HistoryView();
        view.viewInterface = viewInterface;
        return view;
    }
}
