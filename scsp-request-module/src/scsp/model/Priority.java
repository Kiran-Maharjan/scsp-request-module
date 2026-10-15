package scsp.model;

public enum Priority {
    CRITICAL(1),
    HIGH(2),
    MEDIUM(3),
    LOW(4);

    private final int weight;

    Priority(int weight) {
        this.weight = weight;
    }

    public int getWeight() {
        return weight;
    }
}
