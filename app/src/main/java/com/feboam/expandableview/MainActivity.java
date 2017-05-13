package com.feboam.expandableview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    
    private RelativeLayout rl;
    private LinearLayout ll;
    ValueAnimator mAnimator;
    boolean isShow = false;
    TextView mTv;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        ll.post(new Runnable() {
            @Override
            public void run() {
                ll.getLayoutParams().height = 0;
                ll.requestLayout();
            }
        });
    }

    private void initView() {
        rl = (RelativeLayout) findViewById(R.id.rl);
        ll = (LinearLayout) findViewById(R.id.ll);
        mTv = (TextView) findViewById(R.id.tv);
        rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isShow =!isShow;
                animateArea(isShow);
            }
        });
    }

    void animateArea(boolean shown) {

        if (mAnimator != null) {
            mAnimator.cancel();
        }

        final int startHeight = ll.getHeight();
        final int endHeight = shown ? getAreaActualHeight() : 0;

        ValueAnimator animator = ValueAnimator.ofInt(startHeight, endHeight);
        animator.setDuration(500);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int height = (int) animation.getAnimatedValue();
                ll.getLayoutParams().height = height;
                ll.requestLayout();
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                mTv.setText(isShow ?"收缩":"展开");
                mTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, isShow ?  R.mipmap.arrow_up_normal:R.mipmap.arrow_down_normal , 0);
            }
            @Override
            public void onAnimationEnd(Animator animation) {

            }
        });
        animator.start();
        mAnimator = animator;
    }


    int getAreaActualHeight() {
        int height =  ll.getMeasuredHeight();
        if (height == 0) {
            measureAreaActualHeight();
            height =  ll.getMeasuredHeight();
        }
        return height;
    }

    void measureAreaActualHeight() {
        ll.measure(rl.getWidth(), rl.getHeight());
    }
}
