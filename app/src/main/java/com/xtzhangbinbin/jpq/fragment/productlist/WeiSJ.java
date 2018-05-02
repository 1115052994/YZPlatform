package com.xtzhangbinbin.jpq.fragment.productlist;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RadioButton;

import com.xtzhangbinbin.jpq.R;
import com.xtzhangbinbin.jpq.adapter.WeisjAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class WeiSJ extends Fragment {


    @BindView(R.id.weisj_list)
    ListView weisjList;
    Unbinder unbinder;
    @BindView(R.id.radio)
    RadioButton radio;

    public WeiSJ() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_weisj, container, false);
        unbinder = ButterKnife.bind(this, inflate);
        weisjList.setAdapter(new WeisjAdapter(getContext()));
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
