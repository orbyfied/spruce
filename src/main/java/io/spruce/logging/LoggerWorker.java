package io.spruce.logging;

import io.spruce.event.Record;
import io.spruce.logging.io.OutputWorker;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Asynchronous queue worker/processor attached to loggers.
 */
public class LoggerWorker {

    /**
     * The logger that this worker is attached to.
     */
    protected Logger logger;

    /**
     * The worker thread.
     */
    protected Thread thread;

    /**
     * If the worker thread is currently running.
     */
    protected AtomicBoolean working;

    /**
     * The queue of pending log requests or "Records".
     */
    protected Queue<Record> queue = new ArrayDeque<>();

    /**
     * Basic constructor.
     * @param logger The logger to bind this worker to.
     */
    public LoggerWorker(Logger logger) {
        this.logger = logger;
        this.thread = new WorkerThread();
        this.working = new AtomicBoolean(false);
    }

    /**
     * Gets the logger which this worker is bound to.
     * @return The logger object.
     */
    public Logger getLogger() {
        return logger;
    }

    /**
     * Checks if the thread is currently active.
     * @return True/false.
     */
    public boolean isActive() {
        return working.get();
    }

    /**
     * Starts the worker thread.
     */
    public void start() {
        working.set(true);
        thread.start();
    }

    /**
     * Stops the worker thread.
     */
    public void stop() {
        working.set(false);
    }

    /**
     * Queues a new log request to be processed
     * and sent.
     * @param record The request.
     */
    public void queue(Record record) {
        queue.add(record);
        if (!working.get()) start();
    }

    /** The class for the worker thread. */
    class WorkerThread extends Thread {
        @Override
        public void run() {
            while (working.get() && !queue.isEmpty()) {
                // poll and check request
                Record record = queue.poll();
                if (record == null) continue;

                // format request
                String tf = logger.format0(
                        record.text().toString(),
                        record.getLevel(),
                        record.getOther()
                );
                record.setText(tf);

                // pass request through pipeline
                logger.pipeline().in(record);
                if (record.isCancelled()) continue;

                // queue for output to all output workers
                for (OutputWorker worker : logger.write0(record))
                    worker.queue(record);
            }

            working.set(false);

        }
    }
}
