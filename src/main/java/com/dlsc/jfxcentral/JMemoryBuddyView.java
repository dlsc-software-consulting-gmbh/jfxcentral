package com.dlsc.jfxcentral;

import com.dlsc.jfxcentral.views.page.Page;
import de.sandec.jmemorybuddy.JMemoryBuddyLive;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class JMemoryBuddyView extends com.jpro.web.View {


    @Override
    public String title() {
        return "JMemoryBuddy";
    }

    @Override
    public String description() {
        return "JMemoryBuddy";
    }

    @Override
    public Node content() {
        System.gc();
        VBox res = new VBox();
        JMemoryBuddyLive.Report report = JMemoryBuddyLive.getReport();
        res.getChildren().add(new Label("collected: " + report.collectedEntries));
        res.getChildren().add(new Label("uncollected: " + report.uncollectedEntries.size()));

        for(JMemoryBuddyLive.CollectableEntry entry: report.uncollectedEntries) {
            res.getChildren().add(new Label("entry: " + entry.name));
        }

        return res;
    }

}
