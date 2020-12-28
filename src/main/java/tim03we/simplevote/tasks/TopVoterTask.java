package tim03we.simplevote.tasks;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.level.Location;
import cn.nukkit.level.particle.FloatingTextParticle;
import cn.nukkit.scheduler.Task;
import tim03we.simplevote.Language;
import tim03we.simplevote.SimpleVote;

public class TopVoterTask extends Task {

    private Location location;
    private int count;
    private FloatingTextParticle particle;

    public TopVoterTask(Location location, int count) {
        this.location = location;
        this.count = count;
        this.particle = new FloatingTextParticle(location, " ", "");
    }

    @Override
    public void onRun(int i) {
        StringBuilder stringBuilder = new StringBuilder(Language.translate(false, "floating.text.title") + "\n§r\n");
        for (String list : SimpleVote.instance.getTopList(this.count)) {
            String[] ex = list.split(":");
            stringBuilder.append(Language.translate(false, "floating.text", ex[0], ex[1]) + "\n");
        }
        stringBuilder.append("§r\n" + Language.translate(false, "floating.text.title"));
        for (Player player : Server.getInstance().getOnlinePlayers().values()) {
            if(!player.getLevel().getName().equals(location.getLevel().getName())) {
                particle.setInvisible(true);
            } else {
                particle.setInvisible(false);
            }
            particle.setTitle(stringBuilder.toString());
            location.getLevel().addParticle(particle, player);
        }
    }
}
