package com.abhai.deadshock.utils;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.nio.file.Paths;

public class GameMedia {
    //Booker
    public static final Media deathMedia = new Media(Paths.get("resources", "videos", "death.mp4").toUri().toString());
    public static final AudioClip FUCK = new AudioClip(Paths.get("resources", "sounds", "voices", "booker", "fuck.mp3").toUri().toString());
    public static final AudioClip SHIT = new AudioClip(Paths.get("resources", "sounds", "voices", "booker", "shit.mp3").toUri().toString());
    public static final AudioClip GREAT = new AudioClip(Paths.get("resources", "sounds", "voices", "booker", "great.mp3").toUri().toString());
    public static final AudioClip BOOKER_HIT = new AudioClip(Paths.get("resources", "sounds", "voices", "booker", "hit.mp3").toUri().toString());
    public static final AudioClip LETS_GO = new AudioClip(Paths.get("resources", "sounds", "voices", "booker", "letsGo.mp3").toUri().toString());
    public static final AudioClip CRETINS = new AudioClip(Paths.get("resources", "sounds", "voices", "booker", "cretins.mp3").toUri().toString());
    public static final AudioClip BOOKER_HIT_2 = new AudioClip(Paths.get("resources", "sounds", "voices", "booker", "hit2.mp3").toUri().toString());
    public static final AudioClip BOOKER_HIT_3 = new AudioClip(Paths.get("resources", "sounds", "voices", "booker", "hit3.mp3").toUri().toString());
    public static final AudioClip WILL_WORK = new AudioClip(Paths.get("resources", "sounds", "voices", "booker", "willWork.mp3").toUri().toString());
    public static final AudioClip ENERGETIC = new AudioClip(Paths.get("resources", "sounds", "voices", "booker", "energetic.mp3").toUri().toString());
    public static final AudioClip FEELS_BETTER = new AudioClip(Paths.get("resources", "sounds", "voices", "booker", "feelsBetter.mp3").toUri().toString());
    public static final AudioClip NEW_ENERGETIC = new AudioClip(Paths.get("resources", "sounds", "voices", "booker", "newEnergetic.mp3").toUri().toString());
    public static final AudioClip FEELING_BETTER = new AudioClip(Paths.get("resources", "sounds", "voices", "booker", "feelingBetter.mp3").toUri().toString());

    //Elizabeth
    public static final AudioClip FREEDOM = new AudioClip(Paths.get("resources", "sounds", "voices", "elizabeth", "freedom.mp3").toUri().toString());
    public static final AudioClip BOOKER_CATCH = new AudioClip(Paths.get("resources", "sounds", "voices", "elizabeth", "bookerCatch.mp3").toUri().toString());
    public static final AudioClip BOOKER_CATCH_2 = new AudioClip(Paths.get("resources", "sounds", "voices", "elizabeth", "bookerCatch2.mp3").toUri().toString());
    public static final AudioClip BOOKER_CATCH_3 = new AudioClip(Paths.get("resources", "sounds", "voices", "elizabeth", "bookerCatch3.mp3").toUri().toString());
    public static final MediaPlayer OH_BOOKER = new MediaPlayer(new Media(Paths.get("resources", "sounds", "voices", "elizabeth", "booker.mp3").toUri().toString()));

