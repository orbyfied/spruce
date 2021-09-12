package io.spruce.arg;

import io.spruce.system.Capability;

public class DisableCapability {
    Capability capability;

    public DisableCapability(Capability capability) { this.capability = capability; }

    public Capability getCapability() {
        return capability;
    }
}
