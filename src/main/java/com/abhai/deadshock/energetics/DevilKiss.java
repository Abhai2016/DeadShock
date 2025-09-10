package com.abhai.deadshock.energetics;

import com.abhai.deadshock.Game;
import com.abhai.deadshock.utils.GameMedia;
import com.abhai.deadshock.utils.pools.ObjectPool;

import java.util.ArrayList;

public class DevilKiss {
    private final ArrayList<DevilKissShot> devilKissShots;
    private final ObjectPool<DevilKissShot> devilKissShotObjectPool;

    public DevilKiss() {
        devilKissShots = new ArrayList<>();
        devilKissShotObjectPool = new ObjectPool<>(DevilKissShot::new, 10, 20);
    }

    public void shoot() {
        GameMedia.DEVIL_KISS_SHOT.play(Game.getGameWorld().getMenu().getFxSlider().getValue() / 100);
        DevilKissShot devilKissShot = devilKissShotObjectPool.get();
        devilKissShot.init(Game.getGameWorld().getBooker().getTranslateX(), Game.getGameWorld().getBooker().getTranslateY() + 5);
        devilKissShots.add(devilKissShot);
    }

    public void delete() {
        if (!devilKissShots.isEmpty()) {
            for (DevilKissShot devilKissShot : devilKissShots)
                devilKissShotObjectPool.put(devilKissShot);
            Game.getGameWorld().getGameRoot().getChildren().removeAll(devilKissShots);
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
                    Game.getGameWorld().getGameRoot().getChildren().remove(devilKissShot);
                    return;
                }
            }
    }
}
