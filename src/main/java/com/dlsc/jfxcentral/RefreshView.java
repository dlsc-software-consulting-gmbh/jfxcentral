package com.dlsc.jfxcentral;

import com.dlsc.jfxcentral.data.DataRepository;
import com.dlsc.jfxcentral.views.DukeAnimationView;
import javafx.scene.Node;

public class RefreshView extends com.jpro.web.View {


    @Override
    public String title() {
        return "Refresh";
    }

    @Override
    public String description() {
        return "Refreshes the local repository.";
    }

    @Override
    public Node content() {
        DukeAnimationView view = new DukeAnimationView(() -> {
        });
        view.setEndText("Done!");
        view.showLastImage();

        JFXCentralApp.updateRepositoryInBackground(view, () -> DataRepository.getInstance().loadData());

        return view;
    }

    @Override
    public boolean fullscreen() {
        return true;
    }

    @Override
    public boolean handleURL(String x) {
        return true;
    }
}
