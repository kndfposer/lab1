package model;

public class Technique {
    private String name;
    private String type;
    private Sorcerer owner;
    private long damage;

    public Technique() {}

    public Technique(String name, String type, Sorcerer owner, long damage) {
        this.name = name;
        this.type = type;
        this.owner = owner;
        this.damage = damage;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Sorcerer getOwner() { return owner; }
    public void setOwner(Sorcerer owner) { this.owner = owner; }

    public long getDamage() { return damage; }
    public void setDamage(long damage) { this.damage = damage; }

    @Override
    public String toString() {
        return "Technique{name='" + name + "', type='" + type +
                "', owner=" + (owner != null ? owner.getName() : "null") +
                ", damage=" + damage + "}";
    }
}