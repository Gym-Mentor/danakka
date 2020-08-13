package com.example.fish;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class ResultDialog extends Dialog {

    private TextView mTitleView;
    private TextView first_fish;
    private TextView first_pred;
    private TextView second_fish;
    private TextView second_pred;
    private TextView third_fish;
    private TextView third_pred;

    private Button mLeftButton;

    private String mTitle;
    private String f_fish;
    private String f_pred;
    private String s_fish;
    private String s_pred;
    private String t_fish;
    private String t_pred;


    private View.OnClickListener mLeftClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 다이얼로그 외부 화면 흐리게 표현
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.result_dialog);


        first_fish = (TextView) findViewById(R.id.first_fish);
        first_pred= (TextView) findViewById(R.id.first_pred);
        second_fish = (TextView) findViewById(R.id.second_fish);
        second_pred= (TextView) findViewById(R.id.second_pred);
        third_fish = (TextView) findViewById(R.id.third_fish);
        third_pred= (TextView) findViewById(R.id.third_pred);

        mLeftButton = (Button) findViewById(R.id.dialog_btn);

        // 제목과 내용을 생성자에서 셋팅한다.
        first_fish.setText(f_fish);
        first_pred.setText(f_pred+"%");
        second_fish.setText(s_fish);
        second_pred.setText(s_pred+"%");
        third_fish.setText(t_fish);
        third_pred.setText(t_pred+"%");

        // 클릭 이벤트 셋팅

        if (mLeftClickListener != null )
            mLeftButton.setOnClickListener(mLeftClickListener);

    }

    // 클릭버튼이 하나일때 생성자 함수로 클릭이벤트를 받는다.
    public ResultDialog(Context context, String first_fish, float first_pred,String second_fish, float second_pred,String third_fish, float third_pred,
                        View.OnClickListener singleListener) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.f_fish=first_fish;
        this.f_pred=Float.toString(first_pred);
        this.s_fish=second_fish;
        this.s_pred=Float.toString(second_pred);
        this.t_fish=third_fish;
        this.t_pred=Float.toString(third_pred);

        this.mLeftClickListener = singleListener;
    }

}
