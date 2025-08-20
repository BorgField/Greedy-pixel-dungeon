package com.shatteredpixel.shatteredpixeldungeon.items.potions.mini;

import static com.shatteredpixel.shatteredpixeldungeon.items.potions.mini.PotionOfBurst.Burst.DURATION;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.Splash;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfAggression;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.TendonHookSickle;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PointF;

public class PotionOfBurning extends MiniPotion {
    //炎蚀试剂
    {
        icon = ItemSpriteSheet.Icons.POTION_BURNING;

    }

    @Override
    public void shatter( int cell ) {

        Char ch = Actor.findChar( cell );
        splash( cell );

        if (ch != null) {
            if (Char.hasProp(ch, Char.Property.BOSS) || Char.hasProp(ch, Char.Property.MINIBOSS)) {
                Buff.prolong(ch, StoneOfAggression.Aggression.class, StoneOfAggression.Aggression.DURATION / 4f);
            } else {
                Buff.affect(ch, Burning.class).setCount(1, 3);
            }
        }

        CellEmitter.center(cell).start( Speck.factory( Speck.SCREAM ), 0.3f, 3 );
        Sample.INSTANCE.play( Assets.Sounds.READ );

    }

    public int proc(int damage, Char attacker, Char defender) {
//            if (count > 0) {
//                defender.damage((int) (defender.HT * 0.10f * lvl), attacker);
//                count--;
//            }
        if (defender.isAlive() && defender.buff(Burning.class) != null) {
            // 计算33%的额外伤害（四舍五入）
            int extraDmg = Math.round(damage * 0.33f);
            // 确保至少造成1点伤害
            if (extraDmg < 1) extraDmg = 1;
            // 对defender施加额外伤害
            defender.damage( extraDmg, attacker );
            // 添加视觉效果
            if (defender.sprite.visible) {
                Splash.at(defender.sprite.center(), PointF.PI, PointF.PI / 4,
                        defender.sprite.blood(), Math.min(5 * extraDmg / defender.HT, 6));
            }
        }
        return damage;
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
        public String desc() {
            return Messages.get(this, "desc", lvl, count);
        }

        public int lvl = 0;
        public int count = 0;



        public void setCount(int str, int counts){
            lvl = lvl+str;
            count = lvl + counts;
        }

    }
}
