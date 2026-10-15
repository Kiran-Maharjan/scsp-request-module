package scsp.model;

import scsp.exception.InvalidStatusTransitionException;

import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

public class ServiceRequest {

    private final String requestId;
    private final String residentId;
    private final ServiceCategory category;
    private final String description;
    private final String location;
    private final LocalDateTime submittedDate;

    private RequestStatus status;
    private Priority priority;
    private String assignedTeam;

    /** Legal lifecycle transitions (BR-06) */
    private static final Map<RequestStatus, Set<RequestStatus>> ALLOWED =
            new EnumMap<>(RequestStatus.class);

    static {
        ALLOWED.put(RequestStatus.SUBMITTED,    EnumSet.of(RequestStatus.UNDER_REVIEW));
        ALLOWED.put(RequestStatus.UNDER_REVIEW, EnumSet.of(RequestStatus.ASSIGNED));
        ALLOWED.put(RequestStatus.ASSIGNED,     EnumSet.of(RequestStatus.IN_PROGRESS));
        ALLOWED.put(RequestStatus.IN_PROGRESS,  EnumSet.of(RequestStatus.COMPLETED));
        ALLOWED.put(RequestStatus.COMPLETED,    EnumSet.of(RequestStatus.CLOSED));
        ALLOWED.put(RequestStatus.CLOSED,       EnumSet.noneOf(RequestStatus.class));
    }

    public ServiceRequest(String requestId,
                          String residentId,
                          ServiceCategory category,
                          String description,
                          String location,
                          Priority priority) {
        this.requestId = requestId;
        this.residentId = residentId;
        this.category = category;
        this.description = description;
        this.location = location;
        this.priority = priority;
        this.status = RequestStatus.SUBMITTED;
        this.submittedDate = LocalDateTime.now();
    }

    /** Enforces the approved lifecycle (BR-06, BR-07). */
    public void updateStatus(RequestStatus next) {
        if (!ALLOWED.get(status).contains(next)) {
            throw new InvalidStatusTransitionException(
                    "Illegal transition: " + status + " -> " + next);
        }
        this.status = next;
    }

    /** BR-07: a request can only close after work is completed. */
    public void close() {
        if (status != RequestStatus.COMPLETED) {
            throw new InvalidStatusTransitionException(
                    "Request must be COMPLETED before it can be CLOSED.");
        }
        updateStatus(RequestStatus.CLOSED);
    }

    /** BR-05: request must be assigned before work begins. */
    public void assignTo(String team) {
        if (team == null || team.isBlank()) {
            throw new IllegalArgumentException("Team must be provided.");
        }
        this.assignedTeam = team;
        updateStatus(RequestStatus.ASSIGNED);
    }

    /** BR-08: duplicate detection based on location, category and time. */
    public boolean isDuplicateOf(ServiceRequest other) {
        if (other == null) return false;
        boolean sameLocation = this.location.equalsIgnoreCase(other.location);
        boolean sameCategory = this.category == other.category;
        boolean withinWindow = this.submittedDate
                .isBefore(other.submittedDate.plusHours(24))
                && this.submittedDate
                .isAfter(other.submittedDate.minusHours(24));
        return sameLocation && sameCategory && withinWindow;
    }

    // Getters
    public String getRequestId() { return requestId; }
    public String getResidentId() { return residentId; }
    public ServiceCategory getCategory() { return category; }
    public String getDescription() { return description; }
    public String getLocation() { return location; }
    public LocalDateTime getSubmittedDate() { return submittedDate; }
    public RequestStatus getStatus() { return status; }
    public Priority getPriority() { return priority; }
    public String getAssignedTeam() { return assignedTeam; }

    public void setPriority(Priority priority) { this.priority = priority; }

    @Override
    public String toString() {
        return String.format(
                "ServiceRequest[id=%s, category=%s, location=%s, status=%s, priority=%s, team=%s]",
                requestId, category, location, status, priority,
                assignedTeam == null ? "unassigned" : assignedTeam);
    }
}
