package com.dlsc.jfxcentral;

import com.dlsc.jfxcentral.model.*;
import com.fatboyindustrial.gsonjavatime.Converters;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class DataRepository {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private ExecutorService executor = Executors.newCachedThreadPool();

    private static final DataRepository instance = new DataRepository();

    private final Gson gson = Converters.registerLocalDate(new GsonBuilder()).setPrettyPrinting().create();

    private Map<Library, ObjectProperty<LibraryInfo>> libraryInfoMap = new HashMap<>();

    private Map<News, StringProperty> newsTextMap = new HashMap<>();

    private Map<Library, StringProperty> libraryReadMeMap = new HashMap<>();

    public static synchronized DataRepository getInstance() {
        return instance;
    }

    private DataRepository() {
        loadData();
    }

    public void refresh() {
        libraryInfoMap.clear();
        newsTextMap.clear();
        libraryReadMeMap.clear();
        getPeople().clear();
        getLibraries().clear();
        getBooks().clear();
        getNews().clear();
        getVideos().clear();

        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Platform.runLater(() -> {
                loadData();
            });
        });

        thread.setDaemon(true);
        thread.setName("Refresh Data Thread");
        thread.start();
    }

    private void loadData() {
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

            // load libraries
            File newsFile = loadFile("news.json", "https://raw.githubusercontent.com/dlemmermann/jfxcentral-data/main/news.json");
            setNews(gson.fromJson(new FileReader(newsFile), new TypeToken<List<News>>() {
            }.getType()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Optional<Person> getPersonById(String id) {
        return people.stream().filter(person -> person.getId().equals(id)).findFirst();
    }

    public Optional<Library> getLibraryById(String id) {
        return libraries.stream().filter(library -> library.getId().equals(id)).findFirst();
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

    public StringProperty newsTextProperty(News news) {
        return newsTextMap.computeIfAbsent(news, key -> {
            StringProperty textProperty = new SimpleStringProperty();

            executor.submit(() -> {
                String url = getNewsBaseUrl(news) + "/text.md?time=" + ZonedDateTime.now().toInstant();
                System.out.println("loading news from: " + url);
                String text = loadString(url);
                Platform.runLater(() -> textProperty.set(text));
            });

            return textProperty;
        });
    }

    public String getNewsBaseUrl(News news) {
        return "https://raw.githubusercontent.com/dlemmermann/jfxcentral-data/main/news/" + DATE_FORMATTER.format(news.getDate()) + "-" + news.getId();
    }

    public StringProperty libraryReadMeProperty(Library library) {
        return libraryReadMeMap.computeIfAbsent(library, key -> {
            StringProperty readmeProperty = new SimpleStringProperty();

            String readmeFileURL = library.getReadmeFileURL();
            if (StringUtils.isNotBlank(readmeFileURL)) {
                executor.submit(() -> {
                    String readmeText = loadString(library.getReadmeFileURL());
                    Platform.runLater(() -> readmeProperty.set(readmeText));
                });
            }

            return readmeProperty;
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

    private final ListProperty<News> news = new SimpleListProperty<>(this, "news", FXCollections.observableArrayList());

    public ObservableList<News> getNews() {
        return news.get();
    }

    public ListProperty<News> newsProperty() {
        return news;
    }

    public void setNews(List<News> news) {
        this.news.setAll(news);
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
        url = url + "?time=" + ZonedDateTime.now().toInstant();
        System.out.println("url: " + url);
        ReadableByteChannel readChannel = Channels.newChannel(new URL(url).openStream());
        File file = File.createTempFile(fileName, "json");
        FileOutputStream fileOS = new FileOutputStream(file);
        FileChannel writeChannel = fileOS.getChannel();
        writeChannel.transferFrom(readChannel, 0, Long.MAX_VALUE);
        return file;
    }

    private String loadString(String address) {
        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(address);

            // read text returned by server
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

            String line;
            while ((line = in.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
            in.close();

        } catch (MalformedURLException e) {
            System.out.println("Malformed URL: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("I/O Error: " + e.getMessage());
        }

        return sb.toString();
    }

    public static void main(String[] args) {
        new DataRepository();
    }
}
