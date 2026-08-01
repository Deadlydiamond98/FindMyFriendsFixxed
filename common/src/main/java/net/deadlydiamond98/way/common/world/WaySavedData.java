package net.deadlydiamond98.way.common.world;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.saveddata.SavedData;

public class WaySavedData extends SavedData {

    public static final String ID = "way_saved_data";

    public static final SavedData.Factory<WaySavedData> FACTORY =
            new SavedData.Factory<>(WaySavedData::new, WaySavedData::fromNbt, DataFixTypes.LEVEL);

    public static WaySavedData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(FACTORY, ID);
    }

    public static WaySavedData get(MinecraftServer server) {
        return get(server.overworld());
    }

    // Data Saved to overworld for variables that are global

    // LOCK
    private boolean lockColor = false;
    private boolean lockSee = false;

    // COLOR
    private boolean colorDistance = false;
    private boolean namePainFlash = true;
    private boolean namePainGetRedder = false;

    // DISTANCE
    private int minRender = 0;
    private int maxRender = 999999;

    // OTHER
    private boolean forceOptIn = false;
    private boolean seeTeamColorOnly = false;
    private boolean teamColourNoFriendlyFire = false;
    private int packetUpdateRate = 5;

    @Override
    public CompoundTag save(CompoundTag nbt, HolderLookup.Provider registries) {
        nbt.putBoolean("lockColorWay", this.lockColor);
        nbt.putBoolean("lockSeeWay", this.lockSee);

        nbt.putBoolean("colorDistanceWay", this.colorDistance);
        nbt.putBoolean("namePainFlashWay", this.namePainFlash);
        nbt.putBoolean("namePainGetRedder", this.namePainGetRedder);

        nbt.putBoolean("forceOptInWay", this.forceOptIn);
        nbt.putBoolean("seeTeamColorOnlyWay", this.seeTeamColorOnly);
        nbt.putBoolean("teamColourNoFriendlyFireWay", this.teamColourNoFriendlyFire);
        nbt.putInt("packetUpdateRate", this.packetUpdateRate);
        nbt.putInt("minRender", this.minRender);
        nbt.putInt("maxRender", this.maxRender);
        return nbt;
    }

    public static WaySavedData fromNbt(CompoundTag nbt, HolderLookup.Provider registries) {
        WaySavedData data = new WaySavedData();

        data.lockColor = nbt.getBoolean("lockColorWay");
        data.lockSee = nbt.getBoolean("lockSeeWay");

        data.colorDistance = nbt.getBoolean("colorDistanceWay");
        data.namePainFlash = nbt.getBoolean("namePainFlashWay");
        data.namePainGetRedder = nbt.getBoolean("namePainGetRedder");

        data.forceOptIn = nbt.getBoolean("forceOptInWay");
        data.seeTeamColorOnly = nbt.getBoolean("seeTeamColorOnlyWay");
        data.teamColourNoFriendlyFire = nbt.getBoolean("teamColourNoFriendlyFireWay");
        data.packetUpdateRate = nbt.getInt("packetUpdateRate");

        data.minRender = nbt.getInt("minRender");
        data.maxRender = nbt.getInt("maxRender");
        return data;
    }

    // GETTERS

    public boolean colorDistance() {
        return this.colorDistance;
    }

    public boolean namePainFlash() {
        return this.namePainFlash;
    }

    public boolean namePainGetRedder() {
        return this.namePainGetRedder;
    }

    public boolean forceOptIn() {
        return this.forceOptIn;
    }

    public boolean seeTeamColorOnly() {
        return this.seeTeamColorOnly;
    }

    public boolean teamColourNoFriendlyFire() {
        return this.teamColourNoFriendlyFire;
    }

    public int getPacketUpdateRate() {
        return this.packetUpdateRate;
    }

    public int getMinRender() {return this.minRender;}

    public int getMaxRender() {return this.maxRender;}

    // SETTERS

    public void setColorDistance(boolean colorDistance) {
        this.colorDistance = colorDistance;
        this.setDirty();
    }

    public void setNamePainFlash(boolean namePainFlash) {
        this.namePainFlash = namePainFlash;
        this.setDirty();
    }

    public void setNamePainGetRedder(boolean namePainGetRedder) {
        this.namePainGetRedder = namePainGetRedder;
        this.setDirty();
    }

    public void setForceOptIn(boolean forceOptIn) {
        this.forceOptIn = forceOptIn;
        this.setDirty();
    }

    public void setSeeTeamColorOnly(boolean seeTeamColorOnly) {
        this.seeTeamColorOnly = seeTeamColorOnly;
        this.setDirty();
    }

    public void setTeamColourNoFriendlyFire(boolean teamColourNoFriendlyFire) {
        this.teamColourNoFriendlyFire = teamColourNoFriendlyFire;
        this.setDirty();
    }

    public void setPacketUpdateRate(int packetUpdateRate) {
        this.packetUpdateRate = packetUpdateRate;
        this.setDirty();
    }

    public void setMinRender(int minRender) {
        this.minRender = minRender;
        this.setDirty();
    }

    public void setMaxRender(int maxRender) {
        this.maxRender = maxRender;
        this.setDirty();
    }

    // LOCKED GETTERS AND SETTERS

    public boolean colorLocked() {
        return this.lockColor;
    }

    public void setColorLocked(boolean lockColor) {
        this.lockColor = lockColor;
        this.setDirty();
    }

    public boolean seeLocked() {
        return this.lockSee;
    }

    public void setSeeLocked(boolean lockSee) {
        this.lockSee = lockSee;
        this.setDirty();
    }
}
