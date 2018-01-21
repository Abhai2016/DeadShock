package game;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

public class Sounds {
    //Booker
    public static MediaPlayer bookerVoice = new MediaPlayer(new Media(
            new File("file:/../sounds/voice/booker/doctor_take_me_that.mp3").toURI().toString()));
    public static AudioClip doctorVoice = new AudioClip(new File("file:/../sounds/voice/booker/doctor_take_me_that.mp3").toURI().toString());
    public static AudioClip excellentVoice = new AudioClip(new File("file:/../sounds/voice/booker/excellent.mp3").toURI().toString());
    public static AudioClip audioClipFit = new AudioClip(new File("file:/../sounds/voice/booker/fit.mp3").toURI().toString());
    public static AudioClip audioClipEnemyHit = new AudioClip(new File("file:/../sounds/voice/booker/enemy_hit.mp3").toURI().toString());
    public static AudioClip audioClipEnemyHit2 = new AudioClip(new File("file:/../sounds/voice/booker/enemy_hit2.mp3").toURI().toString());
    public static AudioClip audioClipEnemyHit3 = new AudioClip(new File("file:/../sounds/voice/booker/enemy_hit3.mp3").toURI().toString());
    public static AudioClip feelBetterVoice = new AudioClip(
            new File("file:/../sounds/voice/booker/I_already_feel_better.mp3").toURI().toString());
    public static AudioClip audioClipLetsGo = new AudioClip(
            new File("file:/../sounds/voice/booker/let's_go_house_wait_us.mp3").toURI().toString());
    public static AudioClip audioClipEnergetic = new AudioClip(new File("file:/../sounds/voice/booker/energetic_hmm.mp3").toURI().toString());
    public static AudioClip audioClipNewEnergetic = new AudioClip(
            new File("file:/../sounds/voice/booker/new_energetic.mp3").toURI().toString());


    //Elizabeth
    public static MediaPlayer elizabethMediaPlayer;
    public static AudioClip audioClipAmmo = new AudioClip(new File("file:/../sounds/voice/elizabeth/ammo.mp3").toURI().toString());
    public static AudioClip audioClipAmmo2 = new AudioClip(new File("file:/../sounds/voice/elizabeth/ammo2.mp3").toURI().toString());
    public static AudioClip audioClipBookerCatch = new AudioClip(
            new File("file:/../sounds/voice/elizabeth/booker_catch.mp3").toURI().toString());
    public static AudioClip audioClipBookerCatch2 = new AudioClip(
            new File("file:/../sounds/voice/elizabeth/booker_catch2.mp3").toURI().toString());
    public static AudioClip audioClipBookerCatch3 = new AudioClip(
            new File("file:/../sounds/voice/elizabeth/booker_catch3.mp3").toURI().toString());
    public static AudioClip audioClipEmpty = new AudioClip(new File("file:/../sounds/voice/elizabeth/I_am_empty.mp3").toURI().toString());
    public static AudioClip audioClipAnything = new AudioClip(
            new File("file:/../sounds/voice/elizabeth/I_didn't_find_anything.mp3").toURI().toString());
    public static AudioClip audioClipAnymore = new AudioClip(
            new File("file:/../sounds/voice/elizabeth/I_haven't_anything_anymore.mp3").toURI().toString());
    public static AudioClip audioClipAnother = new AudioClip(
            new File("file:/../sounds/voice/elizabeth/I_would_try_find_another.mp3").toURI().toString());


    //Enemies
    public static AudioClip audioClipILostHim = new AudioClip(new File("file:/../sounds/voice/enemies/I_lost_him.mp3").toURI().toString());
    public static AudioClip audioClipILostHim2 = new AudioClip(new File("file:/../sounds/voice/enemies/I_lost_him2.mp3").toURI().toString());
    public static AudioClip audioClipWhereHeGoes = new AudioClip(new File("file:/../sounds/voice/enemies/where_he_goes.mp3").toURI().toString());
    public static AudioClip audioClipJustBeenHere = new AudioClip(
            new File("file:/../sounds/voice/enemies/just_been_here.mp3").toURI().toString());
    public static AudioClip audioClipHeIsGone = new AudioClip(new File("file:/../sounds/voice/enemies/he_is_gone.mp3").toURI().toString());
    public static AudioClip audioClipHeHasGone = new AudioClip(new File("file:/../sounds/voice/enemies/he_has_gone.mp3").toURI().toString());

