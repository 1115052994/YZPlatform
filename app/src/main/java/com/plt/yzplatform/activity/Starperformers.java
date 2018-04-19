package com.plt.yzplatform.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.plt.yzplatform.R;
import com.plt.yzplatform.adapter.CallStar;
import com.plt.yzplatform.adapter.StarAdapter;
import com.plt.yzplatform.base.BaseActivity;
import com.plt.yzplatform.utils.JumpUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Starperformers extends BaseActivity {

    @BindView(R.id.star_list)
    ListView starList;
    @BindView(R.id.layout)
    LinearLayout layout;
    private List<ArrayList<String>> arr = new ArrayList<>();
    public StarAdapter starAdapter;
    ArrayList<String> strings = new ArrayList<>();
    ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starperformers);
        ButterKnife.bind(this);
        starList = findViewById(R.id.star_list);
        layout = findViewById(R.id.layout);
        strings.add("aaaaa");
        strings.add("bbbbb");
        arr.add(strings);
        arr.add(strings);
        starAdapter = new StarAdapter(this, arr);
        starList.setAdapter(starAdapter);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(Starperformers.this, Addstar.class), 1);
            }
        });
        starAdapter.getcall(new CallStar() {
            @Override
            public void Call(View view) {
                starAdapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        setTitle("明星员工");
        setLeftImageClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JumpUtil.newInstance().finishRightTrans(Starperformers.this);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 1) {
            arr.add(data.getStringArrayListExtra("name"));
            starAdapter.notifyDataSetChanged();
        }
    }
}
