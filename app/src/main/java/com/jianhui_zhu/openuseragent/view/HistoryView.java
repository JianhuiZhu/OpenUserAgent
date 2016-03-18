package com.jianhui_zhu.openuseragent.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jianhui_zhu.openuseragent.R;
import com.jianhui_zhu.openuseragent.model.beans.History;
import com.jianhui_zhu.openuseragent.presenter.HistoryPresenter;
import com.jianhui_zhu.openuseragent.util.AbstractFragment;
import com.jianhui_zhu.openuseragent.view.adapter.HistoryAdapter;
import com.jianhui_zhu.openuseragent.view.interfaces.HistoryViewInterface;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.functions.Action1;

/**
 * Created by jianhuizhu on 2016-02-16.
 */
public class HistoryView extends AbstractFragment implements DatePickerDialog.OnDateSetListener,HistoryViewInterface{
    @Bind(R.id.general_tool_bar_title)
    TextView toolbarTitle;
    @Bind(R.id.history_list)
    RecyclerView list;
    @OnClick(R.id.date_picker_btn)
    public void click(View view){
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.show(getFragmentManager(), "Datepickerdialog");
    }
    HistoryAdapter adapter;
    HistoryPresenter presenter=new HistoryPresenter(getActivity(),this);
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        list.setLayoutManager(new LinearLayoutManager(getActivity()));
        list.setItemAnimator(new DefaultItemAnimator());
        list.setHasFixedSize(true);
        presenter.getAllHistory().subscribe(new Action1<List<History>>() {
            @Override
            public void call(List<History> histories) {
                adapter=new HistoryAdapter(getActivity(),presenter,histories);
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
}
