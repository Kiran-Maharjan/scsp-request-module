package scsp;

import scsp.exception.InvalidStatusTransitionException;
import scsp.factory.ServiceRequestFactory;
import scsp.model.Priority;
import scsp.model.RequestStatus;
import scsp.model.ServiceCategory;
import scsp.model.ServiceRequest;
import scsp.repository.InMemoryRequestRepository;
import scsp.repository.RequestRepository;
import scsp.service.*;
import scsp.strategy.DefaultPriorityStrategy;

public class Main {

    public static void main(String[] args) {

        // --- Wiring (manual dependency injection) ---
        RequestRepository repository = new InMemoryRequestRepository();
        ServiceRequestFactory factory =
                new ServiceRequestFactory(new DefaultPriorityStrategy());
        DuplicateDetector detector = new DuplicateDetector(repository);
        GISService gis = new GISAdapter();
        NotificationService notifier = new NotificationAdapter();

        RequestService service = new RequestService(
                repository, factory, detector, gis, notifier);

        System.out.println("=== Smart Community Services Platform - Request Module Demo ===\n");

        // 1. Resident reports a hazardous road issue
        ServiceRequest r1 = service.createRequest(
                "RES-001",
                ServiceCategory.ROAD_DAMAGE,
                "Large pothole causing danger to cyclists",
                "12 Main Street, Greenfield");
        System.out.println("Created: " + r1);
        System.out.println("Priority (expected CRITICAL): " + r1.getPriority());

        // 2. Duplicate report at the same location/category
        System.out.println("\n-- Submitting a duplicate report --");
        ServiceRequest r2 = service.createRequest(
                "RES-002",
                ServiceCategory.ROAD_DAMAGE,
                "Pothole on Main Street",
                "12 Main Street, Greenfield");
        System.out.println("Created: " + r2);

        // 3. Council workflow: review -> assign -> start -> complete -> close
        System.out.println("\n-- Council workflow --");
        service.reviewRequest(r1.getRequestId());
        System.out.println("After review:     " + r1);

        service.assignRequest(r1.getRequestId(), "Roads Maintenance Team A");
        System.out.println("After assignment: " + r1);

        service.startWork(r1.getRequestId());
        System.out.println("After start:      " + r1);

        service.completeWork(r1.getRequestId());
        System.out.println("After completion: " + r1);

        service.closeRequest(r1.getRequestId());
        System.out.println("After closure:    " + r1);

        // 4. Demonstrate illegal transition (should throw)
        System.out.println("\n-- Attempting illegal transition on closed request --");
        try {
            service.startWork(r1.getRequestId());
        } catch (InvalidStatusTransitionException ex) {
            System.out.println("Blocked as expected: " + ex.getMessage());
        }

        // 5. List requests sorted by priority
        System.out.println("\n-- Listing all requests (sorted by priority) --");
        service.listAll().forEach(System.out::println);

        System.out.println("\nRequests still in SUBMITTED state: "
                + service.listByStatus(RequestStatus.SUBMITTED).size());
    }
}
