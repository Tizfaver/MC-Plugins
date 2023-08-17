package com.tizfaver.lucky.specialeffects;

import com.tizfaver.lucky.Main;
import com.tizfaver.lucky.utils.SpecialEffect;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomCommand implements SpecialEffect {

    private List<String> commands = new ArrayList<>();

    public CustomCommand(List<String> list){
        for(String s : list){
            int probability = extractProbability(s);

            for(int i = 0; i < probability; i++){
                commands.add(s);
            }
        }
    }

    @Override
    public void runEffect(Player player, Block block, BlockBreakEvent event) {
        String execute = commands.get(Main.getRandom().nextInt(this.commands.size()));
        String userChoice = extractUserChoice(execute);

        if(userChoice.equals("console")){
            ConsoleCommandSender sender = Bukkit.getConsoleSender();
            Bukkit.getServer().dispatchCommand(sender, extractUserCommand(execute));
        } else {
            Bukkit.dispatchCommand(player, extractUserCommand(execute));
        }
    }

    private static int extractProbability(String input) {
        Pattern pattern = Pattern.compile("\\[prob (\\d+)\\]");
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            String probString = matcher.group(1);
            return Integer.parseInt(probString);
        } else {
            return -1;
        }
    }

    private static String extractUserChoice(String input) {
        Pattern pattern = Pattern.compile("\\[(console|player)\\]");
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return ""; //returning an empty string to indicate no user choice found.
        }
    }

    private static String extractUserCommand(String input) {
        String[] parts = input.split(":", 2);
        if (parts.length > 1) {
            return parts[1].trim();
        } else {
            return ""; //returning an empty string to indicate no match found.
        }
    }
}
