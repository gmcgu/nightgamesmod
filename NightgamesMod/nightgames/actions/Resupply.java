package nightgames.actions;

import nightgames.characters.Character;
import nightgames.characters.State;
import nightgames.characters.Trait;
import nightgames.global.Flag;
import nightgames.global.Global;
import nightgames.items.Item;
import nightgames.match.ftc.FTCMatch;

public class Resupply extends Action {

    /**
     *
     */
    private static final long serialVersionUID = -3349606637987124335L;

    public Resupply() {
        super("Resupply");
    }

    @Override
    public boolean usable(Character user) {
        return !user.bound() && (user.location().resupply() || Global.checkFlag(Flag.CanResupplyAnywhere) || user.has(Trait.immobile)
                        || (Global.checkFlag(Flag.FTC) && ((FTCMatch) Global.getMatch()).isBase(user, user.location())));
    }

    @Override
    public IMovement execute(Character user) {
        if (Global.checkFlag(Flag.FTC)) {
            FTCMatch match = (FTCMatch) Global.getMatch();
            if (user.human()) {
                Global.gui().message("You get a change of clothes from the chest placed here.");
            }
            if (user.has(Item.Flag) && !match.isPrey(user)) {
                match.turnInFlag(user);
            } else if (match.canCollectFlag(user)) {
                match.grabFlag();
            }
        } else {
            if (user.human()) {
                if (Global.getMatch().getCondition().name().equals("nudist")) {
                    Global.gui().message(
                                    "You check in so that you're eligible to fight again, but you still don't get any clothes.");
                } else {
                    Global.gui().message("You pick up a change of clothes and prepare to get back in the fray.");
                }
            }
        }
        user.state = State.resupplying;
        return Movement.resupply;
    }

    @Override
    public IMovement consider() {
        return Movement.resupply;
    }

}
