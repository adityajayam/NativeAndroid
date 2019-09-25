package demo.photogallery.threadPoolAndTask;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/*
 * ThreadPool download class
 */
public class PhotoThreadPoolDownload {
    private static PhotoThreadPoolDownload photoThreadPoolDownload;

    private ThreadPoolExecutor downloadThreadPool;

    private static int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();

    // Sets the amount of time an idle thread waits before terminating
    private static final int KEEP_ALIVE_TIME = 1;
    // Sets the Time Unit to seconds
    private static final TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;

    private PhotoThreadPoolDownload() {
        BlockingQueue<Runnable> runnableBlockingQueue = new LinkedBlockingQueue<>();
        downloadThreadPool = new ThreadPoolExecutor(NUMBER_OF_CORES,// Initial pool size
                NUMBER_OF_CORES,       // Max pool size
                KEEP_ALIVE_TIME,
                KEEP_ALIVE_TIME_UNIT,
                runnableBlockingQueue
        );
    }

    public static PhotoThreadPoolDownload getInstance() {
        if (photoThreadPoolDownload == null) {
            photoThreadPoolDownload = new PhotoThreadPoolDownload();
        }
        return photoThreadPoolDownload;
    }

    /**
     * Method which triggers the download
     *
     * @param runnable task which should be executed.
     */
    public void startDownload(Runnable runnable) {
        //add a download task to the pool.
        photoThreadPoolDownload.downloadThreadPool.execute(runnable);
    }
}
