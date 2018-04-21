package com.plt.yzplatform.fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.plt.yzplatform.R;
import com.plt.yzplatform.adapter.CarListAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class CollectCardeal extends Fragment {
    @BindView(R.id.car_deal_listview)
    ListView carDealListview;
    Unbinder unbinder;
    private Context context;

    public CollectCardeal(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_collect_cardeal, container, false);
        unbinder = ButterKnife.bind(this, inflate);
        carDealListview.setAdapter(new CarListAdapter(context));

        return inflate;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
