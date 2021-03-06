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
package mage.sets.mirrodin;

import java.util.UUID;

import mage.constants.CardType;
import mage.constants.Rarity;
import mage.abilities.Ability;
import mage.abilities.common.SimpleActivatedAbility;
import mage.abilities.costs.common.SacrificeSourceCost;
import mage.abilities.costs.common.TapSourceCost;
import mage.abilities.costs.mana.GenericManaCost;
import mage.abilities.effects.OneShotEffect;
import mage.abilities.effects.common.counter.AddCountersTargetEffect;
import mage.cards.CardImpl;
import mage.constants.Outcome;
import mage.constants.Zone;
import mage.counters.CounterType;
import mage.game.Game;
import mage.game.permanent.Permanent;
import mage.target.TargetPermanent;

/**
 * @author Loki
 */
public class OblivionStone extends CardImpl {

    public OblivionStone(UUID ownerId) {
        super(ownerId, 222, "Oblivion Stone", Rarity.RARE, new CardType[]{CardType.ARTIFACT}, "{3}");
        this.expansionSetCode = "MRD";

        // {4}, {tap}: Put a fate counter on target permanent.
        Ability ability = new SimpleActivatedAbility(Zone.BATTLEFIELD, new AddCountersTargetEffect(CounterType.FATE.createInstance()), new GenericManaCost(4));
        ability.addCost(new TapSourceCost());
        ability.addTarget(new TargetPermanent());
        this.addAbility(ability);
        // {5}, {tap}, Sacrifice Oblivion Stone: Destroy each nonland permanent without a fate counter on it, then remove all fate counters from all permanents.
        ability = new SimpleActivatedAbility(Zone.BATTLEFIELD, new OblivionStoneEffect(), new GenericManaCost(5));
        ability.addCost(new TapSourceCost());
        ability.addCost(new SacrificeSourceCost());
        this.addAbility(ability);
    }

    public OblivionStone(final OblivionStone card) {
        super(card);
    }

    @java.lang.Override
    public OblivionStone copy() {
        return new OblivionStone(this);
    }
}

class OblivionStoneEffect extends OneShotEffect {
    OblivionStoneEffect() {
        super(Outcome.DestroyPermanent);
        staticText = "Destroy each nonland permanent without a fate counter on it, then remove all fate counters from all permanents";
    }

    OblivionStoneEffect(final OblivionStoneEffect effect) {
        super(effect);
    }

    @java.lang.Override
    public boolean apply(Game game, Ability source) {
        for (Permanent p : game.getBattlefield().getAllActivePermanents()) {
            if (!(p.getCardType().contains(CardType.LAND) || p.getCounters().containsKey(CounterType.FATE))) {
                p.destroy(source.getSourceId(), game, false);
            }
        }
        for (Permanent p : game.getBattlefield().getAllActivePermanents()) {
            if (p.getCounters().containsKey(CounterType.FATE)) {
                p.getCounters().removeCounter(CounterType.FATE, p.getCounters().getCount(CounterType.FATE));
            }
        }
        return true;
    }

    @java.lang.Override
    public OblivionStoneEffect copy() {
        return new OblivionStoneEffect(this);
    }
}
