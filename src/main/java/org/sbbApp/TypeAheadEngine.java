package org.sbbApp;

import org.Core.Transport;
import org.Models.Station;
import org.Models.Stations;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class TypeAheadEngine {
    private Transport transport;

    private ArrayList<String> suggestions = new ArrayList<>();

    public TypeAheadEngine() {
        setTransport(new Transport());
    }

    private void setTransport(Transport transport) {
        this.transport = transport;
    }

    public void updateSuggestions(String input) throws Exception {
        Stations stations = null;

        if (!input.equals(""))
            stations = transport.getStations(input);

        suggestions.clear();

        if (stations != null) {
            if (stations.stationList.size() > 0) {
                for (Station station : stations.stationList) {
                    suggestions.add(station.Name);
                }
            }
        }

    }


    public ArrayList<String> getSuggestions() {
        return suggestions;
    }


}