    //Enemies
    public static final AudioClip DIE = new AudioClip(Paths.get("resources", "sounds", "voices", "enemies", "die.mp3").toUri().toString());
    public static final AudioClip DEATH = new AudioClip(Paths.get("resources", "sounds", "voices", "enemies", "death.mp3").toUri().toString());
    public static final AudioClip STUPID = new AudioClip(Paths.get("resources", "sounds", "voices", "enemies", "stupid.mp3").toUri().toString());
    public static final AudioClip ATTACK = new AudioClip(Paths.get("resources", "sounds", "voices", "enemies", "attack.mp3").toUri().toString());
    public static final AudioClip DEATH_2 = new AudioClip(Paths.get("resources", "sounds", "voices", "enemies", "death2.mp3").toUri().toString());
    public static final AudioClip KILL_ME = new AudioClip(Paths.get("resources", "sounds", "voices", "enemies", "killMe.mp3").toUri().toString());
    public static final AudioClip NO_AMMO = new AudioClip(Paths.get("resources", "sounds", "voices", "enemies", "noAmmo.mp3").toUri().toString());
    public static final AudioClip LOST_HIM = new AudioClip(Paths.get("resources", "sounds", "voices", "enemies", "lostHim.mp3").toUri().toString());
    public static final AudioClip NO_AMMO_2 = new AudioClip(Paths.get("resources", "sounds", "voices", "enemies", "noAmmo2.mp3").toUri().toString());
    public static final AudioClip AUDIO_CLIP_HIT = new AudioClip(Paths.get("resources", "sounds", "voices", "enemies", "hit.mp3").toUri().toString());
    public static final AudioClip NEED_AMMO = new AudioClip(Paths.get("resources", "sounds", "voices", "enemies", "needAmmo.mp3").toUri().toString());
    public static final AudioClip TAKE_THEM = new AudioClip(Paths.get("resources", "sounds", "voices", "enemies", "takeThem.mp3").toUri().toString());
    public static final AudioClip WERE_HERE = new AudioClip(Paths.get("resources", "sounds", "voices", "enemies", "wereHere.mp3").toUri().toString());
    public static final AudioClip HE_IS_HERE = new AudioClip(Paths.get("resources", "sounds", "voices", "enemies", "heIsHere.mp3").toUri().toString());
    public static final AudioClip LOST_HIM_2 = new AudioClip(Paths.get("resources", "sounds", "voices", "enemies", "lostHim2.mp3").toUri().toString());
    public static final AudioClip RELOADING = new AudioClip(Paths.get("resources", "sounds", "voices", "enemies", "reloading.mp3").toUri().toString());
    public static final AudioClip AUDIO_CLIP_FIRE = new AudioClip(Paths.get("resources", "sounds", "voices", "enemies", "fire.mp3").toUri().toString());
    public static final AudioClip AUDIO_CLIP_HIT_3 = new AudioClip(Paths.get("resources", "sounds", "voices", "enemies", "hit3.mp3").toUri().toString());
    public static final AudioClip AUDIO_CLIP_HIT_2 = new AudioClip(Paths.get("resources", "sounds", "voices", "enemies", "hit2.mp3").toUri().toString());
    public static final AudioClip ALL_YOU_CAN = new AudioClip(Paths.get("resources", "sounds", "voices", "enemies", "allYouCan.mp3").toUri().toString());
    public static final AudioClip NEED_AMMO_2 = new AudioClip(Paths.get("resources", "sounds", "voices", "enemies", "needAmmo2.mp3").toUri().toString());
    public static final AudioClip WHO_ARE_YOU = new AudioClip(Paths.get("resources", "sounds", "voices", "enemies", "whoAreYou.mp3").toUri().toString());
    public static final AudioClip HE_HAS_GONE = new AudioClip(Paths.get("resources", "sounds", "voices", "enemies", "heHasGone.mp3").toUri().toString());
    public static final AudioClip DIE_ALREADY = new AudioClip(Paths.get("resources", "sounds", "voices", "enemies", "dieAlready.mp3").toUri().toString());
    public static final AudioClip WONT_GO_AWAY = new AudioClip(Paths.get("resources", "sounds", "voices", "enemies", "wontGoAway.mp3").toUri().toString());
    public static final AudioClip HE_HAS_GONE_3 = new AudioClip(Paths.get("resources", "sounds", "voices", "enemies", "heHasGone3.mp3").toUri().toString());
    public static final AudioClip HE_HAS_GONE_2 = new AudioClip(Paths.get("resources", "sounds", "voices", "enemies", "heHasGone2.mp3").toUri().toString());
    public static final AudioClip AUDIO_CLIP_CAMPER = new AudioClip(Paths.get("resources", "sounds", "voices", "enemies", "camper.wav").toUri().toString());
    public static final AudioClip THEY_ARE_HERE = new AudioClip(Paths.get("resources", "sounds", "voices", "enemies", "theyAreHere.mp3").toUri().toString());
    public static final AudioClip CAN_YOU_SHOOT = new AudioClip(Paths.get("resources", "sounds", "voices", "enemies", "canYouShoot.mp3").toUri().toString());
    public static final AudioClip KEEP_SHOOTING = new AudioClip(Paths.get("resources", "sounds", "voices", "enemies", "keepShooting.mp3").toUri().toString());
    public static final AudioClip GET_DOWN_WEAPON = new AudioClip(Paths.get("resources", "sounds", "voices", "enemies", "getDownWeapon.mp3").toUri().toString());
    public static final AudioClip KEEP_SHOOTING_2 = new AudioClip(Paths.get("resources", "sounds", "voices", "enemies", "keepShooting2.mp3").toUri().toString());
    public static final AudioClip GIVE_HIM_BULLETS = new AudioClip(Paths.get("resources", "sounds", "voices", "enemies", "giveHimBullets.mp3").toUri().toString());
    public static final AudioClip DONT_SPARE_BULLETS = new AudioClip(Paths.get("resources", "sounds", "voices", "enemies", "dontSpareBullets.mp3").toUri().toString());

