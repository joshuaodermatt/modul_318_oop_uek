package org.sbbApp;

import org.Models.Connection;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class MailService {
    private static ConnectionsTableViewObject connection = null;

    public static void setConnection(ConnectionsTableViewObject connectionToSet) {
        connection = connectionToSet;
    }

    public static ConnectionsTableViewObject getConnection() {
        return connection;
    }

    public static boolean connectionIsSet() {
        if (getConnection() != null)
            return true;
        return false;
    }

    public static void mail(String to, String subject) throws Exception {
        String body =
                "Arrival:%20" + connection.getArrival() + "%0D%0A" +
                        "Departure:%20" + connection.getDeparture() + "%0D%0A" +
                        "Platform:%20" + connection.getPlatform() + "%0D%0A" +
                        "Duration:%20" + connection.getDuration();
        Desktop desktop;
        if (Desktop.isDesktopSupported()
                && (desktop = Desktop.getDesktop()).isSupported(Desktop.Action.MAIL)) {
            URI mailto = new URI("mailto:" + to + "?subject=" + subject + "&body=" + body);
            desktop.mail(mailto);
        } else {
            throw new RuntimeException("mailingNotPossible");
        }
    }


}
