package net.deadlydiamond98.way.common.command.commands.admin;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.deadlydiamond98.way.common.command.commands.AbstractWayCommand;
import net.deadlydiamond98.way.common.world.WaySavedData;
import net.deadlydiamond98.way.util.mixin.IWayPlayer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.Collection;
import java.util.List;

public class PersistantStateIntegerCommand extends AbstractWayCommand {

    private final getPersistantState getter;
    private final setPersistantState setter;
    private final boolean sendPacket;
    private final int min;
    private final int max;

    public PersistantStateIntegerCommand(String type, getPersistantState getter, setPersistantState setter, int min, int max, boolean sendPacket) {
        super(2, type);
        this.getter = getter;
        this.setter = setter;
        this.min = min;
        this.max = max;
        this.sendPacket = sendPacket;
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
            setter.set(
                    WaySavedData.get(serverPlayer.getServer()),
                    IntegerArgumentType.getInteger(context, type)
            );
            if (serverPlayer instanceof IWayPlayer && this.sendPacket) {
                player.getCommandSenderWorld().players().forEach(player1 -> ((IWayPlayer) player1).way$updateRenderPreferences());
            }
        }
    }

    @Override
    protected Object getNewValue(CommandContext<CommandSourceStack> context) {
        return IntegerArgumentType.getInteger(context, type);
    }

    @Override
    protected List<ArgumentBuilder<CommandSourceStack, ?>> getExtraCommandParts() {
        return List.of(Commands.argument(type, IntegerArgumentType.integer(this.min, this.max)));
    }

    @Override
    protected boolean multiTarget() {
        return false;
    }

    @Override
    protected void successMSG(CommandContext<CommandSourceStack> context, Collection<? extends Player> players) {
        MutableComponent base = Component.translatable(LANG_PREFIX + getID(context, players.iterator().next()), getNewValue(context));
        sendSuccess(context, base, players.iterator().next());
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
