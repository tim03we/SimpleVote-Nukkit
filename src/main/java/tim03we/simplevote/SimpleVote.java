package tim03we.simplevote;

import cn.nukkit.command.ConsoleCommandSender;
import cn.nukkit.level.Location;
import cn.nukkit.plugin.PluginBase;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import tim03we.simplevote.commands.VoteCommand;
import tim03we.simplevote.tasks.CooldownTask;
import tim03we.simplevote.tasks.TopVoterTask;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class SimpleVote extends PluginBase {

    public static SimpleVote instance;

    public final static String USER_AGENT = "Mozilla/5.0";
    public HashMap<String, Integer> cooldown = new HashMap<>();

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        Language.init();

        if(!getConfig().getString("secret-key").equals("YOUR_SECRET_KEY")) {
            getServer().loadLevel(getConfig().getString("text.level"));
            getServer().getScheduler().scheduleRepeatingTask(new TopVoterTask(new Location(getConfig().getDouble("text.x"), getConfig().getDouble("text.y"), getConfig().getDouble("text.z"), getServer().getLevelByName(getConfig().getString("text.level"))), getConfig().getInt("text.count")), 100, true);

            getServer().getPluginManager().registerEvents(new EventListener(this), this);
            getServer().getCommandMap().register("vote", new VoteCommand(this));
            if(getConfig().getBoolean("cooldown.enable")) {
                getServer().getScheduler().scheduleRepeatingTask(new CooldownTask(this), 20);
            }
        } else {
            getLogger().error("Please enter your secret key from the Vote website in the config.");
        }
    }

    public void sendVoteBroadcast(String name)
    {
        getServer().broadcastMessage(Language.translate(true, "public.vote", name));
    }

    public void sendCommands(String name)
    {
        for (String commands : getConfig().getStringList("commands")) {
            getServer().dispatchCommand(new ConsoleCommandSender(), commands.replace("{player}", name));
        }
    }

    public HashMap<String, Integer> getVoteMap() {
        HashMap<String, Integer> voteMap = new HashMap<>();
        try {
            String url = "https://minecraftpocket-servers.com/api/?object=servers&element=voters&key=" + getConfig().getString("secret-key") + "&month=current&format=json";
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", USER_AGENT);
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            String jsonString = response.toString();
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(jsonString);
            JSONArray voteArray = (JSONArray) jsonObject.get("voters");
            for (Object voter : voteArray) {
                JSONObject playerObject = (JSONObject) parser.parse(voter.toString());
                voteMap.put(playerObject.get("nickname").toString(), Integer.parseInt(playerObject.get("votes").toString()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return voteMap;
    }

    public String checkVoteStatus(String playername) {
        playername = playername.replace(" ", "");
        String number = null;
        try {
            String url = "https://minecraftpocket-servers.com/api/?object=votes&element=claim&key=" + getConfig().getString("secret-key") + "&username=" + playername;
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", USER_AGENT);
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            if(response.toString().equals("0")) {
                number = "0";
            } else if(response.toString().equals("1")) {
                number = "1";
            } else number = "2";
            System.out.println("LOL: " + number);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return number;
    }

    public String setVote(String playername) {
        playername = playername.replace(" ", "");
        String number = null;
        try {
            URL url = new URL ("https://minecraftpocket-servers.com/api/?action=post&object=votes&element=claim&key=" + getConfig().getString("secret-key") + "&username=" + playername);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", USER_AGENT);
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);
            try(BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                if(response.toString().equals("0")) {
                    number = "0";
                } else number = "1";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return number;
    }

    private HashMap<String, Integer> players;

    public List<String> getTopList(int count) {
        this.players = getVoteMap();
        List<String> topList = new ArrayList<>();
        for (int i = 1; i <= count; ++i) {
            String[] top = getHighestVote().split(":");
            boolean inList = false;
            for (String s : topList) {
                String[] ex = s.split(":");
                if(ex[0].equals(top[0])) inList = true;
            }
            if(inList) {
                topList.add("?:?");
            } else topList.add(top[0] + ":" + top[1]);
        }
        return topList;
    }

    public String getHighestVote() {
        String name = null;
        int votes = 0;
        for (String player : this.players.keySet()) {
            if(this.players.get(player) > votes) {
                name = player;
                votes = Integer.parseInt(this.players.get(player).toString());
            }
        }
        this.players.remove(name);
        return name + ":" + votes;
    }
}
