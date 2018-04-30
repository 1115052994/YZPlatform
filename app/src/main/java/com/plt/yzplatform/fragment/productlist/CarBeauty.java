package com.plt.yzplatform.fragment.productlist;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RadioButton;

import com.plt.yzplatform.R;
import com.plt.yzplatform.adapter.CarBeautyAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class CarBeauty extends Fragment {


    @BindView(R.id.car_beauty_list)
    ListView carBeautyList;
    Unbinder unbinder;
    @BindView(R.id.radio)
    RadioButton radio;
    private ArrayList<String> arr=new ArrayList<>();



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_car_beauty, container, false);
        unbinder = ButterKnife.bind(this, inflate);
        arr.add("123");
        carBeautyList.setAdapter(new CarBeautyAdapter(getContext(),arr));
        radio.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        radio.setChecked(false);
                        break;
                    case MotionEvent.ACTION_MOVE:

                        break;
                    case MotionEvent.ACTION_UP:
                        radio.setChecked(true);
                        break;
                }
                return false;
            }
        });

        return inflate;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
