package scsp.strategy;

import scsp.model.Priority;
import scsp.model.ServiceRequest;

public interface PriorityStrategy {
    Priority calculate(ServiceRequest request);
}
