package me.diax.diax.data.config.entities;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;

@Data
public class Config {
    public String prefix, type;

    public Tokens tokens = new Tokens();
    public Channels channels = new Channels();

    public List<String> developers = new LinkedList<>();
    public List<String> donors = new LinkedList<>();
    public List<String> blacklist = new LinkedList<>();

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