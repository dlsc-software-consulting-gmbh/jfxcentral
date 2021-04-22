package com.dlsc.jfxcentral;

import com.dlsc.jfxcentral.model.Person;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.BorderPane;

public class RootPane extends ViewPane {
    private RightPane rightPane;

    public RootPane() {
        getStyleClass().add("root-pane");

        SideBar sideBar = new SideBar(this);
        sideBar.viewProperty().bindBidirectional(viewProperty());

        rightPane = new RightPane(this);
        rightPane.viewProperty().bind(viewProperty());

        BorderPane borderPane = new BorderPane();
        borderPane.setLeft(sideBar);
        borderPane.setCenter(rightPane);

        getChildren().add(borderPane);

        peopleProperty().bind(DataRepository.getInstance().peopleProperty());
    }

    public RightPane getRightPane() {
        return rightPane;
    }

    private final ListProperty<Person> people = new SimpleListProperty<>(this, "people", FXCollections.observableArrayList());

    public final ObservableList<Person> getPeople() {
        return people.get();
    }

    public final ListProperty<Person> peopleProperty() {
        return people;
    }

    public final void setPeople(ObservableList<Person> people) {
        this.people.set(people);
    }
}
