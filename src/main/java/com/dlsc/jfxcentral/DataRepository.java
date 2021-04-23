package com.dlsc.jfxcentral;

import com.dlsc.jfxcentral.model.Library;
import com.dlsc.jfxcentral.model.Person;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.List;

public class DataRepository {

    private static final DataRepository instance = new DataRepository();

    public static synchronized DataRepository getInstance() {
        return instance;
    }

    private DataRepository() {
        try {
            Gson gson = new GsonBuilder().create();

            File peopleFile = loadFile("people.json", "https://raw.githubusercontent.com/dlemmermann/jfxcentral-data/main/people.json");
            setPeople(gson.fromJson(new FileReader(peopleFile), new TypeToken<List<Person>>() {
            }.getType()));

            File librariesFile = loadFile("libraries.json", "https://raw.githubusercontent.com/dlemmermann/jfxcentral-data/main/libraries.json");
            setLibraries(gson.fromJson(new FileReader(librariesFile), new TypeToken<List<Library>>() {
            }.getType()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ListProperty<Library> getLibrariesByPerson(Person person) {
        FilteredList<Library> filteredList = new FilteredList<>(getLibraries());
        filteredList.setPredicate(library -> library.getPersonId().equals(person.getId()));
        return new SimpleListProperty<>(filteredList);
    }

    private final ListProperty<Library> libraries = new SimpleListProperty<>(this, "libraries", FXCollections.observableArrayList());

    public ObservableList<Library> getLibraries() {
        return libraries.get();
    }

    public ListProperty<Library> librariesProperty() {
        return libraries;
    }

    public void setLibraries(List<Library> libraries) {
        this.libraries.setAll(libraries);
    }

    private final ListProperty<Person> people = new SimpleListProperty<>(this, "people", FXCollections.observableArrayList());

    public ObservableList<Person> getPeople() {
        return people.get();
    }

    public ListProperty<Person> peopleProperty() {
        return people;
    }

    public void setPeople(List<Person> people) {
        this.people.setAll(people);
    }

    private File loadFile(String fileName, String url) throws IOException {
        ReadableByteChannel readChannel = Channels.newChannel(new URL(url).openStream());
        File file = File.createTempFile(fileName, "json");
        FileOutputStream fileOS = new FileOutputStream(file);
        FileChannel writeChannel = fileOS.getChannel();
        writeChannel.transferFrom(readChannel, 0, Long.MAX_VALUE);
        return file;
    }

    public static void main(String[] args) {
        new DataRepository();
    }
}
