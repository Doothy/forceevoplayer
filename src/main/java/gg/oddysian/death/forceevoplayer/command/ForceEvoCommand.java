package gg.oddysian.death.forceevoplayer.command;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.evolution.Evolution;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.evolution.conditions.EvoCondition;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import gg.oddysian.death.forceevoplayer.utils.PermissionUtils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;

public class ForceEvoCommand extends CommandBase {


    @Override
    public String getName() {
        return "forceevo";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/forceevo [slot]";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if(sender instanceof EntityPlayerMP){
            EntityPlayerMP player = (EntityPlayerMP) sender;
            if(PermissionUtils.canUse("forceevoplayer.command.forceevo", player)){
                if (args.length != 1){
                    player.sendMessage(new TextComponentString(getUsage(player)));
                    return;
                }
                else {
                    int slot;
                    switch (args[0]) {
                        case "1":
                            slot = 0;
                            break;
                        case "2":
                            slot = 1;
                            break;
                        case "3":
                            slot = 2;
                            break;
                        case "4":
                            slot = 3;
                            break;
                        case "5":
                            slot = 4;
                            break;
                        case "6":
                            slot = 5;
                            break;
                        default:
                            player.sendMessage(new TextComponentString("\u00A7c" + "Please provide a valid slot number"));
                            return;
                    }
                    UUID uuid = player.getUniqueID();
                    Pokemon[] party = Pixelmon.storageManager.getParty(uuid).getAll();
                    if (party[slot] == null) {
                        player.sendMessage(new TextComponentString("\u00A7c" + "The selected slot is empty!"));
                        return;
                    }
                    Pokemon pkmn = party[slot];
                    ArrayList<Evolution> evo = pkmn.getBaseStats().evolutions;
                    if (evo.size() < 1) {
                        player.sendMessage(new TextComponentString("\u00A7c" + "This pokemon has no evolution!"));
                        return;
                    }
                    if (pkmn.isEgg()) {
                        player.sendMessage(new TextComponentString("\u00A7c" + "You cannot evolve an egg, duh"));
                        return;
                    }
                    if (EnumSpecies.hasPokemonAnyCase(evo.get(0).to.name)) {
                        EntityPixelmon entityPixelmon = pkmn.getOrSpawnPixelmon(player);
                        boolean evolved = true;
                        for (Iterator iterator = evo.iterator(); iterator.hasNext(); evolved = false) {
                            Evolution evolution = (Evolution) iterator.next();
                            if (evoCondCheck(entityPixelmon, evolution.conditions)) {
                                evolution.doEvolution(entityPixelmon);
                                evolved = true;
                                break;
                            }
                        }
                        if (!evolved) {
                            pkmn.retrieve();
                            player.sendMessage(new TextComponentString("\u00A7c" + "This pokemon does not fulfill all the conditions!"));
                        }
                    }
                }
            }
        } else return;
    }

    public boolean evoCondCheck(EntityPixelmon entityPixelmon, ArrayList<EvoCondition> evoConditions) {
        Iterator iterator = evoConditions.iterator();
        for (EvoCondition condition; iterator.hasNext(); iterator.next()) {
            condition = (EvoCondition) iterator.next();
            if(!condition.passes(entityPixelmon))
                return false;
        }
        return true;
    }

}
