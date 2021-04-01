package org.Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class StationBoardRoot {

    @JsonProperty("station")
    public Station Station;

    @JsonProperty("stationboard")
    public ArrayList<StationBoard> Entries;
}
