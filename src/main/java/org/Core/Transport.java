package org.Core;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.Models.*;

import java.io.IOException;

public class Transport {

    private HttpClientService http = new HttpClientService();

    private String host = "http://transport.opendata.ch/v1/";

    public Stations getStations(String query) throws Exception {
        query = transformQuery(query);
        String jsonString = "";

        if(query.equals(""))
            throw new IllegalArgumentException();

        jsonString = http.makeRequest(host + "locations?query=" + query + "&type=station");


        ObjectMapper mapper = new ObjectMapper();
        Stations stations = null;
        try{
            stations = mapper.readValue(jsonString, Stations.class);
        }catch (Exception e) {
            e.printStackTrace();
        }

        return stations;
    }



    public StationBoardRoot getStationBoard(String station, String id) {
        station = transformQuery(station);
        String jsonString = "";
        try {
            if(!station.equals("") && !id.equals(""))
                jsonString = http.makeRequest(host +"stationboard?station="+ station +"&id=" + id);
            else
                throw new Exception();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ObjectMapper mapper = new ObjectMapper();
        StationBoardRoot stationBoardRoot = null;
        try{
            stationBoardRoot = mapper.readValue(jsonString, StationBoardRoot.class);
        }catch (Exception e) {
            e.printStackTrace();
        }

        return stationBoardRoot;
    }

    public Connections getConnections(String fromStation, String toStation) {
        fromStation = transformQuery(fromStation);
        toStation = transformQuery(toStation);
        String jsonString = "";
        try {
            if(!fromStation.equals("") && !toStation.equals(""))
                jsonString = http.makeRequest(host +"connections?from="+ fromStation + "&to="+ toStation);
            else
                throw new Exception();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ObjectMapper mapper = new ObjectMapper();
        Connections connections = null;
        try{
            connections = mapper.readValue(jsonString, Connections.class);
        }catch (Exception e) {
            e.printStackTrace();
        }

        return connections;
    }

    public Connections getConnectionsWithTime(String fromStation, String toStation, String date, String time) throws IOException {
        String jsonString = "";
        String query = "connections?from="+ fromStation + "&to="+ toStation+"&date=" + date + "&time=" + time;
        query = transformQuery(query);

        if(fromStation.equals("") || toStation.equals("") || date.equals("") || time.equals(""))
            throw new IllegalArgumentException();

        jsonString = http.makeRequest(host + query);

        ObjectMapper mapper = new ObjectMapper();
        Connections connections = null;
        try{
            connections = mapper.readValue(jsonString, Connections.class);
        }catch (Exception e) {
            e.printStackTrace();
        }

        return connections;
    }

    private String transformQuery(String query) {
        return query.replaceAll("\\s+", "%20");
    }


}
