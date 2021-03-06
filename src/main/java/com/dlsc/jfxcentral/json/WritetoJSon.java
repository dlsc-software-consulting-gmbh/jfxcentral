package com.dlsc.jfxcentral.json;

import com.dlsc.jfxcentral.model.Person;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

public class WritetoJSon {

    public static void main(String[] args) {
        Person dirk_lemmermann = new Person("Dirk Lemmermann");

        dirk_lemmermann.setChampion(true);
        dirk_lemmermann.setRockstar(true);
        dirk_lemmermann.setPhoto("dirk.jpg");

        List<Person> people = new ArrayList<>();
        people.add(dirk_lemmermann);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        System.out.println(gson.toJson(people));
    }
}
