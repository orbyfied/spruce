package io.orbyfied.spruce.logging.process;

import io.orbyfied.spruce.event.Record;

public interface Formatter {
    void format(Record record);
}
