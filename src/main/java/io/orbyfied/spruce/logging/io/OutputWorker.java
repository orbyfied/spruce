package io.orbyfied.spruce.logging.io;

import io.orbyfied.spruce.pipeline.Pipeline;
import io.orbyfied.spruce.util.color.Ansi;
import io.orbyfied.spruce.util.color.TextFormat;
import io.orbyfied.spruce.event.Record;

import java.io.OutputStream;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiFunction;

/**
 * Asynchronous processor/writer class.
 */
public class OutputWorker {

    /**
     * <code>System.out</code> output worker.
     */
    public static final OutputWorker SYSOUT = create(Output.SYSOUT);

    public static OutputWorker create(OutputStream stream, boolean hasAnsi) {
        return new OutputWorker(Output.builder().withStream(stream).hasAnsi(hasAnsi).build());
    }

    public static OutputWorker create(Output info, Pipeline<Record> pipeline, BiFunction<Record, String, String> processor) {
        return new OutputWorker(info.toBuilder().withPipeline(pipeline).withProcessFunction(processor).build());
    }

    public static OutputWorker create(Output info, Pipeline<Record> pipeline) {
        return new OutputWorker(info.toBuilder().withPipeline(pipeline).build());
    }

    public static OutputWorker create(Output info, BiFunction<Record, String, String> processor) {
        return new OutputWorker(info.toBuilder().withProcessFunction(processor).build());
    }

    public static OutputWorker create(Output info) {
        return new OutputWorker(info);
    }

    /////////////////////////////////////////

    /**
     * The logger that this worker is attached to.
     */
    protected Output info;

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
     * @param info The information about the output
     *             it will be writing to.
     */
    public OutputWorker(Output info) {
        this.info = info;
        this.thread = new WorkerThread();
        this.working = new AtomicBoolean(false);
    }

    /**
     * Gets the output info this worker uses.
     * @return The info object.
     */
    public Output getInfo() {
        return info;
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
        try {
            thread.stop();
        } catch (Exception e) { e.printStackTrace(); }
        working.set(false);
    }

    /**
     * Queues a new log request to be written.
     * @param record The request.
     */
    public void queue(Record record) {
        queue.add(record);
        if (!thread.isAlive()) start();
    }

    /** The class for the worker thread. */
    class WorkerThread extends Thread {
        @Override
        public void run() {
            while (working.get() && !queue.isEmpty()) {
                // poll and check request
                Record record = queue.poll();
                if (record == null) continue;

                try {
                    // create final text
                    String finalText = record.finalText() + TextFormat.RESET + "\n";
                    if (info.getProcessFunction() != null) finalText = info.getProcessFunction().apply(record, finalText);

                    // write to output stream
                    if (!info.hasAnsi())
                        finalText = Ansi.strip(finalText);
                    info.write(finalText);
                } catch (Exception e) { e.printStackTrace(); }
            }

            working.set(false);
        }
    }
}
