/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mage.abilities.common;

import mage.abilities.StaticAbility;
import mage.abilities.effects.common.combat.AttacksIfAbleSourceEffect;
import mage.constants.Duration;
import mage.constants.Zone;

/**
 *
 * @author LevelX2
 */
public class AttacksEachCombatStaticAbility extends StaticAbility {

    public AttacksEachCombatStaticAbility() {
        super(Zone.BATTLEFIELD, new AttacksIfAbleSourceEffect(Duration.WhileOnBattlefield, true));
    }

    public AttacksEachCombatStaticAbility(AttacksEachCombatStaticAbility ability) {
        super(ability);
    }

    @Override
    public AttacksEachCombatStaticAbility copy() {
        return new AttacksEachCombatStaticAbility(this);
    }

}
