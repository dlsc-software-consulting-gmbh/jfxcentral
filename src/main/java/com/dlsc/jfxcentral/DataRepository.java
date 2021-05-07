package com.dlsc.jfxcentral;

import com.dlsc.jfxcentral.model.*;
import com.fatboyindustrial.gsonjavatime.Converters;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
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
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class DataRepository {

    private ExecutorService executor = Executors.newCachedThreadPool();

    private static final DataRepository instance = new DataRepository();

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

            // load videos
            File videosFile = loadFile("videos.json", "https://raw.githubusercontent.com/dlemmermann/jfxcentral-data/main/videos.json");
            setVideos(gson.fromJson(new FileReader(videosFile), new TypeToken<List<Video>>() {
            }.getType()));

            // load libraries
            File librariesFile = loadFile("libraries.json", "https://raw.githubusercontent.com/dlemmermann/jfxcentral-data/main/libraries.json");
            setLibraries(gson.fromJson(new FileReader(librariesFile), new TypeToken<List<Library>>() {
            }.getType()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Optional<Person> getPersonById(String id) {
        return people.stream().filter(person -> person.getId().equals(id)).findFirst();
    }

    public ListProperty<Video> getVideosByPerson(Person person) {
        ListProperty<Video> listProperty = new SimpleListProperty<>(FXCollections.observableArrayList());
        listProperty.setAll(videos.stream().filter(video -> video.getPersonIds().contains(person.getId())).collect(Collectors.toList()));
        return listProperty;
    }

    public ListProperty<Library> getLibrariesByPerson(Person person) {
        List<Library> result = libraries.stream().filter(library -> library.getPersonId().equals(person.getId())).collect(Collectors.toList());
        return new SimpleListProperty<>(FXCollections.observableArrayList(result));
    }

    private Map<Library, ObjectProperty<LibraryInfo>> libraryInfoMap = new HashMap<>();

    public ObjectProperty<LibraryInfo> libraryInfoProperty(Library library) {
        return libraryInfoMap.computeIfAbsent(library, key -> {
            ObjectProperty<LibraryInfo> infoProperty = new SimpleObjectProperty<>();

            executor.submit(() -> {
                try {
                    String libraryId = library.getId();
                    File file = loadFile(libraryId + ".json", "https://raw.githubusercontent.com/dlemmermann/jfxcentral-data/main/libraries/" + libraryId + "/_info.json?time=" + ZonedDateTime.now().toInstant());
                    LibraryInfo result = gson.fromJson(new FileReader(file), LibraryInfo.class);
                    Platform.runLater(() -> infoProperty.set(result));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });

            return infoProperty;
        });
    }

    public ListProperty<Book> getBooksByPerson(Person person) {
        ObservableList<Book> list = FXCollections.observableArrayList();
        list.setAll(getBooks().stream().filter(book -> book.getPersonIds().contains(person.getId())).collect(Collectors.toList()));
        return new SimpleListProperty<>(list);
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

    private final ListProperty<Video> videos = new SimpleListProperty<>(this, "videos", FXCollections.observableArrayList());

    public ObservableList<Video> getVideos() {
        return videos.get();
    }

    public ListProperty<Video> videosProperty() {
        return videos;
    }

    public void setVideos(List<Video> videos) {
        this.videos.setAll(videos);
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
        // adding caching buster via timestamp
        System.out.println("url: " + url + "?time=" + ZonedDateTime.now().toInstant());
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
