package com.dlsc.jfxcentral.json;

import com.dlsc.jfxcentral.model.Book;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class WriteBook {

    public static void main(String[] args) {
        Book book = new Book();
        book.setTitle("JavaFX in Action");
        book.setPublishedDate(LocalDate.now());
        book.setAuthors("Dirk Lemmermann, Steve Smith");
        book.setIsbn("34234-34-32434");
        book.setUrl("https://fxexperience.com");
        book.setId("in-action");
        book.setDescription("this book is about ..... bla bla bla");
        book.setImage("cover.jpg");
        book.setAmazon("https://www.amazon.com/-/de/dp/B00L3TF02K/ref=sr_1_1?__mk_de_DE=ÅMÅŽÕÑ&dchild=1&keywords=Mastering+JavaFX+8+Controls&qid=1619703516&sr=8-1");
        book.getPersonIds().add("person1");
        book.getPersonIds().add("person2");
        List<Book> list = new ArrayList<>();
        list.add(book);

//        Gson gson = Converters.registerLocalDate(new GsonBuilder()).setPrettyPrinting().create();
//        String json = gson.toJson(list);
//        System.out.println(json);
//
//        gson.fromJson(json, Book.class);
    }
}
