package com.myuuiii.nexusdefend.entities;

import org.bukkit.Location;

public class NexusLocation {
    public String NexusId;
    public Location Location;
    public Integer Health;

    public NexusLocation(String nexusId, org.bukkit.Location location, int health) {
        NexusId = nexusId;
        Location = location;
        Health = health;
    }

    public String getNexusId() {
        return NexusId;
    }

    public void setNexusId(String nexusId) {
        NexusId = nexusId;
    }

    public org.bukkit.Location getLocation() {
        return Location;
    }

    public void setLocation(org.bukkit.Location location) {
        Location = location;
    }
}
