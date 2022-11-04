package com.myuuiii.nexusdefend.entities;

import org.bukkit.Location;

public class NexusLocation {
    public int NexusId;
    public Location Location;

    public NexusLocation(int nexusId, org.bukkit.Location location) {
        NexusId = nexusId;
        Location = location;
    }

    public int getNexusId() {
        return NexusId;
    }

    public void setNexusId(int nexusId) {
        NexusId = nexusId;
    }

    public org.bukkit.Location getLocation() {
        return Location;
    }

    public void setLocation(org.bukkit.Location location) {
        Location = location;
    }
}
