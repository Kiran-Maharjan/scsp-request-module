package scsp.service;

/**
 * Adapter pattern: wraps email/SMS providers.
 */
public class NotificationAdapter implements NotificationService {

    @Override
    public void notifyResident(String residentId, String message) {
        System.out.println("This is a notification to resident with id: " + residentId + "with message: " + message);
    }

    @Override
    public void notifyTeam(String team, String message) {
        System.out.println("This is a notification to team with id: " + residentId + "with message: " + message);

    }
}
