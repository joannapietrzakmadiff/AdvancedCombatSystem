package com.byalexeykh.advancedcombatsystem.items;

import net.minecraft.block.BlockState;
import net.minecraft.item.*;

public class ACSAttributesContainer {
    public final float ANGLE;
    public final float RANGE;
    public final float NEEDED_BACKSWING_TICKS;
    public final float MIN_BACKSWING_TICKS;
    public final int MAX_COMBO_NUM;
    public final double SPEED_REDUCE_MODIFIER;
    public final float COMBO_CHARGING_SPEED_BOUNS;
    public ACSAttributesContainer(float angle, float range, float neededBackswingTicks, float minBackswingTicks, int maxComboNum, double speedReduceModifier, float comboChargingSpeedBonus){
        this.ANGLE = angle;
        this.RANGE = range;
        this.NEEDED_BACKSWING_TICKS = neededBackswingTicks;
        this.MIN_BACKSWING_TICKS = minBackswingTicks;
        this.MAX_COMBO_NUM = maxComboNum;
        this.SPEED_REDUCE_MODIFIER = speedReduceModifier;
        this.COMBO_CHARGING_SPEED_BOUNS = comboChargingSpeedBonus;
    }

    /**
     * returns default ACS attributes container
     * */
    public static ACSAttributesContainer getDefaultContainer(){
        return new ACSAttributesContainer(30, 6, 6, 3, 6, -0.03d, 0);
    }

    public static boolean canDestroyBySwing(Item item, BlockState block){
        if(item instanceof AdvancedSwordItem){
            return ((AdvancedSwordItem)item).canDestroyBySwing(block);
        }
        else if(item instanceof AdvancedHoeItem){
            return ((AdvancedHoeItem)item).canDestroyBySwing(block);
        }
        else return false;
    }

    /**
     * returns ACS attributes container form item
     * */
    public static ACSAttributesContainer get(Item item){
        if(item instanceof AdvancedTiredItem){
            return ((AdvancedTiredItem)item).getACSAttributes();
        }
        else if(item instanceof SwordItem){
            return new ACSAttributesContainer (50, 7, 16, 5, 4, -0.01d, 0.2f);
        }
        else if(item instanceof PickaxeItem){
            return new ACSAttributesContainer (30, 5, 20, 7, 2, -0.025d, 0.2f);
        }
        else if(item instanceof AxeItem){
            return new ACSAttributesContainer(40, 6, 30, 10, 2, -0.035d, 0.2f);
        }
        else if(item instanceof ShovelItem){
            return new ACSAttributesContainer(40, 6, 20, 6, 3, -0.025d, 0.2f);
        }
        else if(item instanceof HoeItem){
            return new ACSAttributesContainer(70, 5, 20, 8, 2, -0.025d, 0.2f);
        }
        else{
            return getDefaultContainer();
        }
    }

    @Override
    public String toString() {
        return "Angle = " + this.ANGLE + " range = " + this.RANGE + " needed_backswing_ticks = " + this.NEEDED_BACKSWING_TICKS + " max_combo_num = " + this.MAX_COMBO_NUM + " speed_reduce_modifier = " + this.SPEED_REDUCE_MODIFIER + " combo_charging_speed_bonus = " + this.COMBO_CHARGING_SPEED_BOUNS;
    }
}
