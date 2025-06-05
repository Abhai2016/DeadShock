package com.abhai.deadshock;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.nio.file.Paths;

public class Sounds {
    //Booker
    public static MediaPlayer bookerVoice = new MediaPlayer(new Media(Paths.get("resources", "sounds", "voice",
            "booker", "doctor_take_me_that.mp3").toUri().toString()));
    public static AudioClip doctorVoice = new AudioClip(Paths.get("resources", "sounds", "voice",
            "booker", "doctor_take_me_that.mp3").toUri().toString());
    public static AudioClip excellentVoice = new AudioClip(Paths.get("resources", "sounds", "voice",
            "booker", "excellent.mp3").toUri().toString());
    public static AudioClip audioClipFit = new AudioClip(Paths.get("resources", "sounds", "voice",
            "booker", "fit.mp3").toUri().toString());
    public static AudioClip audioClipEnemyHit = new AudioClip(Paths.get("resources", "sounds", "voice",
            "booker", "enemy_hit.mp3").toUri().toString());
    public static AudioClip audioClipEnemyHit2 = new AudioClip(Paths.get("resources", "sounds", "voice",
            "booker", "enemy_hit2.mp3").toUri().toString());
    public static AudioClip audioClipEnemyHit3 = new AudioClip(Paths.get("resources", "sounds", "voice",
            "booker", "enemy_hit3.mp3").toUri().toString());
    public static AudioClip feelBetterVoice = new AudioClip(Paths.get("resources", "sounds", "voice",
            "booker", "I_already_feel_better.mp3").toUri().toString());
    public static AudioClip audioClipLetsGo = new AudioClip(Paths.get("resources", "sounds", "voice",
            "booker", "let's_go_house_wait_us.mp3").toUri().toString());
    public static AudioClip audioClipEnergetic = new AudioClip(Paths.get("resources", "sounds", "voice",
            "booker", "energetic_hmm.mp3").toUri().toString());
    public static AudioClip audioClipNewEnergetic = new AudioClip(Paths.get("resources", "sounds", "voice",
            "booker", "new_energetic.mp3").toUri().toString());


    //Elizabeth
    public static MediaPlayer elizabethMediaPlayer;
    public static AudioClip audioClipAmmo = new AudioClip(Paths.get("resources", "sounds", "voice",
            "elizabeth", "ammo.mp3").toUri().toString());
    public static AudioClip audioClipAmmo2 = new AudioClip(Paths.get("resources", "sounds", "voice",
            "elizabeth", "ammo2.mp3").toUri().toString());
    public static AudioClip audioClipBookerCatch = new AudioClip(Paths.get("resources", "sounds", "voice",
            "elizabeth", "booker_catch.mp3").toUri().toString());
    public static AudioClip audioClipBookerCatch2 = new AudioClip(Paths.get("resources", "sounds", "voice",
            "elizabeth", "booker_catch2.mp3").toUri().toString());
    public static AudioClip audioClipBookerCatch3 = new AudioClip(Paths.get("resources", "sounds", "voice",
            "elizabeth", "booker_catch3.mp3").toUri().toString());
    public static AudioClip audioClipEmpty = new AudioClip(Paths.get("resources", "sounds", "voice",
            "elizabeth", "I_am_empty.mp3").toUri().toString());
    public static AudioClip audioClipAnything = new AudioClip(Paths.get("resources", "sounds", "voice",
            "elizabeth", "I_didn't_find_anything.mp3").toUri().toString());
    public static AudioClip audioClipAnymore = new AudioClip(Paths.get("resources", "sounds", "voice",
            "elizabeth", "I_haven't_anything_anymore.mp3").toUri().toString());
    public static AudioClip audioClipAnother = new AudioClip(Paths.get("resources", "sounds", "voice",
            "elizabeth", "I_would_try_find_another.mp3").toUri().toString());


    //Enemies
    public static AudioClip audioClipILostHim = new AudioClip(Paths.get("resources", "sounds", "voice",
            "enemies", "I_lost_him.mp3").toUri().toString());
    public static AudioClip audioClipILostHim2 = new AudioClip(Paths.get("resources", "sounds", "voice",
            "enemies", "I_lost_him2.mp3").toUri().toString());
    public static AudioClip audioClipWhereHeGoes = new AudioClip(Paths.get("resources", "sounds", "voice",
            "enemies", "where_he_goes.mp3").toUri().toString());
    public static AudioClip audioClipJustBeenHere = new AudioClip(Paths.get("resources", "sounds", "voice",
            "enemies", "just_been_here.mp3").toUri().toString());
    public static AudioClip audioClipHeIsGone = new AudioClip(Paths.get("resources", "sounds", "voice",
            "enemies", "he_is_gone.mp3").toUri().toString());
    public static AudioClip audioClipHeHasGone = new AudioClip(Paths.get("resources", "sounds", "voice",
            "enemies", "he_has_gone.mp3").toUri().toString());

