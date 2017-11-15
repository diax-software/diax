package me.diax.diax.data.db;

import lombok.Data;
import me.diax.diax.data.db.extra.UserSettings;

@Data
public class UserData {
    public String id;
    public UserSettings settings = new UserSettings();
}
