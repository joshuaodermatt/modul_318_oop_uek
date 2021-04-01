package org.sbbApp;

public class StationBoardTableViewObject {
    private String departure;
    private String name;
    private String platform;
    private String destination;

    public StationBoardTableViewObject(String departure, String name, String platform, String destination) {
        this.departure = departure;
        this.name = name;
        this.platform = platform;
        this.destination = destination;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
