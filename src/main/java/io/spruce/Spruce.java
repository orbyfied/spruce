package io.spruce;

import io.spruce.arg.RemoveOutStream;
import io.spruce.event.Record;
import io.spruce.pipeline.Handler;
import io.spruce.pipeline.Pipeline;
import io.spruce.standard.StandardLogger;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
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

    // INSTANCE CONFIGURATIONS
    public final List<OutputStream> cDefaultOutputStreams = new ArrayList<>(Arrays.asList(System.out));
    public final Pipeline<Record> cDefaultPipeline      = new Pipeline<>();

    /**
     * Spruce initializer.
     * @param args The parameters.
     */
    public Spruce(Object... args) {
        // set instance
        spruce = this;

        // TODO: process parameters
        // iterate over parameters
        int l = args.length;
        for (int i = 0; i < l; i++) {
            // get param
            Object arg = args[i];

            // process
            if      (arg instanceof OutputStream)    this.cDefaultOutputStreams.add((OutputStream) arg);
            else if (arg instanceof RemoveOutStream) this.cDefaultOutputStreams.remove(((RemoveOutStream) arg).getStream());

            else if (arg instanceof Handler) { cDefaultPipeline.addLast((Handler<Record>) arg); }

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
    public static Spruce getConfigurationInstance() { return spruce; }

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

    /**
     * The version of Spruce;
     */
    public static final String VERSION = "1.0";

    public static boolean isInitialized() {
        return spruce != null;
    }

           static Win32 win32;
    public static Win32 win32() { return win32; }

    /**
     * Initializes Spruce.
     */
    protected static void initialize() {
        // initialize logger
        logger = LoggerFactory.standard().make("tag:Spruce", "id:spruce-system");

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

        // fix win32 ansi
        if (os.startsWith("windows")) {
            win32 = new Win32();
            if (win32.load()) {
                win32.fixVt();
            }
        }
    }

    /** Native class which should 'fix' (enable ansi support for) the Windows console. */
    static class Win32 {

        public static final String NATIVES_PREFIX  = "spruce-win32";
        public static final String NATIVES_VERSION = "1_0";

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

        public native void writeConsoleBufferDirect();

    }

}
