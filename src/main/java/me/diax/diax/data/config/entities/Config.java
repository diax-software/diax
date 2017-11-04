package me.diax.diax.data.config.entities;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;

@Data
public class Config {
    private String prefix;

    private Tokens tokens = new Tokens();
    private Channels channels = new Channels();

    private List<String> developers = new LinkedList<>();
    private List<String> donors = new LinkedList<>();
    private List<String> blacklist = new LinkedList<>();

    public void addDeveloper(String id) {
        developers.add(id);
    }

    public void removeDeveloper(String id) {
        developers.remove(id);
    }

    public void addDonor(String id) {
        donors.add(id);
    }

    public void removeDonor(String id) {
        donors.remove(id);
    }

    public void blacklist(String id) {
        blacklist.add(id);
    }

    public void unBlacklist(String id) {
        blacklist.remove(id);
    }
}