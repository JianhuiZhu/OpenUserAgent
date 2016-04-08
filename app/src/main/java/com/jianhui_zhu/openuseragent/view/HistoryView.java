package com.jianhui_zhu.openuseragent.view;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jianhui_zhu.openuseragent.R;
import com.jianhui_zhu.openuseragent.model.beans.History;
import com.jianhui_zhu.openuseragent.presenter.HistoryPresenter;
import com.jianhui_zhu.openuseragent.presenter.HomePresenter;
import com.jianhui_zhu.openuseragent.util.AbstractFragment;
import com.jianhui_zhu.openuseragent.util.FragmenUtil;
import com.jianhui_zhu.openuseragent.util.activity.MainActivity;
import com.jianhui_zhu.openuseragent.view.adapter.HistoryAdapter;
import com.jianhui_zhu.openuseragent.view.interfaces.HistoryViewInterface;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.functions.Action1;

/**
 * Created by jianhuizhu on 2016-02-16.
 */
public class HistoryView extends AbstractFragment implements DatePickerDialog.OnDateSetListener,HistoryViewInterface{
    CoordinatorLayout container;
    HistoryView historyView;
    @Bind(R.id.general_tool_bar_title)
    TextView toolbarTitle;
    @Bind(R.id.history_list)
    RecyclerView list;
    @Bind(R.id.delete_btn)
    ImageView delete;
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
                    presenter.deleteSelectedHistories(adapter.getSelected()).subscribe(new Action1<String>() {
                        @Override
                        public void call(String s) {
                            adapter.deleteSelected();
                            Snackbar.make(container,s,Snackbar.LENGTH_SHORT).show();
                        }
                    });
                }
        }
    }
    HistoryAdapter adapter;
    HistoryPresenter presenter=new HistoryPresenter(getActivity(),this);
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
        presenter.getAllHistory().subscribe(new Action1<List<History>>() {
            @Override
            public void call(List<History> histories) {
                HomePresenter homePresenter=null;
                try {
                    homePresenter = HomePresenter.getInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                adapter=new HistoryAdapter(getActivity(),presenter,histories, homePresenter,historyView);
                list.setAdapter(adapter);
            }
        });
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int month, int day) {
        presenter.getHistoryByDate(year, month, day);
    }

    @Override
    public void refreshList(List<History> histories) {
        adapter.changeHistoriesDataSet(histories);
    }

    @Override
    public void setGarbageIconStatus(boolean status) {
        if(status){
            delete.clearColorFilter();
        }else {
            delete.setColorFilter(R.color.mdtp_light_gray, PorterDuff.Mode.MULTIPLY);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
