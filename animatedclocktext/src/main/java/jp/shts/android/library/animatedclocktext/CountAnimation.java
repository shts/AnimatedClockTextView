package jp.shts.android.library.animatedclocktext;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

class CountAnimation {

    interface Callback {
        void onUpdate(int index);

        void onError(int index);

        void onComplete();
    }

    private long count;
    private long sleep;
    private Callback callback;

    CountAnimation(long duration, long count) {
        // count * sleep = duration
        this.sleep = duration / count;
        this.count = count;
    }

    private final Handler uiHandler = new Handler(Looper.getMainLooper());
    private static HandlerThread handlerThread = new HandlerThread("animation-thread");
    private static Handler handler;

    static {
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());
    }

    void start(Callback callback) {
        this.callback = callback;
        handler.post(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < count; i++) {
                    try {
                        Thread.sleep(sleep);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        notifyError(i);
                    }
                    notifyUpdate(i);
                }
                notifyComplete();
            }
        });
    }

    private void notifyUpdate(final int i) {
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null) callback.onUpdate(i);
            }
        });
    }

    private void notifyError(final int i) {
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null) callback.onError(i);
            }
        });
    }

    private void notifyComplete() {
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null) callback.onComplete();
            }
        });
    }
}
