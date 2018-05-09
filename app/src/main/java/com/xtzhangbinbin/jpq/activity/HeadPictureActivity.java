package com.xtzhangbinbin.jpq.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.xtzhangbinbin.jpq.R;
import com.xtzhangbinbin.jpq.base.BaseActivity;
import com.xtzhangbinbin.jpq.utils.JumpUtil;
import com.xtzhangbinbin.jpq.utils.OKhttptils;
import com.xtzhangbinbin.jpq.utils.Prefs;
import com.xtzhangbinbin.jpq.utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HeadPictureActivity extends BaseActivity {

    private static final String TAG = "门头照" ;
    @BindView(R.id.pic)
    ImageView pic;
    private String head_file_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_head_picture);
        ButterKnife.bind(this);
        head_file_id = getIntent().getExtras().getString("head_file_id");
        Log.d(TAG, "onCreate门头照: " + head_file_id);
        if (head_file_id == null){

        }else {
            OKhttptils.getPic(this, head_file_id, pic);
        }
    }



    @OnClick({R.id.pic, R.id.uploading})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.pic:
                upDataPicture(this,pic,null,"门头照",3,2,900,600);
                head_file_id = Prefs.with(getApplicationContext()).read("门头照");
                break;
            case R.id.uploading:
                if (getBitmap(pic) == null){
                    Bundle bundle = new Bundle();
                    bundle.putString("head_file_id",head_file_id);
                    JumpUtil.newInstance().finishRightTrans(this,bundle,003);
                }else {
                    ToastUtil.show(this,"请上传门头照片后提交");
                }
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        setTitle("门头照片");
        setLeftImageClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JumpUtil.newInstance().finishRightTrans(HeadPictureActivity.this);
            }
        });
    }
}
