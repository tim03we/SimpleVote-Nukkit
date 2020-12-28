package tim03we.simplevote.commands;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import tim03we.simplevote.Language;
import tim03we.simplevote.SimpleVote;

public class VoteCommand extends Command {

    SimpleVote plugin;

    public VoteCommand(SimpleVote plugin)
    {
        super("vote", "Vote Command", "/vote");
        this.plugin = plugin;
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] strings) {
        if(sender instanceof Player) {
            if(plugin.cooldown.get(sender.getName()) > 0) {
                sender.sendMessage(Language.translate(true, "cooldown.message", plugin.cooldown.get(sender.getName()).toString()));
                return true;
            }
            try {
                if(plugin.getConfig().getBoolean("cooldown.enable")) {
                    if(plugin.cooldown.get(sender.getName()) > 0) {
                        sender.sendMessage(Language.translate(true, "cooldown.message", plugin.cooldown.get(sender.getName()).toString()));
                        return true;
                    } else {
                        plugin.cooldown.put(sender.getName(), plugin.getConfig().getInt("cooldown.time"));
                    }
                }
                sender.sendMessage(Language.translate(true, "command.checking"));
                if(plugin.checkVoteStatus(sender.getName()).equals("0")) {
                    sender.sendMessage(Language.translate(true, "command.not.voted"));
                } else if(plugin.checkVoteStatus(sender.getName()).equals("1")) {
                    if(plugin.setVote(sender.getName()).equals("1")) {
                        plugin.sendVoteBroadcast(sender.getName());
                        sender.sendMessage(Language.translate(true, "command.success"));
                        plugin.sendCommands(sender.getName());
                    }
                } else if(plugin.checkVoteStatus(sender.getName()).equals("2")) {
                    sender.sendMessage(Language.translate(true, "command.already.voted"));
                }
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        } else {
            sender.sendMessage("Run this command InGame!");
        }
        return false;
    }
}
