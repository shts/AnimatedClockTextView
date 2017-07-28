package jp.shts.android.library.sample.countanimation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView tv;

    private static final long ANIM_DURATION = 5000L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView) findViewById(R.id.text);
        final CountTime countTime = new CountTime(0, 0);
        tv.setText(countTime.toString());
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CountAnimation(ANIM_DURATION, 0, 5000).start(new CountAnimation.Callback() {
                    @Override
                    public void onUpdate(int i) {
                        countTime.up();
                        tv.setText(countTime.toString());
                    }

                    @Override
                    public void onError(int i) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });
            }
        });
    }

    class CountTime {
        int hour, minute;

        CountTime(int hour, int minute) {
            this.hour = hour;
            this.minute = minute;
        }

        void up() {
            if (minute < 59) {
                minute++;
            } else {
                minute = 0;
                if (hour < 23) {
                    hour++;
                } else {
                    hour = 0;
                }
            }
        }

        public String toString() {
            return hourString() + ":" + minuteString();
        }

        String hourString() {
            return String.valueOf(hour);
        }

        String minuteString() {
            return minute < 10 ? "0" + minute : String.valueOf(minute);
        }
    }
}