    //fx
    public static final Media RPG_RELOAD = new Media(Paths.get("resources", "sounds", "fx", "weapons", "rpgReload.mp3").toUri().toString());
    public static final Media PISTOL_RELOAD = new Media(Paths.get("resources", "sounds", "fx", "weapons", "pistolReload.mp3").toUri().toString());
    public static final AudioClip AUDIO_CLIP = new AudioClip(Paths.get("resources", "sounds", "fx", "energetics", "hypnosis.mp3").toUri().toString());
    public static final AudioClip PISTOL_SHOT = new AudioClip(Paths.get("resources", "sounds", "fx", "weapons", "pistolShot.mp3").toUri().toString());
    public static final AudioClip ELECTRICITY = new AudioClip(Paths.get("resources", "sounds", "fx", "energetics", "electricity.mp3").toUri().toString());
    public static final AudioClip RPG_EXPLOSION = new AudioClip(Paths.get("resources", "sounds", "fx", "weapons", "rpgExplosion.mp3").toUri().toString());
    public static final Media MACHINE_GUN_RELOAD = new Media(Paths.get("resources", "sounds", "fx", "weapons", "machineGunReload.mp3").toUri().toString());
    public static final AudioClip CLOSE_COMBAT = new AudioClip(Paths.get("resources", "sounds", "voices", "booker", "closeCombat.mp3").toUri().toString());
    public static final Media RPG_SHOT_WITH_RELOAD = new Media(Paths.get("resources", "sounds", "fx", "weapons", "rpgShotWithReload.mp3").toUri().toString());
    public static final AudioClip MACHINE_GUN_SHOT = new AudioClip(Paths.get("resources", "sounds", "fx", "weapons", "machineGunShot.mp3").toUri().toString());
    public static final AudioClip DEVIL_KISS_SHOT = new AudioClip(Paths.get("resources", "sounds", "fx", "energetics", "devilKissShot.mp3").toUri().toString());
    public static final AudioClip AUDIO_CLIP_PURCHASE = new AudioClip(Paths.get("resources", "sounds", "fx", "vendingMachine", "purchase.mp3").toUri().toString());
    public static final AudioClip AUDIO_CLIP_OPEN_MENU = new AudioClip(Paths.get("resources", "sounds", "fx", "vendingMachine", "openMenu.mp3").toUri().toString());
    public static final AudioClip ELECTRICITY_DEATH = new AudioClip(Paths.get("resources", "sounds", "fx", "energetics", "electricityDeath.mp3").toUri().toString());
    public static final AudioClip CHANGE_TO_HYPNOSIS = new AudioClip(Paths.get("resources", "sounds", "fx", "energetics", "changeToHypnosis.mp3").toUri().toString());
    public static final AudioClip AUDIO_CLIP_CHANGE_ITEM = new AudioClip(Paths.get("resources", "sounds", "fx", "vendingMachine", "changeItem.mp3").toUri().toString());
    public static final AudioClip CHANGE_TO_DEVIL_KISS = new AudioClip(Paths.get("resources", "sounds", "fx", "energetics", "changeToDevilKiss.mp3").toUri().toString());
    public static final AudioClip CHANGE_TO_ELECTRICITY = new AudioClip(Paths.get("resources", "sounds", "fx", "energetics", "changeToElectricity.mp3").toUri().toString());

    //boss
    public static final AudioClip BOSS_HIT = new AudioClip(Paths.get("resources", "sounds", "fx", "boss", "hit.mp3").toUri().toString());
    public static final AudioClip BOSS_HIT_2 = new AudioClip(Paths.get("resources", "sounds", "fx", "boss", "hit2.mp3").toUri().toString());
    public static final AudioClip BOSS_HIT_3 = new AudioClip(Paths.get("resources", "sounds", "fx", "boss", "hit3.mp3").toUri().toString());
    public static final AudioClip BOSS_TROMP = new AudioClip(Paths.get("resources", "sounds", "fx", "boss", "tromp.mp3").toUri().toString());
    public static final MediaPlayer BOSS_DEATH = new MediaPlayer(new Media(Paths.get("resources", "sounds", "fx", "boss", "death.mp3").toUri().toString()));

    //cutscenes
    public static final Media THIRD_CUTSCENE = new Media(Paths.get("resources", "videos", "end.mp4").toUri().toString());
    public static final Media SECOND_CUTSCENE = new Media(Paths.get("resources", "videos", "comstock.mp4").toUri().toString());
    public static final Media FIRST_CUTSCENE = new Media(Paths.get("resources", "videos", "elizabeth.mp4").toUri().toString());
}
