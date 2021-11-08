package io.orbyfied.spruce;

import io.orbyfied.spruce.arg.RemoveOutWorker;
import io.orbyfied.spruce.event.Record;
import io.orbyfied.spruce.logging.LoggerFactory;
import io.orbyfied.spruce.logging.io.OutputWorker;
import io.orbyfied.spruce.pipeline.Part;
import io.orbyfied.spruce.pipeline.Pipeline;
import io.orbyfied.spruce.standard.StandardLogger;
import io.orbyfied.spruce.standard.StandardLoggerFactory;
import io.orbyfied.spruce.system.Capability;
import io.orbyfied.spruce.arg.DisableCapability;
import io.orbyfied.spruce.system.Capabilities;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Spruce v1.0 (by orbyfied)
 * @author  orbyfied
 * @version 1.0
 * ---------------------------------------
 * Spruce library initializer and interface.
 */
public class Spruce {

    /**
     * The version of Spruce;
     */
    public static final String VERSION = "2.0.1";

    // INSTANCE CONFIGURATIONS
    public final List<OutputWorker> defaultOutputStreams = new ArrayList<>(Arrays.asList(OutputWorker.SYSOUT));
    public final Pipeline<Record> defaultPipeline = new Pipeline<>();
    public final Capabilities       capabilities;
    public final Capabilities       flatCapabilities;

    /**
     * Spruce initializer.
     * @param args The parameters.
     */
    public Spruce(Object... args) {
        // set instance
        spruce = this;

        // set defaults
        capabilities = DEFAULT_CAPABILITIES.flatten();

        // capabilities to remove
        List<Capability> remove = new ArrayList<>();

        // TODO: process parameters
        // iterate over parameters
        int l = args.length;
        for (int i = 0; i < l; i++) {
            // get param
            Object arg = args[i];

            // process
            if      (arg instanceof OutputWorker)    this.defaultOutputStreams.add((OutputWorker) arg);
            else if (arg instanceof RemoveOutWorker) this.defaultOutputStreams.remove(((RemoveOutWorker) arg).getWorker());

            else if (arg instanceof Part) defaultPipeline.addLast((Part<Record>) arg);

            else if (arg instanceof Capabilities) capabilities.set((Capabilities) arg);
            else if (arg instanceof Capability) capabilities.add((Capability) arg);
            else if (arg instanceof DisableCapability) remove.add(((DisableCapability)arg).getCapability());

            else if (arg instanceof String) {
                // get str
                String str = (String) arg;

                // test for expression
                if (str.startsWith("!")) {
                    // remove !
                    str = str.substring(1);

                    // parse
                    StringBuilder b = new StringBuilder();

                    char[] chars = str.toCharArray();
                    for (char c : chars) {
                        // TODO: impl
                    }
                }
            }
        }

        // remove capabilities
        for (Capability c : remove) {
            capabilities.disableBranch(c);
            capabilities.removeBranch(c);
        }

        /* Disable native branch because it doesn't work yet. */
        capabilities.disableBranch(Capability.NATIVES);

        // flatten capabilities
        flatCapabilities = capabilities.flatten();

        // enable capabilities
        flatCapabilities.enableAll();

        // initialize
        initialize();
    }

    private static boolean uIsValidIdChar(char c) {
        c = Character.toLowerCase(c);
        return  ((c <= '9' && c >= '0') || (c <= 'z' && c >= 'a')) || c == '_';
    }

    /**
     * The Spruce configuration instance.
     */
    private static Spruce spruce;
    public static Spruce get() { return spruce; }

    private static StandardLogger logger;

    /**
     * The Spruce data namespace.
     */
    public static final String NAMESPACE = "io.orbyfied.spruce";

    /**
     * The path to all Spruce data (like natives).
     */
    private static String DATA_PATH;
    public static String getDataPath() { return DATA_PATH; }

    /**
     * The path to all natives.
     */
    private static String NATIVES_PATH;
    public static String getNativesPath() { return NATIVES_PATH; }

