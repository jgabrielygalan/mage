/*
 *  Copyright 2010 BetaSteward_at_googlemail.com. All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without modification, are
 *  permitted provided that the following conditions are met:
 *
 *     1. Redistributions of source code must retain the above copyright notice, this list of
 *        conditions and the following disclaimer.
 *
 *     2. Redistributions in binary form must reproduce the above copyright notice, this list
 *        of conditions and the following disclaimer in the documentation and/or other materials
 *        provided with the distribution.
 *
 *  THIS SOFTWARE IS PROVIDED BY BetaSteward_at_googlemail.com ``AS IS'' AND ANY EXPRESS OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 *  FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL BetaSteward_at_googlemail.com OR
 *  CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 *  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 *  SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 *  ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 *  NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 *  ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *  The views and conclusions contained in the software and documentation are those of the
 *  authors and should not be interpreted as representing official policies, either expressed
 *  or implied, of BetaSteward_at_googlemail.com.
 */
package mage.sets.ravnika;

import java.util.List;
import java.util.UUID;
import mage.MageObject;
import mage.abilities.Ability;
import mage.abilities.effects.OneShotEffect;
import mage.cards.Card;
import mage.cards.CardImpl;
import mage.cards.Cards;
import mage.cards.CardsImpl;
import mage.constants.CardType;
import mage.constants.Outcome;
import mage.constants.Rarity;
import mage.constants.Zone;
import mage.filter.FilterCard;
import mage.filter.common.FilterCreatureCard;
import mage.game.Game;
import mage.players.Player;
import mage.target.TargetCard;
import mage.target.common.TargetCardInLibrary;

/**
 *
 * @author LevelX2
 */
public class CongregationAtDawn extends CardImpl<CongregationAtDawn> {

    public CongregationAtDawn(UUID ownerId) {
        super(ownerId, 198, "Congregation at Dawn", Rarity.UNCOMMON, new CardType[]{CardType.INSTANT}, "{G}{G}{W}");
        this.expansionSetCode = "RAV";

        this.color.setGreen(true);
        this.color.setWhite(true);

        // Search your library for up to three creature cards and reveal them. Shuffle your library, then put those cards on top of it in any order.
        this.getSpellAbility().addEffect(new CongregationAtDawnEffect());
    }

    public CongregationAtDawn(final CongregationAtDawn card) {
        super(card);
    }

    @Override
    public CongregationAtDawn copy() {
        return new CongregationAtDawn(this);
    }
}

class CongregationAtDawnEffect extends OneShotEffect<CongregationAtDawnEffect> {
    static final private String textTop = "card to put on your library (last chosen will be on top)";

    public CongregationAtDawnEffect() {
        super(Outcome.Benefit);
        this.staticText = "Search your library for up to three creature cards and reveal them. Shuffle your library, then put those cards on top of it in any order";
    }

    public CongregationAtDawnEffect(final CongregationAtDawnEffect effect) {
        super(effect);
    }

    @Override
    public CongregationAtDawnEffect copy() {
        return new CongregationAtDawnEffect(this);
    }

    @Override
    public boolean apply(Game game, Ability source) {
        Player controller = game.getPlayer(source.getControllerId());
        MageObject sourceObject = game.getObject(source.getSourceId());
        if (controller != null && sourceObject != null) {
            TargetCardInLibrary target = new TargetCardInLibrary(0, 3, new FilterCreatureCard("creature cards"));
            if (controller.searchLibrary(target, game)) {
                if (target.getTargets().size() > 0) {
                    Cards revealed = new CardsImpl();
                    for (UUID cardId : (List<UUID>) target.getTargets()) {
                        Card card = controller.getLibrary().getCard(cardId, game);
                        revealed.add(card);
                    }
                    controller.revealCards(sourceObject.getName(), revealed, game);

                    controller.shuffleLibrary(game);

                    TargetCard targetToLib = new TargetCard(Zone.PICK, new FilterCard(textTop));
                    target.setRequired(true);
                    while (revealed.size() > 1) {
                        controller.choose(Outcome.Neutral, revealed, target, game);
                        Card card = revealed.get(targetToLib.getFirstTarget(), game);
                        if (card != null) {
                            revealed.remove(card);
                            card.moveToZone(Zone.LIBRARY, source.getSourceId(), game, true);
                        }
                        targetToLib.clearChosen();
                    }
                    if (revealed.size() == 1) {
                        Card card = revealed.get(revealed.iterator().next(), game);
                        card.moveToZone(Zone.LIBRARY, source.getSourceId(), game, true);
                    }

                }

                return true;
            }
        }
        return false;
    }
}