    public static AudioClip audioClipDontGoAway = new AudioClip(
            new File("file:/../sounds/voice/enemies/don't_go_away.mp3").toURI().toString());
    public static AudioClip audioClipFire = new AudioClip(new File("file:/../sounds/voice/enemies/fire.mp3").toURI().toString());
    public static AudioClip audioClipHeAlreadyHere = new AudioClip(
            new File("file:/../sounds/voice/enemies/he_already_here.mp3").toURI().toString());
    public static AudioClip audioClipThrowWeapon = new AudioClip(
            new File("file:/../sounds/voice/enemies/throw_weapon_out.mp3").toURI().toString());
    public static AudioClip audioClipYouDieHere = new AudioClip(new File("file:/../sounds/voice/enemies/you_die_here.mp3").toURI().toString());
    public static AudioClip audioClipInFight = new AudioClip(new File("file:/../sounds/voice/enemies/in_fight.mp3").toURI().toString());
    public static AudioClip audioClipTheyHere = new AudioClip(
            new File("file:/../sounds/voice/enemies/I_knew_that_they_here.mp3").toURI().toString());
    public static AudioClip audioClipTakeThem = new AudioClip(new File("file:/../sounds/voice/enemies/take_them.mp3").toURI().toString());

    public static AudioClip audioClipCamper = new AudioClip(new File("file:/../sounds/voice/enemies/camper.wav").toURI().toString());
    public static AudioClip audioClipDie = new AudioClip(new File("file:/../sounds/voice/enemies/die.mp3").toURI().toString());
    public static AudioClip audioClipDie2 = new AudioClip(new File("file:/../sounds/voice/enemies/die2.mp3").toURI().toString());
    public static AudioClip audioClipHit = new AudioClip(new File("file:/../sounds/voice/enemies/hit.mp3").toURI().toString());
    public static AudioClip audioClipHit2 = new AudioClip(new File("file:/../sounds/voice/enemies/hit2.mp3").toURI().toString());
    public static AudioClip audioClipHit3 = new AudioClip(new File("file:/../sounds/voice/enemies/hit3.mp3").toURI().toString());

    public static AudioClip enemyVoice = new AudioClip(new File("file:/../sounds/voice/enemies/can_you_shoot.mp3").toURI().toString());
    public static AudioClip enemyVoice2 = new AudioClip(new File("file:/../sounds/voice/enemies/die_already.mp3").toURI().toString());
    public static AudioClip enemyVoice3 = new AudioClip(
            new File("file:/../sounds/voice/enemies/don't_care_about_bullets.mp3").toURI().toString());
    public static AudioClip enemyVoice4 = new AudioClip(new File("file:/../sounds/voice/enemies/keep_shooting.mp3").toURI().toString());
    public static AudioClip enemyVoice5 = new AudioClip(new File("file:/../sounds/voice/enemies/kill_me_if_you_can.mp3").toURI().toString());
    public static AudioClip enemyVoice6 = new AudioClip(new File("file:/../sounds/voice/enemies/that_all_you_can.mp3").toURI().toString());
    public static AudioClip enemyVoice7 = new AudioClip(new File("file:/../sounds/voice/enemies/who_are_you.mp3").toURI().toString());
    public static AudioClip enemyVoice8 = new AudioClip(new File("file:/../sounds/voice/enemies/you_a_stupid.mp3").toURI().toString());
    public static AudioClip enemyVoice9 = new AudioClip(new File("file:/../sounds/voice/enemies/take_him_bullets.mp3").toURI().toString());
    public static AudioClip enemyVoice10 = new AudioClip(new File("file:/../sounds/voice/enemies/keep_fire.mp3").toURI().toString());

