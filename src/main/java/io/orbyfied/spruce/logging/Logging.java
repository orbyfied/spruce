package io.orbyfied.spruce.logging;

import io.orbyfied.spruce.arg.OutputInfo;
import io.orbyfied.spruce.logging.io.OutputWorker;

import java.util.List;

public class Logging {
    public static final List<OutputWorker> INITIAL_OUTPUTS = List.of(OutputWorker.create(OutputInfo.SYSOUT));
}
