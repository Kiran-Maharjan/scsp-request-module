package scsp.service;

import scsp.factory.ServiceRequestFactory;
import scsp.model.RequestStatus;
import scsp.model.ServiceCategory;
import scsp.model.ServiceRequest;
import scsp.repository.RequestRepository;

import java.util.List;

/**
 * Application service orchestrating the Service Request Lifecycle module.
 */
public class RequestService {

    private final RequestRepository repository;
    private final ServiceRequestFactory factory;
    private final DuplicateDetector duplicateDetector;
    private final GISService gisService;
    private final NotificationService notificationService;

    public RequestService(RequestRepository repository,
                          ServiceRequestFactory factory,
                          DuplicateDetector duplicateDetector,
                          GISService gisService,
                          NotificationService notificationService) {
        this.repository = repository;
        this.factory = factory;
        this.duplicateDetector = duplicateDetector;
        this.gisService = gisService;
        this.notificationService = notificationService;
    }

    /** FR-06 to FR-11: create a service request with validation and duplicate check. */
    public ServiceRequest createRequest(String residentId,
                                        ServiceCategory category,
                                        String description,
                                        String location) {

        if (!gisService.validateAddress(location)) {
            throw new IllegalArgumentException("Invalid location: " + location);
        }

        ServiceRequest request = factory.create(residentId, category, description, location);

        duplicateDetector.findDuplicate(request).ifPresent(dup -> {
            System.out.println("[WARN] Possible duplicate of " + dup.getRequestId());
        });

        repository.save(request);
        notificationService.notifyResident(
                residentId, "Your request " + request.getRequestId() + " was submitted.");
        return request;
    }

    /** FR-12 / FR-13: assign a request to a maintenance team. */
    public void assignRequest(String requestId, String team) {
        ServiceRequest request = getOrThrow(requestId);
        request.updateStatus(RequestStatus.UNDER_REVIEW);  // forced intermediate step
        request.assignTo(team);                            // moves to ASSIGNED
        repository.save(request);
        notificationService.notifyTeam(team, "New task assigned: " + requestId);
    }

    public void startWork(String requestId) {
        ServiceRequest request = getOrThrow(requestId);
        request.updateStatus(RequestStatus.IN_PROGRESS);
        repository.save(request);
    }

    public void completeWork(String requestId) {
        ServiceRequest request = getOrThrow(requestId);
        request.updateStatus(RequestStatus.COMPLETED);
        repository.save(request);
        notificationService.notifyResident(
                request.getResidentId(),
                "Your request " + requestId + " has been completed.");
    }

    public void closeRequest(String requestId) {
        ServiceRequest request = getOrThrow(requestId);
        request.close();
        repository.save(request);
    }

    public List<ServiceRequest> listAll() {
        return repository.findAll();
    }

    public List<ServiceRequest> listByStatus(RequestStatus status) {
        return repository.findByStatus(status);
    }

    private ServiceRequest getOrThrow(String requestId) {
        return repository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Request not found: " + requestId));
    }
}
