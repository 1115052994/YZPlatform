package com.xtzhangbinbin.jpq.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.xtzhangbinbin.jpq.R;
import com.xtzhangbinbin.jpq.base.BaseActivity;
import com.xtzhangbinbin.jpq.utils.JumpUtil;
import com.xtzhangbinbin.jpq.utils.Prefs;
import com.xtzhangbinbin.jpq.utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BusinessActivity extends BaseActivity {

    private static final String TAG = "营业执照";
    @BindView(R.id.pic)
    ImageView pic;
    private String file_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business);
        ButterKnife.bind(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setTitle("营业执照");
        setLeftImageClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JumpUtil.newInstance().finishRightTrans(BusinessActivity.this);
            }
        });
    }

    @OnClick({R.id.pic, R.id.uploading})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.pic:
                upDataPicture(this,pic,null,"营业执照");
                file_id = Prefs.with(getApplicationContext()).read("营业执照");
                Log.e(TAG, "onViewClicked: " + file_id );
                break;
            case R.id.uploading:
                if (getBitmap(pic) == null){
                    Bundle bundle = new Bundle();
                    bundle.putString("file_id",file_id);
                    JumpUtil.newInstance().finishRightTrans(BusinessActivity.this,bundle,002);
                }else {
                    ToastUtil.show(this,"请上传营业执照后提交");
                }
                break;
        }
    }
}
