package scsp.repository;

import scsp.model.RequestStatus;
import scsp.model.ServiceRequest;

import java.util.*;
import java.util.stream.Collectors;

public class InMemoryRequestRepository implements RequestRepository {

    private final Map<String, ServiceRequest> store = new LinkedHashMap<>();

    @Override
    public void save(ServiceRequest request) {
        store.put(request.getRequestId(), request);
    }

    @Override
    public Optional<ServiceRequest> findById(String id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<ServiceRequest> findByResident(String residentId) {
        return store.values().stream()
                .filter(r -> r.getResidentId().equals(residentId))
                .collect(Collectors.toList());
    }

    @Override
    public List<ServiceRequest> findByStatus(RequestStatus status) {
        return store.values().stream()
                .filter(r -> r.getStatus() == status)
                .collect(Collectors.toList());
    }

    @Override
    public List<ServiceRequest> findAll() {
        return new ArrayList<>(store.values());
    }
}
