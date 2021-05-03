package com.dlsc.jfxcentral;

import com.dlsc.jfxcentral.model.Book;
import com.dlsc.jfxcentral.model.Library;
import com.dlsc.jfxcentral.model.Person;
import com.fatboyindustrial.gsonjavatime.Converters;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.beans.property.MapProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class DataRepository {

    private ExecutorService executor = Executors.newCachedThreadPool();

    private static final DataRepository instance = new DataRepository();

//    private final Gson gson = Converters.registerLocalDate(new GsonBuilder()).setPrettyPrinting().create();

    private final Gson gson = Converters.registerLocalDate(new GsonBuilder()).setPrettyPrinting().create();

    public static synchronized DataRepository getInstance() {
        return instance;
    }

    private DataRepository() {
        try {
            // load people
            File peopleFile = loadFile("people.json", "https://raw.githubusercontent.com/dlemmermann/jfxcentral-data/main/people.json");
            setPeople(gson.fromJson(new FileReader(peopleFile), new TypeToken<List<Person>>() {
            }.getType()));

            // load books
            File booksFile = loadFile("books.json", "https://raw.githubusercontent.com/dlemmermann/jfxcentral-data/main/books.json");
            setBooks(gson.fromJson(new FileReader(booksFile), new TypeToken<List<Book>>() {
            }.getType()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ListProperty<Library> getLibrariesByPerson(Person person) {

        ObservableList<Library> list = FXCollections.observableArrayList();

        person.getLibraryIds().forEach(libraryId -> {
            if (libraries.containsKey(libraryId)) {
                list.add(libraries.get(libraryId));
            } else {
//                executor.submit(() -> {
                    try {
                        File file = loadFile(libraryId + ".json", "https://raw.githubusercontent.com/dlemmermann/jfxcentral-data/main/libraries/" + libraryId + "/_info.json");
                        Library result = gson.fromJson(new FileReader(file), Library.class);

                        Platform.runLater(() -> {
                            getLibraries().put(libraryId, result);
                            list.add(result);
                        });
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
//                });
            }
        });

        return new SimpleListProperty<>(list);
    }

    public ListProperty<Book> getBooksByPerson(Person person) {
        ObservableList<Book> list = FXCollections.observableArrayList();
        list.setAll(getBooks().stream().filter(book -> book.getPersonIds().contains(person.getId())).collect(Collectors.toList()));
        return new SimpleListProperty<>(list);
    }

    private final MapProperty<String, Library> libraries = new SimpleMapProperty<>(this, "libraries", FXCollections.observableHashMap());

    public ObservableMap<String, Library> getLibraries() {
        return libraries.get();
    }

    public MapProperty<String, Library> librariesProperty() {
        return libraries;
    }

    public void setLibraries(ObservableMap<String, Library> libraries) {
        this.libraries.set(libraries);
    }

    private final ListProperty<Book> books = new SimpleListProperty<>(this, "books", FXCollections.observableArrayList());

    public ObservableList<Book> getBooks() {
        return books.get();
    }

    public ListProperty<Book> booksProperty() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books.setAll(books);
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
        System.out.println("url: " + url);
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
