package org.Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(value = { "icon" })

public class Station {

    @JsonProperty("id")
    public String Id;

    @JsonProperty("name")
    public String Name;

    @JsonProperty("score")
    public int Score;

    @JsonProperty("coordinate")
    public Coordinate Coordinate;

    @JsonProperty("distance")
    public double Distance;
}
