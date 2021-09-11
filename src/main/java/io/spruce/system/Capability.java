package io.spruce.system;

import javax.print.attribute.standard.MediaSize;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum Capability {
    CORE("core", null, null),
    LOGGING("logging", null, null, CORE),
    NATIVES("natives", null, null, CORE),
    VTSEQ("vtseq", null, null, NATIVES),
    ALL("_all", null, null);

    static {
        // set dependents
        List<Capability> c = new ArrayList<>(Arrays.asList(values()));
        c.remove(ALL);
        ALL.dependencies = c.toArray(new Capability[0]);
    }

    /**
     * The basic name of the capability.
     */
    String name;

    /**
     * The dependencies/parents of the capability.
     */
    Capability[] dependencies;

    /**
     * The 'enabler' of this capability.
     */
    Enabler enabler;

    /**
     * The 'disabler' of this capability.
     */
    Disabler disabler;

    /**
     * Is this capability enabled?
     */
    boolean isEnabled = false;

    /** Base constructor. */
    Capability(String name, Enabler enabler, Disabler disabler, Capability... dependencies) {
        this.name = name;
        this.dependencies = dependencies;
        this.enabler = enabler;
        this.disabler = disabler;
    }

    public void enable() {
        boolean b = true;
        if (enabler != null)
            b = enabler.onEnable();
        if (!b) return;
        isEnabled = true;
    }

    public void disable() {
        boolean b = true;
        if (disabler != null)
            b = disabler.onDisable();
        if (!b) return;
        isEnabled = false;
    }

    public boolean isEnabled() { return isEnabled; }
    public Disabler getDisabler() { return disabler; }
    public Enabler getEnabler() { return enabler; }
    public String getName() { return name; }
    public Capability[] getDependencies() { return dependencies; }

    public interface Enabler {
        boolean onEnable();
    }

    public interface Disabler {
        boolean onDisable();
    }
}
