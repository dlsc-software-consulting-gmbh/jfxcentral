package com.dlsc.jfxcentral;

import de.sandec.jmemorybuddy.JMemoryBuddyLive;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class MemoryView extends com.jpro.web.View {


    @Override
    public String title() {
        return "Memory";
    }

    @Override
    public String description() {
        return "Displays the memory usage of JFX-Central";
    }

    @Override
    public Node content() {
        System.gc();

        JMemoryBuddyLive.Report report = JMemoryBuddyLive.getReport();

        VBox res = new VBox();
        res.getChildren().add(new Label("collected: " + report.collectedEntries));
        res.getChildren().add(new Label("uncollected: " + report.uncollectedEntries.size()));

        for (JMemoryBuddyLive.CollectableEntry entry : report.uncollectedEntries) {
            res.getChildren().add(new Label("entry: " + entry.name));
        }

        return res;
    }

}
