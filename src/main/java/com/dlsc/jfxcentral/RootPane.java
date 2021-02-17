package com.dlsc.jfxcentral;

import com.dlsc.jfxcentral.model.Person;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;

public class RootPane extends ViewPane {

    public RootPane() {
        getStyleClass().add("root-pane");

        SideBar sideBar = new SideBar(this);
        sideBar.viewProperty().bindBidirectional(viewProperty());

        RightPane rightPane = new RightPane(this);
        rightPane.viewProperty().bind(viewProperty());

        BorderPane borderPane = new BorderPane();
        borderPane.setLeft(sideBar);
        borderPane.setCenter(rightPane);

        getChildren().add(borderPane);

        Person dirk_lemmermann = new Person("Dirk Lemmermann");
        Person hendrik_ebbers = new Person("Hendrik Ebbers");
        Person gerrit_grunwald = new Person("Gerrit Grunwald");
        Person michael_hoffer = new Person("Michael Hoffer");

        Person jose = new Person("José Peredaz");
        Person johan = new Person("Johan Vos");
        Person andres = new Person("Andres Almiray");

        dirk_lemmermann.setChampion(true);
        dirk_lemmermann.setRockstar(true);
        dirk_lemmermann.setPhoto(new Image(RootPane.class.getResource("people/dirk.jpg").toExternalForm()));

        hendrik_ebbers.setChampion(true);
        hendrik_ebbers.setRockstar(true);
        hendrik_ebbers.setPhoto(new Image(RootPane.class.getResource("people/hendrik.png").toExternalForm()));

        gerrit_grunwald.setChampion(true);
        gerrit_grunwald.setRockstar(true);
        gerrit_grunwald.setPhoto(new Image(RootPane.class.getResource("people/gerrit.jpeg").toExternalForm()));

        michael_hoffer.setRockstar(true);
        michael_hoffer.setPhoto(new Image(RootPane.class.getResource("people/mhoffer.jpg").toExternalForm()));

        jose.setRockstar(true);
        jose.setChampion(true);
        jose.setPhoto(new Image(RootPane.class.getResource("people/jose.png").toExternalForm()));

        johan.setRockstar(true);
        johan.setChampion(true);
        johan.setPhoto(new Image(RootPane.class.getResource("people/johan.jpg").toExternalForm()));

        andres.setRockstar(true);
        andres.setChampion(true);
        andres.setPhoto(new Image(RootPane.class.getResource("people/andres.jpg").toExternalForm()));

        people.add(dirk_lemmermann);
        people.add(hendrik_ebbers);
        people.add(gerrit_grunwald);
        people.add(michael_hoffer);
        people.add(johan);
        people.add(jose);
        people.add(andres);

        people.add(dirk_lemmermann);
        people.add(hendrik_ebbers);
        people.add(gerrit_grunwald);
        people.add(michael_hoffer);
        people.add(johan);
        people.add(jose);
        people.add(andres);

        people.add(dirk_lemmermann);
        people.add(hendrik_ebbers);
        people.add(gerrit_grunwald);
        people.add(michael_hoffer);
        people.add(johan);
        people.add(jose);
        people.add(andres);

        people.add(dirk_lemmermann);
        people.add(hendrik_ebbers);
        people.add(gerrit_grunwald);
        people.add(michael_hoffer);
        people.add(johan);
        people.add(jose);
        people.add(andres);

        people.add(dirk_lemmermann);
        people.add(hendrik_ebbers);
        people.add(gerrit_grunwald);
        people.add(michael_hoffer);
        people.add(johan);
        people.add(jose);
        people.add(andres);
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
