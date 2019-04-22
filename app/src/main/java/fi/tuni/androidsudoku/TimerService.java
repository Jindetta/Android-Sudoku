package fi.tuni.androidsudoku;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;

/**
 * Service for keeping track of time.
 *
 * @author  Joonas Lauhala @literal{<joonas.lauhala@tuni.fi>}
 * @version 20190422
 * @since   1.8
 */
public class TimerService extends Service {

    /**
     * Defines event identifier.
     */
    public static final String TIMER_EVENT = "UPDATE_TIMER";

    /**
     * Stores initial timestamp.
     */
    private volatile long startTimestamp;

    /**
     * Starts service.
     *
     * @param intent    Sender intent.
     * @param flags     Sender flags.
     * @param startId   Start identifier.
     * @return          Service state value.
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            startTimestamp = System.currentTimeMillis();

            new Thread(this::updateEvent).start();
            return START_STICKY;
        }

        return START_NOT_STICKY;
    }

    /**
     * Destroys service.
     */
    @Override
    public void onDestroy() {
        startTimestamp = -1;
        super.onDestroy();
    }

    /**
     * Binds service (not used).
     *
     * @param intent    Intent data.
     * @return          null
     */
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * Updates timestamp.
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
