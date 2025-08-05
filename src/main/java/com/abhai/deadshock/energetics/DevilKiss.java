package com.abhai.deadshock.energetics;

import com.abhai.deadshock.Game;
import com.abhai.deadshock.utils.ObjectPool;
import com.abhai.deadshock.utils.Sounds;

import java.util.ArrayList;

public class DevilKiss {
    private final ArrayList<DevilKissShot> devilKissShots;
    private final ObjectPool<DevilKissShot> devilKissShotObjectPool;

    public DevilKiss() {
        devilKissShots = new ArrayList<>();
        devilKissShotObjectPool = new ObjectPool<>(DevilKissShot::new, 10, 20);
    }

    public void shoot() {
        Sounds.devilKissShot.play(Game.menu.fxSlider.getValue() / 100);
        DevilKissShot devilKissShot = devilKissShotObjectPool.get();
        devilKissShot.init(Game.booker.getTranslateX(), Game.booker.getTranslateY() + 5);
        devilKissShots.add(devilKissShot);
    }

    public void delete() {
        if (!devilKissShots.isEmpty()) {
            for (DevilKissShot devilKissShot : devilKissShots)
                devilKissShotObjectPool.put(devilKissShot);
            devilKissShots.clear();
        }
    }

    public void update() {
        if (!devilKissShots.isEmpty())
            for (DevilKissShot devilKissShot : devilKissShots) {
                devilKissShot.update();
                if (devilKissShot.isToDelete()) {
                    devilKissShots.remove(devilKissShot);
                    devilKissShotObjectPool.put(devilKissShot);
                    Game.gameRoot.getChildren().remove(devilKissShot);
                    return;
                }
            }
    }
}
