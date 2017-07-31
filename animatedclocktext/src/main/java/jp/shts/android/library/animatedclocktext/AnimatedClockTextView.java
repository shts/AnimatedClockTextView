package jp.shts.android.library.animatedclocktext;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

public class AnimatedClockTextView extends android.support.v7.widget.AppCompatTextView {

    private static final int ANIM_DURATION = 5000;

    private CountAnimation countAnimation;
    private CountTime countTime;
    private long duration = ANIM_DURATION;

    public AnimatedClockTextView(Context context) {
        this(context, null);
    }

    public AnimatedClockTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AnimatedClockTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.AnimatedClockTextView);
            int hours = ta.getInt(R.styleable.AnimatedClockTextView_hour, 0);
            int minutes = ta.getInt(R.styleable.AnimatedClockTextView_minutes, 0);
            countTime = new CountTime(hours, minutes);
            duration = ta.getInteger(R.styleable.AnimatedClockTextView_duration, ANIM_DURATION);
            ta.recycle();
        } else {
            countTime = new CountTime(0, 0);
            duration = ANIM_DURATION;
        }
        setText(countTime.toString());
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void setTime(int hours, int minutes) {
        checkParams(hours, minutes);
        countTime = new CountTime(hours, minutes);
        setText(countTime.toString());
    }

    public void animateToTime(int hours, int minutes) {
        checkParams(hours, minutes);
        final CountTime nextCountTime = new CountTime(hours, minutes);
        final int diffOfMinutes = countTime.diffOfMinutes(nextCountTime);
        if (duration <= 0 || diffOfMinutes <= 0) return;
        countAnimation = new CountAnimation(duration, diffOfMinutes);
        countAnimation.start(new CountAnimation.Callback() {
            @Override
            public void onUpdate(int index) {
                if (countTime.isAfter(nextCountTime)) {
                    countTime.up();
                } else {
                    countTime.down();
                }
                setText(countTime.toString());
            }

            @Override
            public void onError(int index) {

            }

            @Override
            public void onComplete() {
                countTime = nextCountTime;
            }
        });
    }

    private void checkParams(int hours, int minutes) {
        if (23 < hours) throw new IllegalArgumentException("hours must be in 0-23.");
        if (59 < minutes) throw new IllegalArgumentException("minutes must be in 0-59.");
    }

    private static class CountTime {
        int hour, minute;

        private CountTime(int hour, int minute) {
            this.hour = hour;
            this.minute = minute;
        }

        private void up() {
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

        private void down() {
            if (0 < minute) {
                minute--;
            } else {
                minute = 59;
                if (0 < hour) {
                    hour--;
                } else {
                    hour = 23;
                }
            }
        }

        private boolean isAfter(CountTime countTime) {
            return toMinutes() < countTime.toMinutes();
        }

        private boolean isBefore(CountTime countTime) {
            return toMinutes() > countTime.toMinutes();
        }

        private int diffOfMinutes(CountTime countTime) {
            return Math.abs(toMinutes() - countTime.toMinutes());
        }

        private int toMinutes() {
            return hour * 60 + minute;
        }

        @Override
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
