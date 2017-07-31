package jp.shts.android.library.sample.countanimation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import jp.shts.android.library.animatedclocktext.AnimatedClockTextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    AnimatedClockTextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (AnimatedClockTextView) findViewById(R.id.text);
//        tv.setTime(11, 30);
//        tv.setDuration(2000L);
        findViewById(R.id.button).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        tv.animateToTime(14, 45);
    }
}
