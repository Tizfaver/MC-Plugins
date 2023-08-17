package com.tizfaver.lucky.utils;

import com.tizfaver.lucky.Main;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class GetDeathMessage {

    private static final List<String> tempNormal = new ArrayList<>(Main.getInstance().getConfig().getStringList("death-messages.normal-messages"));
    private List<String> normals = new ArrayList<>();
    private List<String> accidents = new ArrayList<>();
    private List<String> normalsFinal = new ArrayList<>();
    private List<String> accidentsFinal = new ArrayList<>();
    private static final List<String> tempFinal = new ArrayList<>(Main.getInstance().getConfig().getStringList("death-messages.final-kills"));

    public GetDeathMessage(){
        for(String msg : tempNormal){
            if(msg.contains("[killer]"))
                normals.add(msg);
            else
                accidents.add(msg);
        }

        for(String msg : tempFinal){
            if(msg.contains("[killer]"))
                normalsFinal.add(msg);
            else
                accidentsFinal.add(msg);
        }
    }

    public String getRandomAccident(String type, Player victim){
        Main plugin = Main.getInstance();
        String msg;
        if(type.equals("normal"))
            msg = this.accidents.get(Main.getRandom().nextInt(this.accidents.size()));
        else //it's final
            msg = this.accidentsFinal.get(Main.getRandom().nextInt(this.accidentsFinal.size()));

        msg = Utils.replacePlaceholders(victim, msg);
        msg = msg.replace("[victim]", Main.getInstance().getGame().getTeamByPlayer(victim).getColor() + "" + victim.getName() + "" + ChatColor.RESET);

        return msg;
    }

    public String getRandomNormal(String type, Player victim, Player killer){
        Main plugin = Main.getInstance();
        String msg;
        if(type.equals("normal"))
            msg = this.normals.get(Main.getRandom().nextInt(this.normals.size()));
        else //it's final
            msg = this.normalsFinal.get(Main.getRandom().nextInt(this.normalsFinal.size()));

        msg = Utils.replacePlaceholders(killer, msg);
        msg = msg.replace("[victim]", Main.getInstance().getGame().getTeamByPlayer(victim).getColor() + "" + victim.getName() + "" + ChatColor.RESET);
        msg = msg.replace("[killer]", Main.getInstance().getGame().getTeamByPlayer(killer).getColor() + "" + killer.getName() + "" + ChatColor.RESET);

        return msg;
    }

}
