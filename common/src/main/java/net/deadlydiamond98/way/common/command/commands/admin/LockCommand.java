package net.deadlydiamond98.way.common.command.commands.admin;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.deadlydiamond98.way.common.command.commands.AbstractWayCommand;
import net.deadlydiamond98.way.common.world.WaySavedData;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.Collection;
import java.util.List;

public class LockCommand extends AbstractWayCommand {

    private final getPersistantState getter;
    private final setPersistantState setter;
    private final String lockType;

    public LockCommand(String type, getPersistantState getter, setPersistantState setter) {
        super(2, "lock");
        this.lockType = type;
        this.getter = getter;
        this.setter = setter;
    }

    public boolean getValue(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            return getter.get(WaySavedData.get(serverPlayer.getServer()));
        }
        return false;
    }

    @Override
    protected void execute(CommandContext<CommandSourceStack> context, Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            setter.set(WaySavedData.get(serverPlayer.getServer()), BoolArgumentType.getBool(context, type));
        }
    }

    @Override
    protected Object getNewValue(CommandContext<CommandSourceStack> context) {
        return BoolArgumentType.getBool(context, type);
    }

    @Override
    protected List<ArgumentBuilder<CommandSourceStack, ?>> getExtraCommandParts() {
        return List.of(Commands.literal(this.lockType), Commands.argument(type, BoolArgumentType.bool()));
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

    @Override
    protected String getID(CommandContext<CommandSourceStack> context, Player player) {
        return super.getID(context, player) + "." + this.lockType;
    }

    @FunctionalInterface
    public interface getPersistantState {
        boolean get(WaySavedData data);
    }

    @FunctionalInterface
    public interface setPersistantState {
        void set(WaySavedData data, boolean bool);
    }
}