    public static AudioClip audioClipDontGoAway = new AudioClip(Paths.get("resources", "sounds", "voice",
            "enemies", "don't_go_away.mp3").toUri().toString());
    public static AudioClip audioClipFire = new AudioClip(Paths.get("resources", "sounds", "voice",
            "enemies", "fire.mp3").toUri().toString());
    public static AudioClip audioClipHeAlreadyHere = new AudioClip(Paths.get("resources", "sounds", "voice",
            "enemies", "he_already_here.mp3").toUri().toString());
    public static AudioClip audioClipThrowWeapon = new AudioClip(Paths.get("resources", "sounds", "voice",
            "enemies", "throw_weapon_out.mp3").toUri().toString());
    public static AudioClip audioClipYouDieHere = new AudioClip(Paths.get("resources", "sounds", "voice",
            "enemies", "you_die_here.mp3").toUri().toString());
    public static AudioClip audioClipInFight = new AudioClip(Paths.get("resources", "sounds", "voice",
            "enemies", "in_fight.mp3").toUri().toString());
    public static AudioClip audioClipTheyHere = new AudioClip(Paths.get("resources", "sounds", "voice",
            "enemies", "I_knew_that_they_here.mp3").toUri().toString());
    public static AudioClip audioClipTakeThem = new AudioClip(Paths.get("resources", "sounds", "voice",
            "enemies", "take_them.mp3").toUri().toString());

    public static AudioClip audioClipCamper = new AudioClip(Paths.get("resources", "sounds", "voice",
            "enemies", "camper.wav").toUri().toString());
    public static AudioClip audioClipDie = new AudioClip(Paths.get("resources", "sounds", "voice",
            "enemies", "die.mp3").toUri().toString());
    public static AudioClip audioClipDie2 = new AudioClip(Paths.get("resources", "sounds", "voice",
            "enemies", "die2.mp3").toUri().toString());
    public static AudioClip audioClipHit = new AudioClip(Paths.get("resources", "sounds", "voice",
            "enemies", "hit.mp3").toUri().toString());
    public static AudioClip audioClipHit2 = new AudioClip(Paths.get("resources", "sounds", "voice",
            "enemies", "hit2.mp3").toUri().toString());
    public static AudioClip audioClipHit3 = new AudioClip(Paths.get("resources", "sounds", "voice",
            "enemies", "hit3.mp3").toUri().toString());

    public static AudioClip enemyVoice = new AudioClip(Paths.get("resources", "sounds", "voice",
            "enemies", "can_you_shoot.mp3").toUri().toString());
    public static AudioClip enemyVoice2 = new AudioClip(Paths.get("resources", "sounds", "voice",
            "enemies", "die_already.mp3").toUri().toString());
    public static AudioClip enemyVoice3 = new AudioClip(Paths.get("resources", "sounds", "voice",
            "enemies", "don't_care_about_bullets.mp3").toUri().toString());
    public static AudioClip enemyVoice4 = new AudioClip(Paths.get("resources", "sounds", "voice",
            "enemies", "keep_shooting.mp3").toUri().toString());
    public static AudioClip enemyVoice5 = new AudioClip(Paths.get("resources", "sounds", "voice",
            "enemies", "kill_me_if_you_can.mp3").toUri().toString());
    public static AudioClip enemyVoice6 = new AudioClip(Paths.get("resources", "sounds", "voice",
            "enemies", "that_all_you_can.mp3").toUri().toString());
    public static AudioClip enemyVoice7 = new AudioClip(Paths.get("resources", "sounds", "voice",
            "enemies", "who_are_you.mp3").toUri().toString());
    public static AudioClip enemyVoice8 = new AudioClip(Paths.get("resources", "sounds", "voice",
            "enemies", "you_a_stupid.mp3").toUri().toString());
    public static AudioClip enemyVoice9 = new AudioClip(Paths.get("resources", "sounds", "voice",
            "enemies", "take_him_bullets.mp3").toUri().toString());
    public static AudioClip enemyVoice10 = new AudioClip(Paths.get("resources", "sounds", "voice",
            "enemies", "keep_fire.mp3").toUri().toString());

