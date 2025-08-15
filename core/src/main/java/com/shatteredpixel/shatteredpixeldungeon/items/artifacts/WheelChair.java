package com.shatteredpixel.shatteredpixeldungeon.items.artifacts;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Regeneration;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vulnerable;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.warrior.HeroicLeap;
import com.shatteredpixel.shatteredpixeldungeon.effects.Chains;
import com.shatteredpixel.shatteredpixeldungeon.effects.Effects;
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfEnergy;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.levels.MiningLevel;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.shatteredpixel.shatteredpixeldungeon.ui.ActionIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.BArray;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class WheelChair extends Artifact {
    public static final String AC_JUMP       = "JUMP";

    {
        image = ItemSpriteSheet.ROUND_SHIELD;

        levelCap = 5;
        exp = 0;

        charge = 3;

        defaultAction = AC_JUMP;
        usesTargeting = true;
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions( hero );
        if (isEquipped(hero) && charge > 0 && !cursed && hero.buff(MagicImmune.class) == null) {
            actions.add(AC_JUMP);
        }
        return actions;
    }

    public int targetingPos( Hero user, int dst ){
        return dst;
    }

    @Override
    public void execute(Hero hero, String action) {

        super.execute(hero, action);

        if (hero.buff(MagicImmune.class) != null) return;

        if (action.equals(AC_JUMP)){

            curUser = hero;

            if (!isEquipped( hero )) {
                GLog.i( Messages.get(Artifact.class, "need_to_equip") );
                usesTargeting = false;

            } else if (charge < 1) {
                GLog.i( Messages.get(this, "no_charge") );
                usesTargeting = false;

            } else if (cursed) {
                GLog.w( Messages.get(this, "cursed") );
                usesTargeting = false;

            } else {
                usesTargeting = true;
                GameScene.selectCell(caster);
            }

        }
    }

    @Override
    public void resetForTrinity(int visibleLevel) {
        super.resetForTrinity(visibleLevel);
        charge = Math.min( 3 + level(), 10); //sets charge to soft cap
    }

    public CellSelector.Listener caster = new CellSelector.Listener(){

        @Override
        public void onSelect(Integer target) {
            if (target != null && (Dungeon.level.visited[target] || Dungeon.level.mapped[target])){

                PathFinder.buildDistanceMap(target, BArray.or(Dungeon.level.passable, Dungeon.level.avoid, null));
                if (!(Dungeon.level instanceof MiningLevel) && PathFinder.distance[curUser.pos] == Integer.MAX_VALUE){
                    GLog.w( Messages.get(EtherealChains.class, "cant_reach") );
                    return;
                }

                final Ballistica chain = new Ballistica(hero.pos, target, Ballistica.STOP_TARGET | Ballistica.STOP_SOLID);

                int cell = chain.collisionPos;

                int backTrace = chain.dist-1;
                while (Actor.findChar( cell ) != null && cell != hero.pos) {
                    cell = chain.path.get(backTrace);
                    backTrace--;
                }

                final int dest = cell;
                hero.busy();
                hero.sprite.jump(hero.pos, cell, new Callback() {
                    @Override
                    public void call() {
                        hero.move(dest);
                        Dungeon.level.occupyCell(hero);
                        Dungeon.observe();
                        GameScene.updateFog();

                        WandOfBlastWave.BlastWave.blast(dest);
                        PixelScene.shake(2, 0.5f);

                        Invisibility.dispel();
                        hero.spendAndNext(Actor.TICK);
                    }
                });


            }

        }

        @Override
        public String prompt() {
            return Messages.get(EtherealChains.class, "prompt");
        }
    };



    @Override
    protected Artifact.ArtifactBuff passiveBuff() {
        return new wheelRecharge();
    }

    @Override
    public void charge(Hero target, float amount) {
        if (cursed || target.buff(MagicImmune.class) != null) return;
        int chargeTarget = 5+(level()*2);
        if (charge < chargeTarget*2){
            partialCharge += 0.5f*amount;
            while (partialCharge >= 1){
                partialCharge--;
                charge++;
                updateQuickslot();
            }
        }
    }

    @Override
    public String desc() {
        String desc = super.desc();

        if (isEquipped( hero )){
            desc += "\n\n";
            if (cursed)
                desc += Messages.get(this, "desc_cursed");
            else
                desc += Messages.get(this, "desc_equipped");
        }
        return desc;
    }

    private static int DistanceStacks = 0;
    private static boolean movedLastTurn = true;

    public class wheelRecharge extends Artifact.ArtifactBuff {
        @Override
        public boolean act() {
            int chargeTarget = 5+(level()*2);
            if (charge < chargeTarget
                    && !cursed
                    && target.buff(MagicImmune.class) == null
                    && Regeneration.regenOn()) {
                //gains a charge in 40 - 2*missingCharge turns
                float chargeGain = (1 / (40f - (chargeTarget - charge)*2f));
                chargeGain *= RingOfEnergy.artifactChargeMultiplier(target);
                partialCharge += chargeGain;
            } else if (cursed && Random.Int(100) == 0){
                Buff.prolong( target, Cripple.class, 10f);
            }

            while (partialCharge >= 1) {
                partialCharge --;
                charge ++;
            }

            Buff.affect(hero, DistanceStacks.class);
            // 重置移动标志
            movedLastTurn = false;
            updateQuickslot();
            spend( TICK );
            return true;
        }

        public void gainExp( float levelPortion ) {
            if (cursed || target.buff(MagicImmune.class) != null || levelPortion == 0) return;

            exp += Math.round(levelPortion*100);

            //past the soft charge cap, gaining  charge from leveling is slowed.
            if (charge > 5+(level()*2)){
                levelPortion *= (5+((float)level()*2))/charge;
            }
            partialCharge += levelPortion*6f;

            if (exp > 100+level()*100 && level() < levelCap){
                exp -= 100+level()*100;
                GLog.p( Messages.get(this, "levelup") );
                Catalog.countUses(EtherealChains.class, 2);
                upgrade();
            }

        }
    }
    public static class DistanceStacks extends FlavourBuff {
        public void gainStack(){
            movedLastTurn = true;
//            postpone(target.cooldown()+(1/target.speed()));
            DistanceStacks = DistanceStacks +1;
//            ActionIndicator.setAction(this);
            BuffIndicator.refreshHero();
        }
    }
}
