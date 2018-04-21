package com.plt.yzplatform.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.plt.yzplatform.R;
import com.plt.yzplatform.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/* 产品添加 */
public class AddProductActivity extends BaseActivity {

    @BindView(R.id.mType)
    TextView mType;
    @BindView(R.id.mName)
    EditText mName;
    @BindView(R.id.mYouhui)
    EditText mYouhui;
    @BindView(R.id.product_list)
    ListView productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.mType, R.id.mAdd, R.id.uploading})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mType:
                break;
            case R.id.mAdd:
                break;
            case R.id.uploading:
                break;
        }
    }
}