    public static AudioClip audioClipIEmpty = new AudioClip(Paths.get("resources", "sounds", "voice",
            "enemies", "I_empty.mp3").toUri().toString());
    public static AudioClip audioClipINeedClip = new AudioClip(Paths.get("resources", "sounds", "voice",
            "enemies", "I_need_clip.mp3").toUri().toString());
    public static AudioClip audioClipINeedClip2 = new AudioClip(Paths.get("resources", "sounds", "voice",
            "enemies", "I_need_clip2.mp3").toUri().toString());
    public static AudioClip audioClipIReloading = new AudioClip(Paths.get("resources", "sounds", "voice",
            "enemies", "I_am_reloading.mp3").toUri().toString());
    public static AudioClip audioClipAmmoRunOut = new AudioClip(Paths.get("resources", "sounds", "voice",
            "enemies", "ammo_run_out.mp3").toUri().toString());


    //fx
    public static AudioClip pistolShoot = new AudioClip(Paths.get("resources", "sounds", "fx",
            "weapon", "pistol_shoot.mp3").toUri().toString());
    public static AudioClip machineGunShoot = new AudioClip(Paths.get("resources", "sounds", "fx",
            "weapon", "machine_gun_shoot.mp3").toUri().toString());
    public static MediaPlayer pistolReload = new MediaPlayer(new Media(Paths.get("resources", "sounds", "fx",
            "weapon", "pistol_reload.mp3").toUri().toString()));
    public static MediaPlayer machineGunReload = new MediaPlayer(new Media(Paths.get("resources", "sounds", "fx",
            "weapon", "machine_gun_reload.mp3").toUri().toString()));
    public static MediaPlayer rpgShootAndReload = new MediaPlayer(new Media(Paths.get("resources", "sounds", "fx",
            "weapon", "rpgShootAndReload.mp3").toUri().toString()));
    public static AudioClip rpgExplosion = new AudioClip(Paths.get("resources", "sounds", "fx",
            "weapon", "rpgExplosion.mp3").toUri().toString());

    public static AudioClip audioClipFight = new AudioClip(Paths.get("resources", "sounds", "fx",
            "infighting.mp3").toUri().toString());

    static AudioClip audioClipOpenMenu = new AudioClip(Paths.get("resources", "sounds", "fx",
            "vendingMachine", "openMenu.mp3").toUri().toString());
    static AudioClip audioClipChangeItem = new AudioClip(Paths.get("resources", "sounds", "fx",
            "vendingMachine", "changeItem.mp3").toUri().toString());
    static AudioClip audioClipPurchase = new AudioClip(Paths.get("resources", "sounds", "fx",
            "vendingMachine", "purchase.mp3").toUri().toString());

    public static AudioClip devilKissShoot = new AudioClip(Paths.get("resources", "sounds", "fx",
            "energetics", "fireBall.mp3").toUri().toString());
    public static AudioClip killByLightning = new AudioClip(Paths.get("resources", "sounds", "fx",
            "energetics", "shock_die.mp3").toUri().toString());
    public static AudioClip hypnotist = new AudioClip(Paths.get("resources", "sounds", "fx",
            "energetics", "hypnotist.mp3").toUri().toString());
    public static AudioClip changeOnDevilKiss = new AudioClip(Paths.get("resources", "sounds", "fx",
            "energetics", "changeOnDevilKiss.mp3").toUri().toString());
    public static AudioClip changeOnElectricity = new AudioClip(Paths.get("resources", "sounds", "fx",
            "energetics", "changeOnElectricity.mp3").toUri().toString());
    public static AudioClip changeOnHypnotist = new AudioClip(Paths.get("resources", "sounds", "fx",
            "energetics", "changeOnHypnotist.mp3").toUri().toString());

    //boss
    public static AudioClip bossTromp = new AudioClip(Paths.get("resources", "sounds", "fx",
            "boss", "tromp.mp3").toUri().toString());
    public static AudioClip bossHit = new AudioClip(Paths.get("resources", "sounds", "fx",
            "boss", "hit.mp3").toUri().toString());
    public static AudioClip bossHit2 = new AudioClip(Paths.get("resources", "sounds", "fx",
            "boss", "hit2.mp3").toUri().toString());
    public static AudioClip bossHit3 = new AudioClip(Paths.get("resources", "sounds", "fx",
            "boss", "hit3.mp3").toUri().toString());
}
