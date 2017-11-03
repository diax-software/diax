package me.diax.diax.util;

import org.json.JSONObject;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Data {

    private JSONObject data;
    private File file;

    private String token;
    private String botlistToken;
    private String prefix;
    private Map<String, String> channels;
    private List<String> developers;
    private List<String> donors;
    private List<String> blacklist;
    private String weebToken;

    public Data(File file) throws Exception {
        this.file = file;
        this.reloadData();
    }

    static String readFile(String path, Charset encoding)
            throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

    @SuppressWarnings("unchecked")
    public void reloadData() throws Exception {
        data = new JSONObject(readFile(file.getPath(), StandardCharsets.UTF_8));
        token = data.getString("token");
        botlistToken = data.getString("botlist-token");
        prefix = data.getString("prefix");
        channels = ((HashMap) data.getJSONObject("channels").toMap());
        developers = (ArrayList) data.getJSONArray("developers").toList();
        donors = ((ArrayList) data.getJSONArray("donors").toList());
        blacklist = ((ArrayList) data.getJSONArray("blacklist").toList());
        weebToken = data.getString("weeb-token");
    }

    public void saveData() {
        JSONObject data = new JSONObject();
        data
                .put("token", token)
                .put("botlist-token", botlistToken)
                .put("prefix", prefix)
                .put("channels", channels)
                .put("developers", developers)
                .put("donors", donors)
                .put("blacklist", blacklist)
                .put("weeb-token", weebToken);

        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(data.toString(2));
            fileWriter.flush();
            fileWriter.close();

        } catch (IOException e) {
            LoggerFactory.getLogger(Data.class).error("Error saving data!");
            e.printStackTrace();
        } catch (Exception e) {
            LoggerFactory.getLogger(Data.class).error("An unknown exception occurred whilst saving data!");
            e.printStackTrace();
        }
    }

    public String getToken() {
        return token;
    }

    public String getBotlistToken() {
        return botlistToken;
    }

    public String getPrefix() {
        return prefix;
    }

    public Map<String, String> getChannels() {
        return channels;
    }

    public List<String> getDevelopers() {
        return developers;
    }

    public void addDeveloper(String id) {
        developers.add(id);
    }

    public void removeDeveloper(String id) {
        developers.remove(id);
    }

    public List<String> getDonors() {
        return donors;
    }

    public void addDonor(String id) {
        donors.add(id);
    }

    public void removeDonor(String id) {
        donors.remove(id);
    }

    public List<String> getBlacklist() {
        return blacklist;
    }

    public void blacklist(String id) {
        blacklist.add(id);
    }

    public void unBlacklist(String id) {
        blacklist.remove(id);
    }

    public String getWeebToken() {
        return weebToken;
    }
}