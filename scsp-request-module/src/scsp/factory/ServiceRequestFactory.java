package scsp.factory;

import scsp.model.Priority;
import scsp.model.ServiceCategory;
import scsp.model.ServiceRequest;
import scsp.strategy.PriorityStrategy;

import java.util.UUID;

/**
 * Factory + Strategy: creates a request, generates a unique ID (BR-01)
 * and calculates the initial priority.
 */
public class ServiceRequestFactory {

    private final PriorityStrategy priorityStrategy;

    public ServiceRequestFactory(PriorityStrategy priorityStrategy) {
        this.priorityStrategy = priorityStrategy;
    }

    public ServiceRequest create(String residentId,
                                 ServiceCategory category,
                                 String description,
                                 String location) {
        String id = "REQ-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        ServiceRequest request = new ServiceRequest(
                id, residentId, category, description, location, Priority.MEDIUM);

        request.setPriority(priorityStrategy.calculate(request));
        return request;
    }
}
