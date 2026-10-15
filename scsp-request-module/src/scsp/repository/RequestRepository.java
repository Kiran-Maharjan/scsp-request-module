package scsp.repository;

import scsp.model.RequestStatus;
import scsp.model.ServiceRequest;

import java.util.List;
import java.util.Optional;

public interface RequestRepository {
    void save(ServiceRequest request);
    Optional<ServiceRequest> findById(String id);
    List<ServiceRequest> findByResident(String residentId);
    List<ServiceRequest> findByStatus(RequestStatus status);
    List<ServiceRequest> findAll();
}
