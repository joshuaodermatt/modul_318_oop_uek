package org.sbbApp;

import javafx.scene.input.KeyEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    public static void clearSpecialChars(KeyEvent keyEvent) {
        String input = "";
        try {
            input += keyEvent.getCharacter().charAt(0);
            Pattern pattern = Pattern.compile("[^A-Za-z0-9, äöü'èéàç]");
            Matcher matcher = pattern.matcher(input);
            if (matcher.find())
                keyEvent.consume();
        } catch (Exception exception) {
        }
    }
}
