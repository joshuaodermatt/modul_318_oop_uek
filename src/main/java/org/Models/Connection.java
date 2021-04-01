package org.Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)

public class Connection {

    @JsonProperty("duration")
    public String Duration;

    @JsonProperty("to")
    public ConnectionPoint To;

    @JsonProperty("from")
    public ConnectionPoint From;
}
