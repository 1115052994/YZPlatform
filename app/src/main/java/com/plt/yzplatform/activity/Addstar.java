package com.plt.yzplatform.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.plt.yzplatform.R;
import com.plt.yzplatform.base.BaseActivity;
import com.plt.yzplatform.utils.JumpUtil;
import com.plt.yzplatform.view.CircleImageView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Addstar extends BaseActivity {

    @BindView(R.id.addition_image)
    CircleImageView additionImage;
    @BindView(R.id.addition_ed_name)
    EditText additionEdName;
    @BindView(R.id.addition_ed_introduce)
    EditText additionEdIntroduce;
    @BindView(R.id.addition_layout)
    LinearLayout additionLayout;
    private String text = "示例 精通汽车工作原理以及各种故障排查和解决，有较强的... （最多输入150个字）";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_star);
        ButterKnife.bind(this);
        additionLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (additionEdName.getText().toString().length() != 0 && additionEdIntroduce.getText().toString().length() != 0) {
                    ArrayList<String> objects = new ArrayList<>();
                    objects.add(additionEdName.getText().toString().trim());
                    objects.add(additionEdIntroduce.getText().toString().trim());
                    //将数据封装到intent中
                    Intent intent = new Intent();
                    intent.putStringArrayListExtra("name", objects);
                    //添加返回值
                    setResult(1, intent);
                    //销毁当前的activity
                    finish();
                } else {
                    Toast.makeText(Addstar.this, "不能为空" + additionEdName.getText().toString() + additionEdIntroduce.getText().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        int end = text.length();
        SpannableString textSpan = new SpannableString(text);
        textSpan.setSpan(new AbsoluteSizeSpan(25), 0, 2, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        textSpan.setSpan(new AbsoluteSizeSpan(23), 2, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        additionEdIntroduce.setHint(textSpan);
        additionEdName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        additionEdIntroduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        setTitle("添加明星员工");
        setLeftImageClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JumpUtil.newInstance().finishRightTrans(Addstar.this);
            }
        });
    }
}
