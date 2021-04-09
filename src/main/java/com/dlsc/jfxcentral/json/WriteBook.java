package com.dlsc.jfxcentral.json;

import com.dlsc.jfxcentral.model.Book;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

public class WriteBook {

    public static void main(String[] args) {
        Book book = new Book();
        book.setTitle("JavaFX in Action");
        book.setAuthors("Dirk Lemmermann, Steve Smith");
        book.setIsbn("34234-34-32434");
        book.setUrl("https://fxexperience.com");
        book.setId("in-action");
        book.setDescription("this book is about ..... bla bla bla");
        book.setImage("cover.jpg");

        List<Book> list = new ArrayList<>();
        list.add(book);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        System.out.println(gson.toJson(list));
    }
}
