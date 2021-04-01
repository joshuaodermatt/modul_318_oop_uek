package org.sbbApp;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

public class ConnectionsTableViewObject {
    private String departure;
    private String arrival;
    private String platform;
    private String destination;
    private String duration;

    public ConnectionsTableViewObject() {
        this.departure = null;
        this.arrival = null;
        this.platform = "";
        this.destination = "";
    }

    public ConnectionsTableViewObject(String departure, String arrival, String platform, String duration) {
        this.departure = departure;
        this.arrival = arrival;
        this.platform = platform;
        this.duration = duration;
    }


    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getArrival() {
        return arrival;
    }

    public void setArrival(String arrival) {
        this.arrival = arrival;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }


    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