    public static final Capabilities DEFAULT_CAPABILITIES = new Capabilities(
        Capability.ALL
    );

    public static boolean isInitialized() {
        return spruce != null;
    }

           static Win32 win32;
    public static Win32 win32() { return win32; }

    /**
     * Initializes Spruce.
     */
    protected static void initialize() {
        // get capabilities
        Capabilities capabilities = spruce.flatCapabilities;

        // check for core capability
        if (!capabilities.contains(Capability.CORE))
            return;

        if (capabilities.contains(Capability.LOGGING)) {
            // initialize standard logger factory
            try {
                Field f = StandardLoggerFactory.class.getDeclaredField("instance");
                f.setAccessible(true);
                f.set(null, new StandardLoggerFactory());
            } catch (Exception e) { e.printStackTrace(); }

            // initialize logger
            logger = LoggerFactory.standard().make("tag:Spruce", "id:spruce-system");
        }

        if (capabilities.contains(Capability.NATIVES)) {
            // get os name
            String os = System.getProperty("os.name").toLowerCase();

            // generate and create data path
            if (os.startsWith("windows")) {
                DATA_PATH = System.getenv("APPDATA") + "/" + NAMESPACE;
            } else { DATA_PATH = "/etc/" + NAMESPACE + ""; }

            NATIVES_PATH = DATA_PATH + "/natives";

            File f_dp = new File(DATA_PATH);
            File f_np = new File(NATIVES_PATH);
            if (!f_dp.exists())
                if (!f_dp.mkdirs())
                    logger.severe("failed to create data path (\"" + DATA_PATH + "\")");
            if (!f_np.exists())
                if (!f_np.mkdirs())
                    logger.severe("failed to create natives path (\"" + NATIVES_PATH + "\")");

            // call win32 natives
            if (os.startsWith("windows")) {
                win32 = new Win32();
                if (win32.load()) {
                    if (capabilities.contains(Capability.VTSEQ))
                        win32.fixVt();
                }
            }
        }
    }

    /** Native class which should 'fix' (enable ansi support for) the Windows console. */
    public static class Win32 {

        Win32() {}

        public static final String NATIVES_PREFIX  = "spruce-win32";
        public static final String NATIVES_VERSION = "1_3";

        /** Loads and initializes the natives and other things required for this feature. */
        public boolean load() {
            // get architecture
            String arch;
            String arch_raw = System.getProperty("os.arch");
            if (arch_raw.contains("64")) arch = "64";
            else                         arch = "32";

            // create file(name)
            String fname = NATIVES_PREFIX + "-v" + NATIVES_VERSION + "_" + arch + ".dll";
            String fpath = NATIVES_PATH   + "/"  + fname;
            File   f     = new File(fpath);

            try {
                // check if the file doesnt exist
                if (!f.exists()) {
                    // get resource stream
                    InputStream stream = Spruce.class.getResourceAsStream("/" + fname);
                    if (stream == null) {
                        logger.severe("failed to extract Win32 from jar, file does not exist. (" + fname +")");
                        return false;
                    }

                    // write data (creates the file as well)
                    Files.copy(stream, f.toPath());
                }

                // load library
                System.load(fpath);

                // initialize
                initNative();
            } catch (Exception e) {
                // log error
                logger.severe("an error occurred while loading and initializing Win32;");
                e.printStackTrace();

                // return unsuccessful
                return false;
            }

            // return successful
            return true;
        }

        /** @implNote Win32.cpp */
        public native void initNative();

        /** @implNote Win32.cpp */
        public native void fixVt();

        /** @implNote Win32.cpp */
        public native void setInFlag(long f, boolean b);

        /** @implNote Win32.cpp */
        public native void setOutFlag(long f, boolean b);

        /** @implNote Win32.cpp */
        public native void setConsoleWindowVisible(boolean b);

        /** @implNote Win32.cpp */
        public native void writeConsoleBufferDirect();

    }

}
