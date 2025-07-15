package com.github.ricedope.models;

import com.github.rhys_h_walker.Logger;

public class NetworkingFunctions {

    public static void openBrowserToHyperlink(String url) {
        try {
            java.awt.Desktop.getDesktop().browse(java.net.URI.create(url));
        } catch (java.io.IOException e) {
            Logger.logerror("Failed to open browser to URL: " + url + " \n" + e.getMessage());
        }
    }
    
}
