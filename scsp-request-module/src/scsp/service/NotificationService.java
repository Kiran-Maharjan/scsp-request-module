package scsp.service;

public interface NotificationService {
    void notifyResident(String residentId, String message);
    void notifyTeam(String team, String message);
}
