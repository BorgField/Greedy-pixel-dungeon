package com.shatteredpixel.shatteredpixeldungeon.items.potions.mini;

import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.Recipe;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfExperience;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfFrost;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHaste;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfInvisibility;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLevitation;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLiquidFlame;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfMindVision;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfParalyticGas;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfPurity;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfStrength;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfCleansing;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfCorrosiveGas;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfDivineInspiration;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfDragonsBreath;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfEarthenArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfMagicalSight;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfMastery;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfShielding;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfShroudingFog;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfSnapFreeze;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfStamina;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfStormClouds;
import com.watabou.utils.Reflection;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class MiniPotion extends Potion {
    {
        //sprite = equivalent potion sprite but one row down
    }

    public static final LinkedHashMap<Class<?extends Potion>, Class<?extends MiniPotion>> regToMini = new LinkedHashMap<>();
    public static final LinkedHashMap<Class<?extends MiniPotion>, Class<?extends Potion>> miniToReg = new LinkedHashMap<>();
    static{
        regToMini.put(PotionOfStrength.class, PotionOfBurst.class);
        miniToReg.put(PotionOfBurst.class, PotionOfStrength.class);

//        regToMini.put(PotionOfHealing.class, PotionOfShielding.class);
//        miniToReg.put(PotionOfShielding.class, PotionOfHealing.class);
//
//        regToMini.put(PotionOfMindVision.class, PotionOfMagicalSight.class);
//        miniToReg.put(PotionOfMagicalSight.class, PotionOfMindVision.class);
//
//        regToMini.put(PotionOfFrost.class, PotionOfSnapFreeze.class);
//        miniToReg.put(PotionOfSnapFreeze.class, PotionOfFrost.class);
//
//        regToMini.put(PotionOfLiquidFlame.class, PotionOfDragonsBreath.class);
//        miniToReg.put(PotionOfDragonsBreath.class, PotionOfLiquidFlame.class);
//
//        regToMini.put(PotionOfToxicGas.class, PotionOfCorrosiveGas.class);
//        miniToReg.put(PotionOfCorrosiveGas.class, PotionOfToxicGas.class);
//
//        regToMini.put(PotionOfHaste.class, PotionOfStamina.class);
//        miniToReg.put(PotionOfStamina.class, PotionOfHaste.class);
//
//        regToMini.put(PotionOfInvisibility.class, PotionOfShroudingFog.class);
//        miniToReg.put(PotionOfShroudingFog.class, PotionOfInvisibility.class);
//
//        regToMini.put(PotionOfLevitation.class, PotionOfStormClouds.class);
//        miniToReg.put(PotionOfStormClouds.class, PotionOfLevitation.class);
//
//        regToMini.put(PotionOfParalyticGas.class, PotionOfEarthenArmor.class);
//        miniToReg.put(PotionOfEarthenArmor.class, PotionOfParalyticGas.class);
//
//        regToMini.put(PotionOfPurity.class, PotionOfCleansing.class);
//        miniToReg.put(PotionOfCleansing.class, PotionOfPurity.class);
//
//        regToMini.put(PotionOfExperience.class, PotionOfDivineInspiration.class);
//        miniToReg.put(PotionOfDivineInspiration.class, PotionOfExperience.class);

    }

    @Override
    public boolean isKnown() {
        return anonymous || (handler != null && handler.isKnown( miniToReg.get(this.getClass()) ));
    }

    @Override
    public void setKnown() {
        if (!isKnown()) {
            handler.know(miniToReg.get(this.getClass()));
            updateQuickslot();
        }
    }

    @Override
    public void reset() {
        super.reset();
        if (handler != null && handler.contains(miniToReg.get(this.getClass()))) {
            image = handler.image(miniToReg.get(this.getClass())) + 48;
            color = handler.label(miniToReg.get(this.getClass()));
        }
    }

    @Override
    // 迷你药水价值为普通药水的80%
    public int value() {
        return (int)(Reflection.newInstance(miniToReg.get(getClass())).value() * 0.5f);
    }

    @Override
    // 迷你药水能量值为普通药水的80%
    public int energyVal() {
        return (int)(Reflection.newInstance(miniToReg.get(getClass())).energyVal() * 0.5f);
    }

    public static class PotionToMini extends Recipe {

        @Override
        public boolean testIngredients(ArrayList<Item> ingredients) {
            if (ingredients.size() == 1 && regToMini.containsKey(ingredients.get(0).getClass())){
                return true;
            }

            return false;
        }

        @Override
        // 合成消耗0点能量
        public int cost(ArrayList<Item> ingredients) {
            return 0;
        }

        @Override
        public Item brew(ArrayList<Item> ingredients) {
            Item ingredient = ingredients.get(0);
            // 消耗1个单位原料药水
            ingredient.quantity(ingredient.quantity() - 1);

            // 生成2个单位的迷你药水
            MiniPotion mini = (MiniPotion)Reflection.newInstance(regToMini.get(ingredient.getClass()));
            mini.quantity(2);
            return mini;
        }

        @Override
        public Item sampleOutput(ArrayList<Item> ingredients) {
            // 返回2个单位的迷你药水
            MiniPotion mini = (MiniPotion)Reflection.newInstance(regToMini.get(ingredients.get(0).getClass()));
            mini.quantity(2);
            return mini;
        }
    }
}
