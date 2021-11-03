package io.orbyfied.spruce.arg;

import io.orbyfied.spruce.system.Capability;

public class DisableCapability {
    Capability capability;

    public DisableCapability(Capability capability) { this.capability = capability; }

    public Capability getCapability() {
        return capability;
    }
}
