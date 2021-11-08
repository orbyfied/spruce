package io.orbyfied.spruce.event;

import io.orbyfied.spruce.logging.LogType;
import io.orbyfied.spruce.logging.Logger;

import java.util.Map;

public class Record extends LoggerEvent {
    String   raw;
    LogType level;

    Object[] other;

    StringBuilder prefixBuilder;
    StringBuilder finalTextBuilder;
    StringBuilder suffixBuilder;

    public Record(Logger logger, String raw, LogType level, String currentFinalText, Object[] other) {
        super(logger);

        this.raw    = raw;
        this.level  = level;

        this.other = other;

        this.finalTextBuilder = new StringBuilder(currentFinalText);
        this.prefixBuilder    = new StringBuilder();
        this.suffixBuilder    = new StringBuilder();
    }

    /*                */
    /* Basic getters. */
    /*                */

    public LogType  getLevel() { return level; }
    public String   getRaw()   { return raw;   }
    public Object[] getOther() { return other; }

    public Map<String, Object> getCarry() {
        return carry;
    }

    /**
     * Returns the StringBuilder for the final used/printed text.
     * @return The StringBuilder object.
     */
    public StringBuilder text() { return finalTextBuilder; }

    /**
     * Returns the StringBuilder for the final used/printed prefix.
     * @return The StringBuilder object.
     */
    public StringBuilder prefix() { return prefixBuilder; }

    /**
     * Returns the StringBuilder for the final used/printed suffix.
     * @return The StringBuilder object.
     */
    public StringBuilder suffix() { return suffixBuilder; }

    /**
     * Builds the final text that will be written
     * to the output.
     * @return The final text.
     */
    public String finalText() {
        return prefixBuilder.toString()
                + finalTextBuilder
                + suffixBuilder.toString();
    }

    /**
     * Sets the main text.
     * @param text The text to be used.
     */
    public void setText(String text) {
        this.finalTextBuilder = new StringBuilder(text);
    }

    /**
     * Sets the prefix.
     * @param prefix The prefix to be used.
     */
    public void setPrefix(String prefix) {
        this.prefixBuilder = new StringBuilder(prefix);
    }

    /**
     * Sets the suffix.
     * @param suffix The suffix to be used.
     */
    public void setSuffix(String suffix) {
        this.suffixBuilder = new StringBuilder(suffix);
    }
}
