package org.Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)

public class Stop {

    @JsonProperty("departure")
    public String Departure;

    @JsonProperty("platform")
    public String platform;


}
