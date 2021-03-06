package nightgames.match.team.actions;

import nightgames.characters.Character;
import nightgames.characters.CharacterSex;
import nightgames.global.Global;
import nightgames.items.clothing.Clothing;
import nightgames.items.clothing.ClothingSlot;
import nightgames.items.clothing.Outfit;
import nightgames.match.team.TeamMatch;

public class GiveClothing extends TeammateAction {

    private static final long serialVersionUID = -8837472478312309455L;
    private Clothing given;

    public GiveClothing(TeamMatch match, Character self) {
        super("Give Clothing", match, self);
    }

    @Override
    boolean additionalRequirements() {
        return decideClothing() != null;
    }
    
    @Override
    public String describe(Character doer) {

        Clothing picked = decideClothing();
        boolean appropriate = isAppropriate(picked, getCaptain());
        if (getCaptain().getOutfit().slotEmpty(ClothingSlot.bottom) && picked.getSlots().contains(ClothingSlot.bottom)) {
            String msg = "Looking at {other:name-possessive} exposed {other:main-genitals} thoughtfully for"

                            + " a moment, {self:subject-action:say|says} <i>\"You know, {other:name}, with you"
                            + " being our leader and all, I think you should at least have some underwear on you.\"</i>"
                            + " Without waiting for a response, {self:action:strip|strips} {self:possessive} "
                            + given.getName() + " off and hands it to {other:direct-object}. ";
            if (appropriate) {
                return Global.format(msg + "{other:SUBJECT} graciously {other:action:accept|accepts}"
                                + " the garment and {other:action:put|puts} it on.", self, getCaptain());
            } else {
                return Global.format(msg + "{other:SUBJECT-ACTION:look|looks} at {self:name-do} askance"
                                + " for a bit, wondering if this is some kind of joke. It soon becomes clear"
                                + " that it's not, and {other:pronoun} somewhat hesitantly {other:action:put|puts}"
                                + " it on.", self, getCaptain());
            }
        }
        return "";
    }

    @Override
    void effects() {
        given = decideClothing();
        self.getOutfit().unequip(given);
        getCaptain().getOutfit().equip(given);
    }

    private Clothing decideClothing() {
        if (self.getOutfit().isNude()) {
            return null;
        }
        
        Outfit own = self.getOutfit();
        Outfit capt = getCaptain().getOutfit();
        if (capt.getEquipped().equals(capt.getAll())) {
            return null;
        }
        
        if (capt.slotEmpty(ClothingSlot.bottom) && !own.slotEmpty(ClothingSlot.bottom)) {
            return findAppropriate(own, ClothingSlot.bottom);
        }

        if (capt.slotEmpty(ClothingSlot.top) && !own.slotEmpty(ClothingSlot.top)) {
            return findAppropriate(own, ClothingSlot.top);
        }

        return null;
    }
    
    private boolean isAppropriate(Clothing article, Character c) {
        return article.isForGender(c.body.guessCharacterSex())
                        || c.useFemalePronouns() && article.isForGender(CharacterSex.female);
    }
    
    private Clothing findAppropriate(Outfit outfit, ClothingSlot slot) {
        Clothing picked;
        int layer = Clothing.N_LAYERS;
        do {
            picked = outfit.getSlotAt(slot, --layer);
            if (picked != null && !isAppropriate(picked, getCaptain())) {
                picked = null;
            }
        } while (picked == null && layer > 0);
        
        return picked == null ? outfit.getTopOfSlot(slot) : picked;
    }
}
