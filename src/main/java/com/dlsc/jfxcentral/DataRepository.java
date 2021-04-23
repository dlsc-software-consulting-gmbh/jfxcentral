package com.dlsc.jfxcentral;

import com.dlsc.jfxcentral.model.Person;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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

    public DataRepository() {
        try {
            File peopleFile = loadFile("people.json", "https://raw.githubusercontent.com/dlemmermann/jfxcentral-data/main/people.json");

            Gson gson = new GsonBuilder().create();

            List<Person> list = gson.fromJson(new FileReader(peopleFile), new TypeToken<List<Person>>() {
            }.getType());

            setPeople(list);

        } catch (IOException e) {
            e.printStackTrace();
        }
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
