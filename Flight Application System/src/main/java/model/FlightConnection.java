package model;
import java.util.ArrayList;
import java.util.List;

public class FlightConnection {
    private List<Flight> flights = new ArrayList<>();

    public void addFlight(Flight f) { flights.add(f); }
    public List<Flight> getFlights() { return flights; }

    public int getId() {
        return this.hashCode();
    }

    public double getTotalEconomyPrice() {
        return flights.stream().mapToDouble(Flight::getEconomyClassPrice).sum();
    }

    public double getTotalFirstClassPrice() {
        return flights.stream().mapToDouble(Flight::getFirstClassPrice).sum();
    }

    // Checks available economy seats
    // availableEconomySeats is set by FlightManager after querying the ticket table
    public boolean hasEconomySeats() {
        return flights.stream().allMatch(f -> f.getAvailableEconomySeats() > 0);
    }


    // Checks available first class seats the same way
    public boolean hasFirstClassSeats() {
        return flights.stream().allMatch(f -> f.getAvailableFirstClassSeats() > 0);
    }

    public long getTotalDurationMinutes() {
        if (flights.isEmpty()) return 0;
        long start = flights.get(0).getDepartureTime().getTime();
        long end = flights.get(flights.size() - 1).getArrivalTime().getTime();
        return (end - start) / (1000 * 60);
    }
}