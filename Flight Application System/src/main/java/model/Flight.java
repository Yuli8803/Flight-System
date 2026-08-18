package model;
import java.sql.Date;
import java.sql.Time;

public class Flight {
    private String airlineCode;
    private int flightNumber;
    private Date flightDate;
    private String departureAirportCode;
    private String arrivalAirportCode;
    private Time departureTime;
    private Time arrivalTime;
    private int firstClassCapacity;
    private int economyClassCapacity;
    private double firstClassPrice;
    private double economyClassPrice;


    // These are populated by FlightManager after querying the ticket table
    private int availableEconomySeats;
    private int availableFirstClassSeats;

    public Flight() {}

    public Flight(String airlineCode, int flightNumber, Date flightDate, String departureAirportCode,
                  String arrivalAirportCode, Time departureTime, Time arrivalTime,
                  int firstClassCapacity, int economyClassCapacity,
                  double firstClassPrice, double economyClassPrice) {
        this.airlineCode = airlineCode;
        this.flightNumber = flightNumber;
        this.flightDate = flightDate;
        this.departureAirportCode = departureAirportCode;
        this.arrivalAirportCode = arrivalAirportCode;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.firstClassCapacity = firstClassCapacity;
        this.economyClassCapacity = economyClassCapacity;
        this.firstClassPrice = firstClassPrice;
        this.economyClassPrice = economyClassPrice;
        // Default available = full capacity until FlightManager sets them
        this.availableEconomySeats = economyClassCapacity;
        this.availableFirstClassSeats = firstClassCapacity;
    }

    // Getters and Setters
    public String getAirlineCode() { return airlineCode; }
    public void setAirlineCode(String airlineCode) { this.airlineCode = airlineCode; }

    public int getFlightNumber() { return flightNumber; }
    public void setFlightNumber(int flightNumber) { this.flightNumber = flightNumber; }

    public Date getFlightDate() { return flightDate; }
    public void setFlightDate(Date flightDate) { this.flightDate = flightDate; }

    public String getDepartureAirportCode() { return departureAirportCode; }
    public void setDepartureAirportCode(String code) { this.departureAirportCode = code; }

    public String getArrivalAirportCode() { return arrivalAirportCode; }
    public void setArrivalAirportCode(String code) { this.arrivalAirportCode = code; }

    public Time getDepartureTime() { return departureTime; }
    public void setDepartureTime(Time departureTime) { this.departureTime = departureTime; }

    public Time getArrivalTime() { return arrivalTime; }
    public void setArrivalTime(Time arrivalTime) { this.arrivalTime = arrivalTime; }

    public int getFirstClassCapacity() { return firstClassCapacity; }
    public void setFirstClassCapacity(int cap) { this.firstClassCapacity = cap; }

    public int getEconomyClassCapacity() { return economyClassCapacity; }
    public void setEconomyClassCapacity(int cap) { this.economyClassCapacity = cap; }

    public double getFirstClassPrice() { return firstClassPrice; }
    public void setFirstClassPrice(double price) { this.firstClassPrice = price; }

    public double getEconomyClassPrice() { return economyClassPrice; }
    public void setEconomyClassPrice(double price) { this.economyClassPrice = price; }

    public int getAvailableEconomySeats() { return availableEconomySeats; }
    public void setAvailableEconomySeats(int seats) { this.availableEconomySeats = seats; }

    public int getAvailableFirstClassSeats() { return availableFirstClassSeats; }
    public void setAvailableFirstClassSeats(int seats) { this.availableFirstClassSeats = seats; }
}