package org.Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class Stations {

    @JsonProperty("stations")
    public ArrayList<Station> stationList;
}
