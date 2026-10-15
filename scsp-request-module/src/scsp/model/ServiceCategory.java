package scsp.model;

public enum ServiceCategory {
    ROAD_DAMAGE(true),
    WASTE_COLLECTION(false),
    GRAFFITI(false),
    STREET_LIGHTING(true);

    private final boolean safetyImpact;

    ServiceCategory(boolean safetyImpact) {
        this.safetyImpact = safetyImpact;
    }

    public boolean hasSafetyImpact() {
        return safetyImpact;
    }
}
