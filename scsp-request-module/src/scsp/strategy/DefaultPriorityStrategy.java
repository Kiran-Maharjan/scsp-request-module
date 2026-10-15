package scsp.strategy;

import scsp.model.Priority;
import scsp.model.ServiceRequest;

/**
 * BR-03 / BR-04: priority based on severity and safety impact.
 */
public class DefaultPriorityStrategy implements PriorityStrategy {

    @Override
    public Priority calculate(ServiceRequest request) {
        boolean safety = request.getCategory().hasSafetyImpact();
        String text = request.getDescription().toLowerCase();

        boolean severe = text.contains("danger")
                || text.contains("injury")
                || text.contains("hazard")
                || text.contains("exposed");

        if (safety && severe) return Priority.CRITICAL;
        if (safety)           return Priority.HIGH;
        if (severe)           return Priority.HIGH;
        return Priority.MEDIUM;
    }
}
