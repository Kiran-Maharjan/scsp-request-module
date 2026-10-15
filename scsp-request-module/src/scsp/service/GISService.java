package scsp.service;

public interface GISService {
    boolean validateAddress(String address);
    String resolveZone(String address);
}
