package org.Core;

import junit.framework.TestCase;
import org.Models.Connections;
import org.Models.StationBoardRoot;
import org.Models.Stations;
import org.junit.Test;

import java.io.IOException;

public class TransportTest extends TestCase {

    Transport testee = new Transport();

    @Test
    public void testGetStations() throws Exception {
        Stations stations = testee.getStations("Basel  ");

        assertEquals(10, stations.stationList.size());
    }

    public void testGetStationBoard() {
        StationBoardRoot stationBoardRoot = testee.getStationBoard(" Sursee  ", "8502007");

        assertNotNull(stationBoardRoot);
    }

    public void testGetConnections() {

        Connections connections = testee.getConnections(" Sursee   ", "Luzern");

        assertNotNull(connections);
    }

    public void testGetConnectionsWithDate() throws IOException {

        Connections connections = testee.getConnectionsWithTime(" Sursee   ", "Luzern", "28-04-2021", "12:00");

        assertNotNull(connections);
    }
}
