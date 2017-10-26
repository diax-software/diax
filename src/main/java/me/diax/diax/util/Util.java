package me.diax.diax.util;

public class Util {

    public static boolean isDeveloper(Data data, String id) {
        return data.getDevelopers().contains(id);
    }
}