package com.shatteredpixel.shatteredpixeldungeon.items.potions.mini;

import static com.shatteredpixel.shatteredpixeldungeon.items.potions.mini.PotionOfBurst.Burst.DURATION;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;

public class PotionOfBurning extends MiniPotion {
    //炎蚀试剂
    {
        icon = ItemSpriteSheet.Icons.POTION_BURST;

    }

    @Override
    public void apply(Hero hero) {
        identify();
        Buff.affect(hero, PotionOfBurning.Burning.class).setCount(1, 2);
    }

    public static class Burning extends Buff {
        {
            type = buffType.NEGATIVE;
            announced = true;
        }

        @Override
        public int icon() { return BuffIndicator.MINIPOTION; }

        @Override
        public void tintIcon(Image icon) { icon.hardlight(0.80f, 0, 0.13f); }

        @Override
        public float iconFadePercent() { return Math.max(0, (DURATION - visualcooldown()) / DURATION); }

        @Override
        public String desc() {
            return Messages.get(this, "desc", dispTurns(visualcooldown()), lvl, count);
        }

        public int lvl = 0;
        public int count = 0;

        public int proc(int damage, Char attacker, Char defender) {
            if (count > 0) {
                damage += (int) (defender.HT * 0.10f * lvl);
                count--;
            }
            return damage;
        }

        public void setCount(int str, int counts){
            lvl += str;
            count = lvl + counts;
        }

    }
}
