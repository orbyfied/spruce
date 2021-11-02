package io.spruce.logging;

import io.spruce.arg.OutputInfo;
import io.spruce.logging.io.OutputWorker;

import java.util.ArrayList;
import java.util.List;

public class Logging {
    public static final List<OutputWorker> INITIAL_OUTPUTS = List.of(OutputWorker.create(OutputInfo.SYSOUT));
}
