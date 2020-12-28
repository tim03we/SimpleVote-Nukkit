package tim03we.simplevote;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerLoginEvent;
import cn.nukkit.event.player.PlayerQuitEvent;

public class EventListener implements Listener {

    SimpleVote plugin;

    public EventListener(SimpleVote plugin)
    {
        this.plugin = plugin;
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        plugin.cooldown.put(player.getName(), 0);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        plugin.cooldown.put(player.getName(), 0);
    }
}
