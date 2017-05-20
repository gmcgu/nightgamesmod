package nightgames.status;

import com.google.gson.JsonObject;

import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.skills.Skill;

public class FocussedRecovery extends FocussedBase {

    public FocussedRecovery(Character affected) {
        super("Focussed on Recovery", affected, "recuperating and building strength",
                        "{self:possessive} ability to cause and withstand pleasure");
    }

    @Override
    public double sensitivity(double x, BodyPart withPart, BodyPart targetPart, Skill skill) {
<<<<<<< HEAD
        return 1.5;
=======
        return 1;
>>>>>>> pr/2
    }

    @Override
    public double opponentSensitivity(double x, BodyPart withPart, BodyPart targetPart, Skill skill) {
<<<<<<< HEAD
        return -1.5;
=======
        return -1;
>>>>>>> pr/2
    }

    @Override
    public int gainmojo(int x) {
        return x;
    }
    
    @Override
    public int spendmojo(int x) {
        return (int) (x / 2);
    }
    
    @Override
    public void tick(Combat c) {
        if (c != null) {
            c.write(affected, Global.format("{self:SUBJECT-ACTION:take} a deep breath, restoring"
                        + " some of {self:possessive} energy and calming {self:possessive}"
<<<<<<< HEAD
                        + " nerves.", affected, c.getOpponent(affected)));
=======
                        + " nerves", affected, c.getOpponent(affected)));
>>>>>>> pr/2
        }
        affected.calm(c, affected.getArousal().max() / 20);
        affected.heal(c, affected.getStamina().max() / 10);
    }
    
    @Override
    public Status instance(Character newAffected, Character newOther) {
        return new FocussedRecovery(newAffected);
    }

    @Override
    public JsonObject saveToJson() {
        return null;
    }

    @Override
    public Status loadFromJson(JsonObject obj) {
        return null;
    }

}
