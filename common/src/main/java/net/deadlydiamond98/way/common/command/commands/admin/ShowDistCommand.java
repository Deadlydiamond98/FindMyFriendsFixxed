package net.deadlydiamond98.way.common.command.commands.admin;

import com.mojang.brigadier.context.CommandContext;
import net.deadlydiamond98.way.common.command.commands.AbstractWayCommand;
import net.deadlydiamond98.way.common.world.WaySavedData;
import net.deadlydiamond98.way.util.mixin.IWayPlayer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.Collection;

public class ShowDistCommand extends AbstractWayCommand {

    private final getPersistantState getter;
    private final setPersistantState setter;

    public ShowDistCommand(String type, getPersistantState getter, setPersistantState setter) {
        super(2, type);
        this.getter = getter;
        this.setter = setter;
    }

    public int getValue(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            return getter.get(WaySavedData.get(serverPlayer.getServer()));
        }
        return 4;
    }

    @Override
    protected void execute(CommandContext<CommandSourceStack> context, Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            WaySavedData data = WaySavedData.get(serverPlayer.getServer());
            setter.set(data, getter.get(data));
            if (serverPlayer instanceof IWayPlayer) {
                player.getCommandSenderWorld().players().forEach(player1 -> ((IWayPlayer) player1).way$updateRenderPreferences());
            }
        }
    }

    @Override
    protected Object getNewValue(CommandContext<CommandSourceStack> context) {
        return "If you're seeing this, something is wrong!";
    }

    @Override
    protected boolean multiTarget() {
        return false;
    }

    @Override
    protected void successMSG(CommandContext<CommandSourceStack> context, Collection<? extends Player> players) {
        if (players.iterator().next() instanceof ServerPlayer player) {
            int currentDist = getter.get(WaySavedData.get(player.getServer()));
            MutableComponent base = Component.translatable(LANG_PREFIX + getID(context, players.iterator().next()), currentDist);
            sendSuccess(context, base, players.iterator().next());
        }
    }

    @Override
    protected String getID(CommandContext<CommandSourceStack> context, Player player) {
        return super.getID(context, player) + ".show";
    }

    @FunctionalInterface
    public interface getPersistantState {
        int get(WaySavedData data);
    }

    @FunctionalInterface
    public interface setPersistantState {
        void set(WaySavedData data, int bool);
    }
}
