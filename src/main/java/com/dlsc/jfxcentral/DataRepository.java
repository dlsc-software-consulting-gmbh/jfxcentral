package com.dlsc.jfxcentral;

import com.dlsc.jfxcentral.model.*;
import com.dlsc.jfxcentral.util.QueryResult;
import com.fatboyindustrial.gsonjavatime.Converters;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class DataRepository {

    public enum Source {

        LIVE("live"),
        STAGING("staging");

        private String branchName;

        Source(String branchName) {
            this.branchName = branchName;
        }

        public String getBranchName() {
            return branchName;
        }
    }

    private static final String BASE_URL = "https://raw.githubusercontent.com/dlemmermann/jfxcentral-data/";

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
        recentItems.addListener((Observable it) -> System.out.println("recent items count: " + getRecentItems().size()));
        loadData();
        sourceProperty().addListener(it -> refresh());
    }

    public void refresh() {
        setHomeText("");

        ImageManager.getInstance().clear();

        libraryInfoMap.clear();
        libraryReadMeMap.clear();
        newsTextMap.clear();
        libraryReadMeMap.clear();

        getPosts().clear();
        getPeople().clear();
        getLibraries().clear();
        getBooks().clear();
        getNews().clear();
        getVideos().clear();
        getBlogs().clear();
        getCompanies().clear();

        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Platform.runLater(() -> loadData());
        });

        thread.setDaemon(true);
        thread.setName("Refresh Data Thread");
        thread.start();
    }

    private void loadData() {
        try {
            setHomeText(loadString(getBaseUrl() + "intro.md?time=" + ZonedDateTime.now().toInstant()));

            // load people
            File peopleFile = loadFile("people.json", getBaseUrl() + "people.json");
            setPeople(gson.fromJson(new FileReader(peopleFile), new TypeToken<List<Person>>() {
            }.getType()));

            // load books
            File booksFile = loadFile("books.json", getBaseUrl() + "books.json");
            setBooks(gson.fromJson(new FileReader(booksFile), new TypeToken<List<Book>>() {
            }.getType()));

            // load videos
            File videosFile = loadFile("videos.json", getBaseUrl() + "videos.json");
            setVideos(gson.fromJson(new FileReader(videosFile), new TypeToken<List<Video>>() {
            }.getType()));

            // load libraries
            File librariesFile = loadFile("libraries.json", getBaseUrl() + "libraries.json");
            setLibraries(gson.fromJson(new FileReader(librariesFile), new TypeToken<List<Library>>() {
            }.getType()));

            // load libraries
            File newsFile = loadFile("news.json", getBaseUrl() + "news.json");
            setNews(gson.fromJson(new FileReader(newsFile), new TypeToken<List<News>>() {
            }.getType()));

            // load libraries
            File blogsFile = loadFile("blogs.json", getBaseUrl() + "blogs.json");
            setBlogs(gson.fromJson(new FileReader(blogsFile), new TypeToken<List<Blog>>() {
            }.getType()));

            // load libraries
            File companiesFile = loadFile("companies.json", getBaseUrl() + "companies.json");
            setCompanies(gson.fromJson(new FileReader(companiesFile), new TypeToken<List<Company>>() {
            }.getType()));

            updateRecentItems();
            readFeeds();

        } catch (IOException | FeedException e) {
            e.printStackTrace();
        }
    }

    private void updateRecentItems() {
        getRecentItems().clear();
        getRecentItems().addAll(findRecentItems(getNews()));
        getRecentItems().addAll(findRecentItems(getPeople()));
        getRecentItems().addAll(findRecentItems(getBooks()));
        getRecentItems().addAll(findRecentItems(getLibraries()));
        getRecentItems().addAll(findRecentItems(getVideos()));
        getRecentItems().addAll(findRecentItems(getBlogs()));
    }

    private List<ModelObject> findRecentItems(List<? extends ModelObject> items) {
        items.sort((Comparator<ModelObject>) (m1, m2) -> {
            LocalDate date1 = m1.getModifiedOn();
            LocalDate date2 = m2.getModifiedOn();

            if (date1 != null && date2 != null) {
                return date1.compareTo(date2);
            } else if (date1 != null) {
                return -1;
            }

            return +1;
        });

        List<ModelObject> result = new ArrayList<>();

        for (int i = 0; i < Math.min(3, items.size()); i++) {
            result.add(items.get(i));
        }

        return result;
    }

    private final ListProperty<ModelObject> recentItems = new SimpleListProperty<>(this, "recentItems", FXCollections.observableArrayList());

    public ObservableList<ModelObject> getRecentItems() {
        return recentItems.get();
    }

    public ListProperty<ModelObject> recentItemsProperty() {
        return recentItems;
    }

    public void setRecentItems(ObservableList<ModelObject> recentItems) {
        this.recentItems.set(recentItems);
    }

    public Optional<Person> getPersonById(String id) {
        return people.stream().filter(person -> person.getId().equals(id)).findFirst();
    }

    public Optional<Company> getCompanyById(String id) {
        return companies.stream().filter(company -> company.getId().equals(id)).findFirst();
    }

    public Optional<Library> getLibraryById(String id) {
        return libraries.stream().filter(library -> library.getId().equals(id)).findFirst();
    }

    public ListProperty<Video> getVideosByPerson(Person person) {
        ListProperty<Video> listProperty = new SimpleListProperty<>(FXCollections.observableArrayList());
        listProperty.setAll(videos.stream().filter(video -> video.getPersonIds().contains(person.getId())).collect(Collectors.toList()));
        return listProperty;
    }

    public ListProperty<Blog> getBlogsByPerson(Person person) {
        ListProperty<Blog> listProperty = new SimpleListProperty<>(FXCollections.observableArrayList());
        listProperty.setAll(blogs.stream().filter(blog -> blog.getPersonIds().contains(person.getId())).collect(Collectors.toList()));
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
                    File file = loadFile(libraryId + ".json", getBaseUrl() + "libraries/" + libraryId + "/_info.json?time=" + ZonedDateTime.now().toInstant());
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

    public String getBaseUrl() {
        return BASE_URL + getSource().getBranchName() + "/";
    }

    public String getNewsBaseUrl(News news) {
        return getBaseUrl() + "news/" + DATE_FORMATTER.format(news.getCreatedOn()) + "-" + news.getId();
    }

    public StringProperty libraryReadMeProperty(Library library) {
        return libraryReadMeMap.computeIfAbsent(library, key -> {
            StringProperty readmeProperty = new SimpleStringProperty();

            String readmeFileURL = library.getReadmeFileURL();
            if (StringUtils.isNotBlank(readmeFileURL)) {
                executor.submit(() -> {
                    String readmeText = loadString(getBaseUrl() + "libraries/" + library.getId() + "/_readme.md");
                    Platform.runLater(() -> readmeProperty.set(readmeText));
                });
            }

            return readmeProperty;
        });
    }

    private final StringProperty homeText = new SimpleStringProperty(this, "homeText");

    public String getHomeText() {
        return homeText.get();
    }

    public StringProperty homeTextProperty() {
        return homeText;
    }

    public void setHomeText(String homeText) {
        this.homeText.set(homeText);
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

    private final ListProperty<Blog> blogs = new SimpleListProperty<>(this, "blogs", FXCollections.observableArrayList());

    public ObservableList<Blog> getBlogs() {
        return blogs.get();
    }

    public ListProperty<Blog> blogsProperty() {
        return blogs;
    }

    public void setBlogs(List<Blog> blogs) {
        this.blogs.setAll(blogs);
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

    private final ListProperty<Company> companies = new SimpleListProperty<>(this, "companies", FXCollections.observableArrayList());

    public ObservableList<Company> getCompanies() {
        return companies.get();
    }

    public ListProperty<Company> companiesProperty() {
        return companies;
    }

    public void setCompanies(List<Company> companies) {
        this.companies.setAll(companies);
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
        System.out.println("loading string from: " + address);

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

    private final ObjectProperty<Source> source = new SimpleObjectProperty<>(this, "source", Source.LIVE);

    public Source getSource() {
        return source.get();
    }

    public ObjectProperty<Source> sourceProperty() {
        return source;
    }

    public void setSource(Source source) {
        this.source.set(source);
    }

    public StringProperty getArtefactVersion(Library library) {

        String groupId = library.getGroupId();
        String artifactId = library.getArtifactId();

        StringProperty result = new SimpleStringProperty("");

        if (StringUtils.isNotBlank(groupId) && StringUtils.isNotBlank(artifactId)) {
            executor.execute(() -> {
                HttpURLConnection con = null;

                try {
                    URL url = new URL(MessageFormat.format("http://search.maven.org/solrsearch/select?q=g:{0}+AND+a:{1}", groupId, artifactId));
                    con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("GET");
                    int status = con.getResponseCode();
                    if (status == 200) {
                        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                        String inputLine;
                        StringBuffer content = new StringBuffer();
                        while ((inputLine = in.readLine()) != null) {
                            content.append(inputLine);
                        }
                        in.close();

                        QueryResult queryResult = gson.fromJson(content.toString(), QueryResult.class);
                        System.out.println("latest version: " + queryResult.getResponse().getDocs().get(0).getLatestVersion());
                        Platform.runLater(() -> result.set(queryResult.getResponse().getDocs().get(0).getLatestVersion()));
                    } else {
                        result.set("unknown");
                    }
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (con != null) {
                        con.disconnect();
                    }
                }
            });
        }

        return result;
    }

    private final ListProperty<Post> posts = new SimpleListProperty<>(this, "posts", FXCollections.observableArrayList());

    public ObservableList<Post> getPosts() {
        return posts.get();
    }

    public ListProperty<Post> postsProperty() {
        return posts;
    }

    public void setPosts(ObservableList<Post> posts) {
        this.posts.set(posts);
    }

    public void readFeeds() throws IOException, FeedException {
        for (Blog blog : getBlogs()) {
            String url = blog.getFeed();
            if (StringUtils.isNotBlank(url)) {

                System.out.println("url: " + url);
                SyndFeed feed = new SyndFeedInput().build(new XmlReader(new URL(url)));
                System.out.println(feed.getTitle());

                List<SyndEntry> entries = feed.getEntries();
                entries.forEach(entry -> {
                    getPosts().add(new Post(blog, feed, entry));
                    System.out.println(feed.getTitle() + ":" + entry.getTitle());
                });
            }
        }
    }

    public static void main(String[] args) {
        new DataRepository();
    }
}
