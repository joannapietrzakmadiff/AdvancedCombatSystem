package com.byalexeykh.advancedcombatsystem.items;

import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TieredItem;

public class AdvancedTiredItem extends TieredItem {
    // ACS attributes
    private final float angle;
    private final float range;
    private final float neededBackswingTicks;
    private final float minBackswingTicks;
    private final int maxComboNum;
    public AdvancedTiredItem(IItemTier tier, float angleIn, float rangeIn, float neededBackswingTicksIn, float minBackswingTicksIn, int maxComboNumIn, Properties builder) {
        super(tier, builder);
        this.angle = angleIn;
        this.range = rangeIn;
        this.neededBackswingTicks = neededBackswingTicksIn;
        this.minBackswingTicks = minBackswingTicksIn;
        this.maxComboNum = maxComboNumIn;
    }

    public ACSAttributesContainer getACSAttributes(){
        return new ACSAttributesContainer(this.angle, this.range, this.neededBackswingTicks, this.minBackswingTicks, this.maxComboNum);
    }
}
