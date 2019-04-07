package fi.tuni.androidsudoku;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;

/**
 *
 */
public class TimerService extends Service {

    /**
     *
     */
    public static final String TIMER_EVENT = "UPDATE_TIMER";

    /**
     *
     */
    private volatile long startTimestamp;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            startTimestamp = System.currentTimeMillis();

            new Thread(this::updateEvent).start();
            return START_STICKY;
        }

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        startTimestamp = -1;
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     *
     */
    private void updateEvent() {
        Intent event = new Intent(TIMER_EVENT);
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this);

        while (startTimestamp > 0) {
            event.putExtra("elapsed", System.currentTimeMillis() - startTimestamp);
            manager.sendBroadcast(event);

            try {
                Thread.sleep(25);
            } catch (InterruptedException e) {
                // Ignored
            }
        }
    }
}