    public static AudioClip audioClipIEmpty = new AudioClip(new File("file:/../sounds/voice/enemies/I_empty.mp3").toURI().toString());
    public static AudioClip audioClipINeedClip = new AudioClip(new File("file:/../sounds/voice/enemies/I_need_clip.mp3").toURI().toString());
    public static AudioClip audioClipINeedClip2 = new AudioClip(new File("file:/../sounds/voice/enemies/I_need_clip2.mp3").toURI().toString());
    public static AudioClip audioClipIReloading = new AudioClip(new File("file:/../sounds/voice/enemies/I_am_reloading.mp3").toURI().toString());
    public static AudioClip audioClipAmmoRunOut = new AudioClip(new File("file:/../sounds/voice/enemies/ammo_run_out.mp3").toURI().toString());


    //fx
    public static AudioClip pistolShoot = new AudioClip(new File("file:/../sounds/fx/weapon/pistol_shoot.mp3").toURI().toString());
    public static AudioClip machineGunShoot = new AudioClip(new File("file:/../sounds/fx/weapon/machine_gun_shoot.mp3").toURI().toString());
    public static MediaPlayer pistolReload = new MediaPlayer(new Media(
            new File("file:/../sounds/fx/weapon/pistol_reload.mp3").toURI().toString()));
    public static MediaPlayer machineGunReload = new MediaPlayer(new Media(
            new File("file:/../sounds/fx/weapon/machine_gun_reload.mp3").toURI().toString()));
    public static MediaPlayer rpgShootAndReload = new MediaPlayer(new Media(
            new File("file:/../sounds/fx/weapon/rpgShootAndReload.mp3").toURI().toString()));
    public static AudioClip rpgExplosion = new AudioClip(new File("file:/../sounds/fx/weapon/rpgExplosion.mp3").toURI().toString());

    public static AudioClip audioClipFight = new AudioClip(new File("file:/../sounds/fx/infighting.mp3").toURI().toString());

    static AudioClip audioClipOpenMenu = new AudioClip(new File("file:/../sounds/fx/vendingMachine/openMenu.mp3").toURI().toString());
    static AudioClip audioClipChangeItem = new AudioClip(new File("file:/../sounds/fx/vendingMachine/changeItem.mp3").toURI().toString());
    static AudioClip audioClipPurchase = new AudioClip(new File("file:/../sounds/fx/vendingMachine/purchase.mp3").toURI().toString());

    public static AudioClip devilKissShoot = new AudioClip(new File("file:/../sounds/fx/energetics/fireBall.mp3").toURI().toString());
    public static AudioClip killByLightning = new AudioClip(new File("file:/../sounds/fx/energetics/shock_die.mp3").toURI().toString());
    public static AudioClip hypnotist = new AudioClip(new File("file:/../sounds/fx/energetics/hypnotist.mp3").toURI().toString());
    public static AudioClip changeOnDevilKiss = new AudioClip(
            new File("file:/../sounds/fx/energetics/changeOnDevilKiss.mp3").toURI().toString());
    public static AudioClip changeOnElectricity = new AudioClip(
            new File("file:/../sounds/fx/energetics/changeOnElectricity.mp3").toURI().toString());
    public static AudioClip changeOnHypnotist = new AudioClip(
            new File("file:/../sounds/fx/energetics/changeOnHypnotist.mp3").toURI().toString());

    //boss
    public static AudioClip bossTromp = new AudioClip(new File("file:/../sounds/fx/boss/tromp.mp3").toURI().toString());
    public static AudioClip bossHit = new AudioClip(new File("file:/../sounds/fx/boss/hit.mp3").toURI().toString());
    public static AudioClip bossHit2 = new AudioClip(new File("file:/../sounds/fx/boss/hit2.mp3").toURI().toString());
    public static AudioClip bossHit3 = new AudioClip(new File("file:/../sounds/fx/boss/hit3.mp3").toURI().toString());
}
