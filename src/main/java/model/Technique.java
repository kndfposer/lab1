package model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Technique {
    private String name;
    private String type;

    @JsonIgnore
    private Sorcerer owner;

    @JsonProperty("owner")
    private String ownerName;

    private long damage;

    public Technique() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    @JsonIgnore
    public Sorcerer getOwner() { return owner; }

    @JsonIgnore
    public void setOwner(Sorcerer owner) { this.owner = owner; }

    public String getOwnerName() { return ownerName; }
    public void setOwnerName(String ownerName) { this.ownerName = ownerName; }

    public long getDamage() { return damage; }
    public void setDamage(long damage) { this.damage = damage; }
    public String getDisplayOwnerName() {
        if (owner != null && owner.getName() != null && !owner.getName().isEmpty()) {
            return owner.getName();
        }
        if (ownerName != null && !ownerName.isEmpty()) {
            return ownerName + " (маг не указан в списке участников)";
        }
        return "владелец не указан";
    }
}