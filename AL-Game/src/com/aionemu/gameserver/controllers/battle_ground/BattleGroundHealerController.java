/**
 * This file is part of Aion-Lightning <aion-lightning.org>.
 *
 *  Aion-Lightning is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Aion-Lightning is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details. *
 *  You should have received a copy of the GNU General Public License
 *  along with Aion-Lightning.
 *  If not, see <http://www.gnu.org/licenses/>.
 */


package com.aionemu.gameserver.controllers.battle_ground;

import com.aionemu.gameserver.controllers.NpcController;
import java.util.Collection;

import com.aionemu.gameserver.controllers.attack.AttackStatus;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.battle_ground.BattleGroundHealer;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ATTACK_STATUS.TYPE;
import com.aionemu.gameserver.services.HTMLService;
import com.aionemu.gameserver.services.ecfunctions.battle_ground.factories.SurveyFactory;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.World;

/**
 * @author Eloann
 */
public class BattleGroundHealerController extends NpcController {

    public void onAttack(final Creature creature, int skillId, TYPE type, int damage, AttackStatus status, boolean notifyAttackedObservers) {
    }

    @Override
    public void onDialogRequest(final Player player) {
        if (player.getCommonData().getRace() != getOwner().getRace()) {
            return;
        }

        Collection<Player> players = World.getInstance().getAllPlayers();
        int BgMap = player.getWorldId();
        int BgInstanceId = player.getInstanceId();
        int BattleGroundPlayers = 0;
        for (Player p : players) {
            if (p.getWorldId() == BgMap && p.getInstanceId() == BgInstanceId) {
                BattleGroundPlayers += 1;
            }
        }

        if (BattleGroundPlayers <= 1) {
            if (player.getCommonData().getRace() == Race.ELYOS) {
                TeleportService2.teleportTo(player, 110010000, 1374f, 1399f, 573f, (byte) 0);
            } else {
                TeleportService2.teleportTo(player, 120010000, 1324f, 1550f, 210f, (byte) 0);
            }
            PacketSendUtility.sendMessage(player, "You were alone in the battleground, you have been teleported back.");
        } else if (!player.getBattleGround().running && !player.battlegroundWaiting) {
            if (player.getCommonData().getRace() == Race.ELYOS) {
                TeleportService2.teleportTo(player, 110010000, 1374f, 1399f, 573f, (byte) 0);
            } else {
                TeleportService2.teleportTo(player, 120010000, 1324f, 1550f, 210f, (byte) 0);
            }
            PacketSendUtility.sendMessage(player, "You were alone in the battleground, you have been teleported back.");
        } else {
            // Show rank
            player.battlegroundRequestedRank = true;
            HTMLService.showHTML(player, SurveyFactory.getBattleGroundRanking(player.getBattleGround()), 152000001);
        }
    }

    @Override
    public BattleGroundHealer getOwner() {
        return (BattleGroundHealer) super.getOwner();
    }
}
