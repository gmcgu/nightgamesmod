package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.items.clothing.ClothingTrait;

public class Knee extends Skill {

    public Knee(Character self) {
        super("Knee", self);
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return c.getStance().mobile(getSelf()) && !c.getStance().prone(getSelf()) && getSelf().canAct()
                        && c.getStance().front(target) && !c.getStance().connected();
    }

    @Override
    public int getMojoCost(Combat c) {
        return 15;
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        if (target.roll(this, c, accuracy(c))) {
            if (getSelf().human()) {
                c.write(getSelf(), deal(c, 0, Result.normal, target));
            } else if (c.shouldPrintReceive(target)) {
                if (c.getStance().prone(target)) {
                    c.write(getSelf(), receive(c, 0, Result.special, target));
                } else {
                    c.write(getSelf(), receive(c, 0, Result.normal, target));
                }
                if (target.hasBalls() && Global.random(5) >= 3) {
                    c.write(getSelf(), getSelf().bbLiner(c));
                }
            }
            if (target.has(Trait.achilles) && !target.has(ClothingTrait.armored)) {
                target.pain(c, 20 + Global.random(6) + Math.min(getSelf().get(Attribute.Power), 50));
            }
            if (target.has(ClothingTrait.armored) || target.has(Trait.brassballs)) {
                target.pain(c, Global.random(6) + Math.min(getSelf().get(Attribute.Power) / 2, 50));
            } else {
                target.pain(c, 4 + Global.random(11) + Math.min(getSelf().get(Attribute.Power), 50));
            }

            target.emote(Emotion.angry, 20);
        } else {
            writeOutput(c, Result.miss, target);
            return false;
        }
        return true;
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.get(Attribute.Power) >= 10;
    }

    @Override
    public Skill copy(Character user) {
        return new Knee(user);
    }

    @Override
    public int speed() {
        return 4;
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.damage;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.miss) {
            return target.name() + " blocks your knee strike.";
        }
        return "You deliver a powerful knee strike to " + target.name()
                        + "'s delicate lady flower. She lets out a pained whimper and nurses her injured parts.";
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        String victim = target.hasBalls() ? "balls" : "crotch";
        if (modifier == Result.miss) {
            return String.format("%s tries to knee %s in the %s, but %s %s it.",
                            getSelf().subject(), target.nameDirectObject(),
                            victim, target.pronoun(),
                                            target.action("block"));
        }
        if (modifier == Result.special) {
            return String.format("%s raises one leg into the air, then brings %s knee "
                            + "down like a hammer onto %s %s. %s"
                            + " out in pain and instinctively try "
                            + "to close %s legs, but %s holds them open.",
                            getSelf().subject(), getSelf().possessivePronoun(),
                            target.nameOrPossessivePronoun(), victim,
                            Global.capitalizeFirstLetter(target.subjectAction("cry", "cries")),
                            target.possessivePronoun(), getSelf().subject());
        } else {
            return String.format("%s steps in close and brings %s knee up between %s legs, "
                            + "crushing %s fragile balls. %s and nearly %s from the "
                            + "intense pain in %s abdomen.", getSelf().subject(),
                            getSelf().possessivePronoun(), target.nameOrPossessivePronoun(),
                            target.possessivePronoun(),
                            Global.capitalizeFirstLetter(target.subjectAction("groan")),
                            target.action("collapse"), target.possessivePronoun());
        }
    }

    @Override
    public String describe(Combat c) {
        return "Knee opponent in the groin";
    }

    @Override
    public boolean makesContact() {
        return true;
    }
}
