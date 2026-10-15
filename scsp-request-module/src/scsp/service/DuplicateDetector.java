package scsp.service;

import scsp.model.ServiceRequest;
import scsp.repository.RequestRepository;

import java.util.Optional;

/** BR-08: identifies potentially duplicate requests. */
public class DuplicateDetector {

    private final RequestRepository repository;

    public DuplicateDetector(RequestRepository repository) {
        this.repository = repository;
    }

    public Optional<ServiceRequest> findDuplicate(ServiceRequest candidate) {
        return repository.findAll().stream()
                .filter(existing -> !existing.getRequestId()
                        .equals(candidate.getRequestId()))
                .filter(existing -> existing.isDuplicateOf(candidate))
                .findFirst();
    }
}
