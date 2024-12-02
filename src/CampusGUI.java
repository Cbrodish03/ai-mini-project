package src;

import javax.swing.*;

/**
 * Simple GUI to display the Campus
 * 
 * @author cbrodish03
 * @version 11/26/2024
 */
public class CampusGUI extends JFrame {

    // - Fields

    private CampusMap map;

    // - Constructor(s)

    /**
     * Default constructor for the CampusGUI object
     *
     * @param map the map object to visualize
     */
    public CampusGUI(CampusMap map) {
        this.map = map;

    }

    // - Methods

    /**
     *
     */
    private void initializeUI() {
        setTitle("Campus map GUI");
        setSize(100, 100);
    }
}