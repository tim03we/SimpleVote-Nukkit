package tim03we.simplevote;

import cn.nukkit.utils.Config;

import java.util.HashMap;
import java.util.Map;

public class Language {

    public static HashMap<String, String> messages = new HashMap<>();
    public static String prefix;

    public static void init() {
        messages.clear();
        SimpleVote.instance.saveResource("messages.yml");
        Config m = new Config(SimpleVote.instance.getDataFolder() + "/messages.yml");
        for (Map.Entry<String, Object> map : m.getAll().entrySet()) {
            String key = map.getKey();
            if (map.getValue() instanceof String) {
                String val = (String) map.getValue();
                messages.put(key, val);
            }
        }
        prefix = m.getString("prefix");
    }

    public static String translate(boolean prefix, String key, Object... replacements) {
        String message;
        if(prefix) {
            message = get(key);
        } else {
            message = getNoPrefix(key);
        }
        if(replacements == null) {
            return message;
        }
        int i = 1;
        for (Object replacement : replacements) {
            message = message.replace("%" + i, String.valueOf(replacement));
            i++;
        }
        return message;
    }

    public static String get(String key) {
        return prefix.replace("&", "§") + messages.getOrDefault(key, "null").replace("&", "§");
    }

    public static String getNoPrefix(String key) {
        return messages.getOrDefault(key, "null").replace("&", "§");
    }
}
