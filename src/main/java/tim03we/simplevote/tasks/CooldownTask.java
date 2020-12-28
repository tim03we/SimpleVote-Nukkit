package tim03we.simplevote.tasks;

import cn.nukkit.Player;
import cn.nukkit.scheduler.Task;
import tim03we.simplevote.SimpleVote;

public class CooldownTask extends Task {

    SimpleVote plugin;

    public CooldownTask(SimpleVote plugin)
    {
        this.plugin = plugin;
    }

    @Override
    public void onRun(int i) {
        for (Player player : plugin.getServer().getOnlinePlayers().values()) {
            if(plugin.cooldown.get(player.getName()) == null) {
                plugin.cooldown.put(player.getName(), 0);
            }
            if(plugin.cooldown.get(player.getName()) > 0) {
                plugin.cooldown.put(player.getName(), plugin.cooldown.get(player.getName()) - 1);
            }
        }
    }
}
