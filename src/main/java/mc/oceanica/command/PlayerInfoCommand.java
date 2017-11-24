package mc.oceanica.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

public class PlayerInfoCommand extends CommandBase{

    @Override
    public String getName() {
        return "playerinfo";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "playerinfo";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        Entity commandSenderEntity = sender.getCommandSenderEntity();
        if (commandSenderEntity!=null) {
            EntityPlayer player= (EntityPlayer) commandSenderEntity;
            int totalArmorValue = player.getTotalArmorValue();
            float toughness = (float) player.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue();
            sender.sendMessage(new TextComponentString("Total Armor Value: " + totalArmorValue));
            sender.sendMessage(new TextComponentString("toughness: " + toughness));
        }
    }
}
