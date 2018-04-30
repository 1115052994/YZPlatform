package com.plt.yzplatform.fragment.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.plt.yzplatform.R;
import com.plt.yzplatform.activity.AccessCar;
import com.plt.yzplatform.activity.AccessResult;
import com.plt.yzplatform.activity.AddProductActivity;
import com.plt.yzplatform.activity.CarCompDetail;
import com.plt.yzplatform.activity.CarDetailsActivity;
import com.plt.yzplatform.activity.EnterpriseActivity;
import com.plt.yzplatform.activity.MySubscribe;
import com.plt.yzplatform.activity.PersonalSettingActivity;
import com.plt.yzplatform.utils.JumpUtil;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by glp on 2018/4/18.
 * 描述：主页
 */
public class MainFragment extends Fragment {
    Unbinder unbinder;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_main, container, false);
        }
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.renzheng, R.id.setting, R.id.add, R.id.carDetail,R.id.test,R.id.test1})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.renzheng:
                JumpUtil.newInstance().jumpRight(getContext(), EnterpriseActivity.class);
                break;
            case R.id.setting:
                JumpUtil.newInstance().jumpRight(getContext(), PersonalSettingActivity.class);
                break;
            case R.id.add:
                JumpUtil.newInstance().jumpRight(getContext(), AddProductActivity.class);
                break;
            case R.id.carDetail:
                JumpUtil.newInstance().jumpRight(getContext(), CarDetailsActivity.class);
                break;
            case R.id.test:
                JumpUtil.newInstance().jumpLeft(getContext(), CarCompDetail.class);
                break;
            case R.id.test1:
                //MySubscribe   AccessCar  AccessResult
                JumpUtil.newInstance().jumpLeft(getContext(), AccessCar.class);
                break;
        }
    }
}
