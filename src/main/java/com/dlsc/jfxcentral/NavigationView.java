package com.dlsc.jfxcentral;

import com.jpro.web.Util;
import com.jpro.web.sessionmanager.SessionManager;
import com.jpro.webapi.WebAPI;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material.Material;
import org.kordamp.ikonli.materialdesign.MaterialDesign;

public class NavigationView extends HBox {

    public NavigationView() {
        getStyleClass().add("navigation-view");

        setFillHeight(true);
        setAlignment(Pos.CENTER);

        Button refreshButton = new Button();
        refreshButton.setGraphic(new FontIcon(MaterialDesign.MDI_REFRESH));
        refreshButton.setTooltip(new Tooltip("Refresh data"));
        refreshButton.getStyleClass().add("refresh-button");
        refreshButton.setVisible(!WebAPI.isBrowser());
        refreshButton.setManaged(!WebAPI.isBrowser());
        refreshButton.setOnAction(evt -> Util.getSessionManager(this).gotoURL("/refresh"));

//        {
//            JFXCentralApp.updateRepositoryInBackground(new ProgressMonitor() {
//                double total;
//                double completed;
//
//                @Override
//                public void start(int totalTasks) {
//                    System.out.println("start: total tasks = " + totalTasks);
//                    total = totalTasks;
//                    updatePercentage();
//                }
//
//                @Override
//                public void beginTask(String title, int totalWork) {
//                    System.out.println("begin task: title = " + title + ", totalWork = " + totalWork);
////                    Platform.runLater(() -> statusLabel.setText(title));
//                    updatePercentage();
//                }
//
//                @Override
//                public void update(int completed) {
//                    System.out.println("update: completed = " + completed);
//                    this.completed = completed;
//                    updatePercentage();
//                }
//
//                @Override
//                public void endTask() {
//                    System.out.println("end task");
//                    Platform.runLater(() -> {
////                        statusLabel.setText("");
////                        percentageLabel.setText("");
//                    });
//                }
//
//                @Override
//                public boolean isCancelled() {
//                    return false;
//                }
//
//                private void updatePercentage() {
//                    Platform.runLater(() -> {
////                        percentageLabel.setText((completed / total) + "%");
////                        System.out.println("pl: " + percentageLabel.getText());
//                    });
//                }
//            }, () -> {
//            });
//            DataRepository.getInstance().loadData();
//        });

        Button back = new Button();
        back.setGraphic(new FontIcon(Material.ARROW_BACK));
        back.setOnAction(evt -> Util.goBack(this));
        back.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(back, Priority.ALWAYS);

        Button forward = new Button();
        forward.setGraphic(new FontIcon(Material.ARROW_FORWARD));
        forward.setOnAction(evt -> Util.goForward(this));
        forward.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(forward, Priority.ALWAYS);

        sceneProperty().addListener(it -> {
            if (getScene() != null) {
                SessionManager sessionManager = Util.getSessionManager(this);
                back.disableProperty().bind(Bindings.isEmpty(sessionManager.getHistoryBackward()));
                forward.disableProperty().bind(Bindings.isEmpty(sessionManager.getHistoryForwards()));
            }
        });

        back.setOnContextMenuRequested(evt -> showMenu(back, Util.getSessionManager(this).getHistoryBackward()));
        forward.setOnContextMenuRequested(evt -> showMenu(forward, Util.getSessionManager(this).getHistoryForwards()));

        if (true) { //WebAPI.isBrowser()) {
            getChildren().setAll(back, forward);
        } else {
            getChildren().setAll(refreshButton, back, forward);
        }
    }

    private void showMenu(Button back, ObservableList<String> historyBackward) {
        ContextMenu menu = new ContextMenu();
        for (int i = 0; i < Math.min(20, historyBackward.size()); i++) {
            String url = historyBackward.get(i);
            MenuItem item = new MenuItem(url);
            item.setOnAction(evt -> Util.getSessionManager(this).gotoURL(url));
            menu.getItems().add(item);
        }
        menu.show(back, Side.BOTTOM, 0, 0);
    }
}
