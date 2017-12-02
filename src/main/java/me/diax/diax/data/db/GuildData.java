package me.diax.diax.data.db;

import lombok.Data;
import me.diax.diax.data.db.extra.GuildProfile;
import me.diax.diax.data.db.extra.GuildSettings;

@Data
public class GuildData {
    public String id;
    public GuildSettings settings = new GuildSettings();
    public GuildProfile profile = new GuildProfile();
}
