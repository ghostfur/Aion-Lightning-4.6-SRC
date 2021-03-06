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


package com.aionemu.gameserver.services.ecfunctions;

import java.util.ArrayList;
import java.util.List;

import com.aionemu.gameserver.configs.main.EternityConfig;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.knownlist.Visitor;

import java.util.Calendar;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Eloann
 */
public class BalaurInvadeService {

    private static Logger log = LoggerFactory.getLogger(BalaurInvadeService.class);
    private static boolean isStarted = false;
    @SuppressWarnings("unused")
    private long lastCalculationMillis;
    Timer timer;
    int remainingTime;

    public BalaurInvadeService() {
        remainingTime = 10;
        invasionScheduled();
    }

    public final void invasionScheduled() {
        ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
                int MINUTE = Calendar.getInstance().get(Calendar.MINUTE);

                if (hour == EternityConfig.HOUR && MINUTE <= 6) {
                    ThreadPoolManager.getInstance().schedule(new Runnable() {
                        @Override
                        public void run() {
                            startInvasion();
                            startBossInvasion();
                        }
                    }, 10 * 60 * 1000);
                    loadInvasion();
                }
                if (hour >= EternityConfig.HOUR2 && MINUTE <= 6) {
                    ThreadPoolManager.getInstance().schedule(new Runnable() {
                        @Override
                        public void run() {
                            startInvasion2();
                            startBossInvasion2();
                        }
                    }, 10 * 60 * 1000);
                    loadInvasion();
                }
                if (hour >= EternityConfig.HOUR3 && MINUTE <= 6) {
                    ThreadPoolManager.getInstance().schedule(new Runnable() {
                        @Override
                        public void run() {
                            startInvasion3();
                            startBossInvasion3();
                        }
                    }, 10 * 60 * 1000);
                    loadInvasion();
                }
                if (hour >= EternityConfig.HOUR4 && MINUTE <= 6) {
                    ThreadPoolManager.getInstance().schedule(new Runnable() {
                        @Override
                        public void run() {
                            startInvasion4();
                            startBossInvasion4();
                        }
                    }, 10 * 60 * 1000);
                    loadInvasion();
                }
            }
        }, 5 * 60 * 1000, 5 * 60 * 1000);
        log.info("!!! Loading Balaur Invasion Event !!!");
    }

    public boolean loadInvasion() {
        if (isStarted) {
            return false;
        }
        isStarted = true;
        timer = initTimer();
        if (remainingTime <= 0) {
            remainingTime = 3;
        }

        World.getInstance().doOnAllPlayers(new Visitor<Player>() {
            @Override
            public void visit(Player p) {
                if (p.getCommonData().getLevel() > 1) {
                    PacketSendUtility.sendBrightYellowMessageOnCenter(p, "Beware ! Balaur will attack Elysea/Asmodae in " + remainingTime + " minutes");
                }
            }
        });
        timer.start();
        return true;
    }

    public void startInvasion() {
        int radius = 15;
        int amount = 25;
        int despawnTime = 3600;

        List<Integer> idList = new ArrayList<Integer>();
        idList.add(217425);
        idList.add(217451);
        idList.add(217470);
        idList.add(217423);
        idList.add(217457);
        idList.add(210561);
        idList.add(217450);

        float x = 1761.3821f;
        float y = 835.9161f;
        float z = 226.82199f;
        byte heading = 0;
        int worldId = 210040000;

        int templateId;
        SpawnTemplate spawn = null;

        float interval = (float) (Math.PI * 2.0f / amount);
        float x1;
        float y1;
        @SuppressWarnings("unused")
        int spawnCount = 0;

        VisibleObject visibleObject;
        List<VisibleObject> despawnList = new ArrayList<VisibleObject>();

        for (int i = 0; amount > i; i++) {
            templateId = idList.get((int) (Math.random() * idList.size()));
            x1 = (float) (Math.cos(interval * i) * radius);
            y1 = (float) (Math.sin(interval * i) * radius);
            spawn = SpawnEngine.addNewSpawn(worldId, templateId, x + x1, y + y1, z, heading, 0);

            if (spawn == null) {
                return;
            } else {
                visibleObject = SpawnEngine.spawnObject(spawn, 1);

                if (despawnTime > 0) {
                    despawnList.add(visibleObject);
                }

                spawnCount++;
            }
        }

        if (despawnTime > 0) {
            despawnThem(despawnList, despawnTime);
        }
    }

    public void startBossInvasion() {
        int radius = 0;
        int amount = 1;
        int despawnTime = 3600;

        List<Integer> idList = new ArrayList<Integer>();
        idList.add(217472);//Stone

        float x = 1761.3821f;
        float y = 835.9161f;
        float z = 226.82199f;
        byte heading = 0;
        int worldId = 210040000;

        int templateId;
        SpawnTemplate spawn = null;

        float interval = (float) (Math.PI * 2.0f / amount);
        float x1;
        float y1;
        @SuppressWarnings("unused")
        int spawnCount = 0;

        VisibleObject visibleObject;
        List<VisibleObject> despawnList = new ArrayList<VisibleObject>();

        for (int i = 0; amount > i; i++) {
            templateId = idList.get((int) (Math.random() * idList.size()));
            x1 = (float) (Math.cos(interval * i) * radius);
            y1 = (float) (Math.sin(interval * i) * radius);
            spawn = SpawnEngine.addNewSpawn(worldId, templateId, x + x1, y + y1, z, heading, 0);

            if (spawn == null) {
                return;
            } else {
                visibleObject = SpawnEngine.spawnObject(spawn, 1);

                if (despawnTime > 0) {
                    despawnList.add(visibleObject);
                }

                spawnCount++;
            }
        }

        if (despawnTime > 0) {
            despawnThem(despawnList, despawnTime);
        }
    }

    public void startInvasion2() {
        int radius = 15;
        int amount = 25;
        int despawnTime = 3600;

        List<Integer> idList = new ArrayList<Integer>();
        idList.add(217425);
        idList.add(217451);
        idList.add(217470);
        idList.add(217423);
        idList.add(217457);
        idList.add(210561);
        idList.add(217450);

        float x = 522.5235f;
        float y = 863.517f;
        float z = 232.40518f;
        byte heading = 0;
        int worldId = 220040000;

        int templateId;
        SpawnTemplate spawn = null;

        float interval = (float) (Math.PI * 2.0f / amount);
        float x1;
        float y1;
        @SuppressWarnings("unused")
        int spawnCount = 0;

        VisibleObject visibleObject;
        List<VisibleObject> despawnList = new ArrayList<VisibleObject>();

        for (int i = 0; amount > i; i++) {
            templateId = idList.get((int) (Math.random() * idList.size()));
            x1 = (float) (Math.cos(interval * i) * radius);
            y1 = (float) (Math.sin(interval * i) * radius);
            spawn = SpawnEngine.addNewSpawn(worldId, templateId, x + x1, y + y1, z, heading, 0);

            if (spawn == null) {
                return;
            } else {
                visibleObject = SpawnEngine.spawnObject(spawn, 1);

                if (despawnTime > 0) {
                    despawnList.add(visibleObject);
                }

                spawnCount++;
            }
        }

        if (despawnTime > 0) {
            despawnThem(despawnList, despawnTime);
        }
    }

    public void startBossInvasion2() {
        int radius = 0;
        int amount = 1;
        int despawnTime = 3600;

        List<Integer> idList = new ArrayList<Integer>();
        idList.add(217472);//Stone

        float x = 522.5235f;
        float y = 863.517f;
        float z = 232.40518f;
        byte heading = 0;
        int worldId = 220040000;

        int templateId;
        SpawnTemplate spawn = null;

        float interval = (float) (Math.PI * 2.0f / amount);
        float x1;
        float y1;
        @SuppressWarnings("unused")
        int spawnCount = 0;

        VisibleObject visibleObject;
        List<VisibleObject> despawnList = new ArrayList<VisibleObject>();

        for (int i = 0; amount > i; i++) {
            templateId = idList.get((int) (Math.random() * idList.size()));
            x1 = (float) (Math.cos(interval * i) * radius);
            y1 = (float) (Math.sin(interval * i) * radius);
            spawn = SpawnEngine.addNewSpawn(worldId, templateId, x + x1, y + y1, z, heading, 0);

            if (spawn == null) {
                return;
            } else {
                visibleObject = SpawnEngine.spawnObject(spawn, 1);

                if (despawnTime > 0) {
                    despawnList.add(visibleObject);
                }

                spawnCount++;
            }
        }

        if (despawnTime > 0) {
            despawnThem(despawnList, despawnTime);
        }
    }

    public void startInvasion3() {
        int radius = 15;
        int amount = 25;
        int despawnTime = 3600;

        List<Integer> idList = new ArrayList<Integer>();
        idList.add(217425);
        idList.add(217451);
        idList.add(217470);
        idList.add(217423);
        idList.add(217457);
        idList.add(210561);
        idList.add(217450);

        float x = 1293.3086f;
        float y = 542.59247f;
        float z = 102.4120f;
        byte heading = 0;
        int worldId = 210040000;

        int templateId;
        SpawnTemplate spawn = null;

        float interval = (float) (Math.PI * 2.0f / amount);
        float x1;
        float y1;
        @SuppressWarnings("unused")
        int spawnCount = 0;

        VisibleObject visibleObject;
        List<VisibleObject> despawnList = new ArrayList<VisibleObject>();

        for (int i = 0; amount > i; i++) {
            templateId = idList.get((int) (Math.random() * idList.size()));
            x1 = (float) (Math.cos(interval * i) * radius);
            y1 = (float) (Math.sin(interval * i) * radius);
            spawn = SpawnEngine.addNewSpawn(worldId, templateId, x + x1, y + y1, z, heading, 0);

            if (spawn == null) {
                return;
            } else {
                visibleObject = SpawnEngine.spawnObject(spawn, 1);

                if (despawnTime > 0) {
                    despawnList.add(visibleObject);
                }

                spawnCount++;
            }
        }

        if (despawnTime > 0) {
            despawnThem(despawnList, despawnTime);
        }
    }

    public void startBossInvasion3() {
        int radius = 0;
        int amount = 1;
        int despawnTime = 3600;

        List<Integer> idList = new ArrayList<Integer>();
        idList.add(217472);//Stone

        float x = 1293.3086f;
        float y = 542.59247f;
        float z = 102.4120f;
        byte heading = 0;
        int worldId = 210040000;

        int templateId;
        SpawnTemplate spawn = null;

        float interval = (float) (Math.PI * 2.0f / amount);
        float x1;
        float y1;
        @SuppressWarnings("unused")
        int spawnCount = 0;

        VisibleObject visibleObject;
        List<VisibleObject> despawnList = new ArrayList<VisibleObject>();

        for (int i = 0; amount > i; i++) {
            templateId = idList.get((int) (Math.random() * idList.size()));
            x1 = (float) (Math.cos(interval * i) * radius);
            y1 = (float) (Math.sin(interval * i) * radius);
            spawn = SpawnEngine.addNewSpawn(worldId, templateId, x + x1, y + y1, z, heading, 0);

            if (spawn == null) {
                return;
            } else {
                visibleObject = SpawnEngine.spawnObject(spawn, 1);

                if (despawnTime > 0) {
                    despawnList.add(visibleObject);
                }

                spawnCount++;
            }
        }

        if (despawnTime > 0) {
            despawnThem(despawnList, despawnTime);
        }
    }

    public void startInvasion4() {
        int radius = 15;
        int amount = 25;
        int despawnTime = 3600;

        List<Integer> idList = new ArrayList<Integer>();
        idList.add(217425);
        idList.add(217451);
        idList.add(217470);
        idList.add(217423);
        idList.add(217457);
        idList.add(210561);
        idList.add(217450);

        float x = 1135.7701f;
        float y = 1253.5895f;
        float z = 224.5f;
        byte heading = 0;
        int worldId = 220040000;

        int templateId;
        SpawnTemplate spawn = null;

        float interval = (float) (Math.PI * 2.0f / amount);
        float x1;
        float y1;
        @SuppressWarnings("unused")
        int spawnCount = 0;

        VisibleObject visibleObject;
        List<VisibleObject> despawnList = new ArrayList<VisibleObject>();

        for (int i = 0; amount > i; i++) {
            templateId = idList.get((int) (Math.random() * idList.size()));
            x1 = (float) (Math.cos(interval * i) * radius);
            y1 = (float) (Math.sin(interval * i) * radius);
            spawn = SpawnEngine.addNewSpawn(worldId, templateId, x + x1, y + y1, z, heading, 0);

            if (spawn == null) {
                return;
            } else {
                visibleObject = SpawnEngine.spawnObject(spawn, 1);

                if (despawnTime > 0) {
                    despawnList.add(visibleObject);
                }

                spawnCount++;
            }
        }

        if (despawnTime > 0) {
            despawnThem(despawnList, despawnTime);
        }
    }

    public void startBossInvasion4() {
        int radius = 0;
        int amount = 1;
        int despawnTime = 3600;

        List<Integer> idList = new ArrayList<Integer>();
        idList.add(217472);//Stone

        float x = 1135.7701f;
        float y = 1253.5895f;
        float z = 224.5f;
        byte heading = 0;
        int worldId = 220040000;

        int templateId;
        SpawnTemplate spawn = null;

        float interval = (float) (Math.PI * 2.0f / amount);
        float x1;
        float y1;
        @SuppressWarnings("unused")
        int spawnCount = 0;

        VisibleObject visibleObject;
        List<VisibleObject> despawnList = new ArrayList<VisibleObject>();

        for (int i = 0; amount > i; i++) {
            templateId = idList.get((int) (Math.random() * idList.size()));
            x1 = (float) (Math.cos(interval * i) * radius);
            y1 = (float) (Math.sin(interval * i) * radius);
            spawn = SpawnEngine.addNewSpawn(worldId, templateId, x + x1, y + y1, z, heading, 0);

            if (spawn == null) {
                return;
            } else {
                visibleObject = SpawnEngine.spawnObject(spawn, 1);

                if (despawnTime > 0) {
                    despawnList.add(visibleObject);
                }

                spawnCount++;
            }
        }

        if (despawnTime > 0) {
            despawnThem(despawnList, despawnTime);
        }
    }

    private void despawnThem(final List<VisibleObject> despawnList, final int despawnTime) {
        ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                @SuppressWarnings("unused")
                int despawnCount = 0;
                for (VisibleObject visObj : despawnList) {
                    if (visObj != null && visObj.isSpawned()) {
                        visObj.getController().delete();
                        despawnCount++;
                    }
                }
                despawnThem2();
            }
        }, despawnTime * 1000);
    }

    public void despawnThem2() {
        if (remainingTime <= -60) {
            World.getInstance().doOnAllPlayers(new Visitor<Player>() {
                @Override
                public void visit(Player pl) {
                    PacketSendUtility.sendBrightYellowMessageOnCenter(pl, "Balaur invasion is now ended!");
                }
            });
        }
    }

    public void setTimer(int time) {
        remainingTime = time;
    }

    public int getTimer() {
        if (isStarted) {
            return remainingTime;
        } else {
            return -1;
        }
    }

    private Timer initTimer() {
        ActionListener action = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                remainingTime--;
                BalaurInvadeService.getInstance().sendRemainTime();
            }
        };

        return new Timer(60 * 1000, action);
    }

    public void sendRemainTime() {
        if (remainingTime == 3 || remainingTime == 2 || remainingTime == 1) {
            World.getInstance().doOnAllPlayers(new Visitor<Player>() {
                @Override
                public void visit(Player pl) {
                    PacketSendUtility.sendBrightYellowMessageOnCenter(pl, "Beware  ! Balaur will attack Elysea/Asmodae in " + remainingTime + " minute/s");
                }
            });
        }
    }

    public static BalaurInvadeService getInstance() {
        return SingletonHolder.dr;
    }

    private static class SingletonHolder {

        public static BalaurInvadeService dr = new BalaurInvadeService();
    }
}
