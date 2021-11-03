package io.orbyfied.spruce.system;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.function.Consumer;

public class Capabilities extends HashSet<Capability> {
    /** Basic constructor. */
    public Capabilities() { }

    /** Direct capabilities constructor. */
    public Capabilities(Capability... capabilities) {
        this();
        this.addAll(Arrays.asList(capabilities));
    }

    /**
     * Flattens the capabilities. This means that
     * all their dependencies will be included directly.
     * @return The flattened capabilities.
     */
    public Capabilities flatten() {
        Capabilities flattened = new Capabilities();
        for (Capability capability : this)
            mAddFlatCap(capability, flattened);
        return flattened;
    }

    /** @see Capabilities#flatten() */
    private void mAddFlatCap(Capability c, Capabilities capabilities) {
        capabilities.add(c);
//        System.out.println("Capability: " + c + ", Dependencies: "
//                + Arrays.toString(c.dependencies));
        if (c.dependencies.length > 0)
            for (Capability dep : c.dependencies)
                mAddFlatCap(dep, capabilities);
    }

    /**
     * Sets all element data to the data of the specified
     * capabilities.
     * @param capabilities The capabilities.
     */
    public void set(Capabilities capabilities) {
        this.removeIf(capability -> true);
        this.addAll(capabilities);
    }

    /**
     * Retrieves and returns the specified branch.
     * @param capability The branch root.
     * @return
     */
    public Capability[] getBranch(Capability capability) {
        List<Capability> capabilities = new ArrayList<>();
        for (Capability c : Capability.values()) {
            capabilities.addAll(mGetDep(c));
        }
        return capabilities.toArray(new Capability[0]);
    }

    public void disableBranch(Capability capability) {
        for (Capability c : getBranch(capability))
            c.disable();
    }

    public void iterate(Consumer<Capability> consumer) {
        for (Capability c : this)
            consumer.accept(c);
    }

    public void iterateBranch(Capability capability, Consumer<Capability> consumer) {
        for (Capability c : getBranch(capability))
            consumer.accept(c);
    }

    public void enableAll() {
        iterate(Capability::enable);
    }

    public void disableAll() {
        iterate(Capability::disable);
    }

    /**
     * Removes the specified capability, and the capabilities that depend on it, from the list.
     * @param capability The capability to remove.
     */
    public void removeBranch(Capability capability) {
        // remove capability
        super.remove(capability);

        // iterate all capabilities
        for (Capability c : Capability.values()) {
            if (mHasDep(c, capability))
                super.remove(c);
        }
    }

    private List<Capability> mGetDep(Capability capability) {
        if (capability.dependencies.length == 0)
            return new ArrayList<>();
        List<Capability> capabilities = new ArrayList<>();
        for (Capability c : capability.dependencies) {
            if (this.contains(c))
                capabilities.addAll(mGetDep(c));
        }
        return capabilities;
    }

    private boolean mHasDep(Capability capability, Capability dep) {
        if (capability == dep)
            return true;
        for (Capability c : capability.dependencies) {
            if (mHasDep(c, dep))
                return true;
        }

        return false;
    }
}
