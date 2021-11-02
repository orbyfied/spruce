package io.spruce.logging.io;

import io.spruce.arg.OutputInfo;
import io.spruce.event.Record;
import io.spruce.logging.Logger;
import io.spruce.logging.LoggerWorker;
import io.spruce.pipeline.Pipeline;
import io.spruce.util.color.Ansi;
import io.spruce.util.color.ChatColor;

import java.io.OutputStream;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiFunction;

public class OutputWorker {

    public static final OutputWorker SYSOUT = create(OutputInfo.SYSOUT);

    public static OutputWorker create(OutputStream stream, boolean hasAnsi) {
        return new OutputWorker(OutputInfo.builder().withStream(stream).hasAnsi(hasAnsi).build());
    }

    public static OutputWorker create(OutputInfo info, Pipeline<Record> pipeline, BiFunction<Record, String, String> processor) {
        return new OutputWorker(info.toBuilder().withPipeline(pipeline).withProcessFunction(processor).build());
    }

    public static OutputWorker create(OutputInfo info, Pipeline<Record> pipeline) {
        return new OutputWorker(info.toBuilder().withPipeline(pipeline).build());
    }

    public static OutputWorker create(OutputInfo info, BiFunction<Record, String, String> processor) {
        return new OutputWorker(info.toBuilder().withProcessFunction(processor).build());
    }

    public static OutputWorker create(OutputInfo info) {
        return new OutputWorker(info);
    }

    /////////////////////////////////////////

    /**
     * The logger that this worker is attached to.
     */
    protected OutputInfo info;

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
    public OutputWorker(OutputInfo info) {
        this.info = info;
        this.thread = new WorkerThread();
        this.working = new AtomicBoolean(false);
    }

    /**
     * Gets the output info this worker uses.
     * @return The info object.
     */
    public OutputInfo getInfo() {
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
        working.set(false);
    }

    /**
     * Queues a new log request to be written.
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

                // create final text
                String finalText = record.finalText() + ChatColor.RESET + "\n";
                if (info.getProcessFunction() != null) finalText = info.getProcessFunction().apply(record, finalText);

                // write to output stream
                if (!info.hasAnsi())
                    finalText = Ansi.strip(finalText);
                info.write(finalText);
            }

            working.set(false);
        }
    }
}
