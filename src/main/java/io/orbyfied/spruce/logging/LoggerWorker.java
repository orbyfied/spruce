package io.orbyfied.spruce.logging;

import io.orbyfied.spruce.event.Record;
import io.orbyfied.spruce.logging.io.OutputWorker;

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
     * The communication for thread activation.
     */
    private final Object lock = new Object();

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
        this.start();
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
        synchronized (lock) { lock.notifyAll(); } // <- make sure that the thread wont hibernate forever
    }

    /**
     * Queues a new log request to be processed
     * and sent.
     * @param record The request.
     */
    public void queue(Record record) {
        queue.add(record);
        synchronized (lock) { lock.notifyAll(); }
    }

    /** The class for the worker thread. */
    class WorkerThread extends Thread {

        /** Internal. */
        public WorkerThread() {
            setDaemon(false);
        }

        /** Internal. */
        void waitFor() {
            try { synchronized (lock) { lock.wait(); } } catch (Exception e) { e.printStackTrace(); }
        }

        @Override
        public void run() {
            while (working.get()) {
                // hibernate if nothing is queued
                if (queue.isEmpty())
                    waitFor();
                if (!working.get()) break;

                // poll and check request
                Record record = queue.poll();
                if (record == null) continue;

                try {
                    // set raw text
                    if (!record.isLocked()) { // check if it has been locked first
                        Object o = record.getObject();
                        String t = o != null ? o.toString() : null;
                        record.setRaw(t);
                        record.setText(t);
                    }

                    // format request
                    record.getLevel().format(record);
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
                } catch (Exception e) { }
            }

            working.set(false);

        }

    }
}
