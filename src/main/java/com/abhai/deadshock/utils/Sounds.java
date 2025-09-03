package com.abhai.deadshock.utils;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.nio.file.Paths;

public class Sounds {
    //Booker
    public static final AudioClip fuck = new AudioClip(Paths.get("resources", "sounds", "voices", "booker", "fuck.mp3").toUri().toString());
    public static final AudioClip shit = new AudioClip(Paths.get("resources", "sounds", "voices", "booker", "shit.mp3").toUri().toString());
    public static final AudioClip great = new AudioClip(Paths.get("resources", "sounds", "voices", "booker", "great.mp3").toUri().toString());
    public static final AudioClip bookerHit = new AudioClip(Paths.get("resources", "sounds", "voices", "booker", "hit.mp3").toUri().toString());
    public static final AudioClip letsGo = new AudioClip(Paths.get("resources", "sounds", "voices", "booker", "letsGo.mp3").toUri().toString());
    public static final AudioClip bookerHit2 = new AudioClip(Paths.get("resources", "sounds", "voices", "booker", "hit2.mp3").toUri().toString());
    public static final AudioClip bookerHit3 = new AudioClip(Paths.get("resources", "sounds", "voices", "booker", "hit3.mp3").toUri().toString());
    public static final AudioClip cretins = new AudioClip(Paths.get("resources", "sounds", "voices", "booker", "cretins.mp3").toUri().toString());
    public static final AudioClip willWork = new AudioClip(Paths.get("resources", "sounds", "voices", "booker", "willWork.mp3").toUri().toString());
    public static final AudioClip energetic = new AudioClip(Paths.get("resources", "sounds", "voices", "booker", "energetic.mp3").toUri().toString());
    public static final AudioClip feelsBetter = new AudioClip(Paths.get("resources", "sounds", "voices", "booker", "feelsBetter.mp3").toUri().toString());
    public static final AudioClip newEnergetic = new AudioClip(Paths.get("resources", "sounds", "voices", "booker", "newEnergetic.mp3").toUri().toString());
    public static final AudioClip feelingBetter = new AudioClip(Paths.get("resources", "sounds", "voices", "booker", "feelingBetter.mp3").toUri().toString());

    //Elizabeth
    public static final AudioClip empty = new AudioClip(Paths.get("resources", "sounds", "voices", "elizabeth", "empty.mp3").toUri().toString());
    public static final AudioClip freedom = new AudioClip(Paths.get("resources", "sounds", "voices", "elizabeth", "freedom.mp3").toUri().toString());
    public static final AudioClip tryToFind = new AudioClip(Paths.get("resources", "sounds", "voices", "elizabeth", "tryToFind.mp3").toUri().toString());
    public static final AudioClip haveNothing = new AudioClip(Paths.get("resources", "sounds", "voices", "elizabeth", "haveNothing.mp3").toUri().toString());
    public static final AudioClip bookerCatch = new AudioClip(Paths.get("resources", "sounds", "voices", "elizabeth", "bookerCatch.mp3").toUri().toString());
    public static final AudioClip bookerCatch2 = new AudioClip(Paths.get("resources", "sounds", "voices", "elizabeth", "bookerCatch2.mp3").toUri().toString());
    public static final AudioClip bookerCatch3 = new AudioClip(Paths.get("resources", "sounds", "voices", "elizabeth", "bookerCatch3.mp3").toUri().toString());
    public static final AudioClip foundNothing = new AudioClip(Paths.get("resources", "sounds", "voices", "elizabeth", "foundNothing.mp3").toUri().toString());
    public static final MediaPlayer ohBooker = new MediaPlayer(new Media(Paths.get("resources", "sounds", "voices", "elizabeth", "booker.mp3").toUri().toString()));
    public static final MediaPlayer whereAreYouFrom = new MediaPlayer(new Media(Paths.get("resources", "sounds", "voices", "elizabeth", "whereAreYouFrom.mp3").toUri().toString()));

    //Enemies
    public static final AudioClip die = new AudioClip(Paths.get("resources", "sounds", "voices", "enemies", "die.mp3").toUri().toString());
    public static final AudioClip death = new AudioClip(Paths.get("resources", "sounds", "voices", "enemies", "death.mp3").toUri().toString());
    public static final AudioClip attack = new AudioClip(Paths.get("resources", "sounds", "voices", "enemies", "attack.mp3").toUri().toString());
    public static final AudioClip death2 = new AudioClip(Paths.get("resources", "sounds", "voices", "enemies", "death2.mp3").toUri().toString());
    public static final AudioClip killMe = new AudioClip(Paths.get("resources", "sounds", "voices", "enemies", "killMe.mp3").toUri().toString());
    public static final AudioClip stupid = new AudioClip(Paths.get("resources", "sounds", "voices", "enemies", "stupid.mp3").toUri().toString());
    public static final AudioClip noAmmo = new AudioClip(Paths.get("resources", "sounds", "voices", "enemies", "noAmmo.mp3").toUri().toString());
    public static final AudioClip noAmmo2 = new AudioClip(Paths.get("resources", "sounds", "voices", "enemies", "noAmmo2.mp3").toUri().toString());
    public static final AudioClip lostHim = new AudioClip(Paths.get("resources", "sounds", "voices", "enemies", "lostHim.mp3").toUri().toString());
    public static final AudioClip audioClipHit = new AudioClip(Paths.get("resources", "sounds", "voices", "enemies", "hit.mp3").toUri().toString());
    public static final AudioClip needAmmo = new AudioClip(Paths.get("resources", "sounds", "voices", "enemies", "needAmmo.mp3").toUri().toString());
    public static final AudioClip heIsHere = new AudioClip(Paths.get("resources", "sounds", "voices", "enemies", "heIsHere.mp3").toUri().toString());
    public static final AudioClip takeThem = new AudioClip(Paths.get("resources", "sounds", "voices", "enemies", "takeThem.mp3").toUri().toString());
    public static final AudioClip lostHim2 = new AudioClip(Paths.get("resources", "sounds", "voices", "enemies", "lostHim2.mp3").toUri().toString());
    public static final AudioClip wereHere = new AudioClip(Paths.get("resources", "sounds", "voices", "enemies", "wereHere.mp3").toUri().toString());
    public static final AudioClip audioClipHit3 = new AudioClip(Paths.get("resources", "sounds", "voices", "enemies", "hit3.mp3").toUri().toString());
    public static final AudioClip audioClipFire = new AudioClip(Paths.get("resources", "sounds", "voices", "enemies", "fire.mp3").toUri().toString());
    public static final AudioClip audioClipHit2 = new AudioClip(Paths.get("resources", "sounds", "voices", "enemies", "hit2.mp3").toUri().toString());
    public static final AudioClip reloading = new AudioClip(Paths.get("resources", "sounds", "voices", "enemies", "reloading.mp3").toUri().toString());
    public static final AudioClip allYouCan = new AudioClip(Paths.get("resources", "sounds", "voices", "enemies", "allYouCan.mp3").toUri().toString());
    public static final AudioClip needAmmo2 = new AudioClip(Paths.get("resources", "sounds", "voices", "enemies", "needAmmo2.mp3").toUri().toString());
    public static final AudioClip whoAreYou = new AudioClip(Paths.get("resources", "sounds", "voices", "enemies", "whoAreYou.mp3").toUri().toString());
    public static final AudioClip heHasGone = new AudioClip(Paths.get("resources", "sounds", "voices", "enemies", "heHasGone.mp3").toUri().toString());
    public static final AudioClip dieAlready = new AudioClip(Paths.get("resources", "sounds", "voices", "enemies", "dieAlready.mp3").toUri().toString());
    public static final AudioClip heHasGone3 = new AudioClip(Paths.get("resources", "sounds", "voices", "enemies", "heHasGone3.mp3").toUri().toString());
    public static final AudioClip heHasGone2 = new AudioClip(Paths.get("resources", "sounds", "voices", "enemies", "heHasGone2.mp3").toUri().toString());
    public static final AudioClip wontGoAway = new AudioClip(Paths.get("resources", "sounds", "voices", "enemies", "wontGoAway.mp3").toUri().toString());
    public static final AudioClip audioClipCamper = new AudioClip(Paths.get("resources", "sounds", "voices", "enemies", "camper.wav").toUri().toString());
    public static final AudioClip theyAreHere = new AudioClip(Paths.get("resources", "sounds", "voices", "enemies", "theyAreHere.mp3").toUri().toString());
    public static final AudioClip canYouShoot = new AudioClip(Paths.get("resources", "sounds", "voices", "enemies", "canYouShoot.mp3").toUri().toString());
    public static final AudioClip keepShooting = new AudioClip(Paths.get("resources", "sounds", "voices", "enemies", "keepShooting.mp3").toUri().toString());
    public static final AudioClip getDownWeapon = new AudioClip(Paths.get("resources", "sounds", "voices", "enemies", "getDownWeapon.mp3").toUri().toString());
    public static final AudioClip keepShooting2 = new AudioClip(Paths.get("resources", "sounds", "voices", "enemies", "keepShooting2.mp3").toUri().toString());
    public static final AudioClip giveHimBullets = new AudioClip(Paths.get("resources", "sounds", "voices", "enemies", "giveHimBullets.mp3").toUri().toString());
    public static final AudioClip dontSpareBullets = new AudioClip(Paths.get("resources", "sounds", "voices", "enemies", "dontSpareBullets.mp3").toUri().toString());

    //fx
    public static final Media rpgReload = new Media(Paths.get("resources", "sounds", "fx", "weapons", "rpgReload.mp3").toUri().toString());
    public static final Media pistolReload = new Media(Paths.get("resources", "sounds", "fx", "weapons", "pistolReload.mp3").toUri().toString());
    public static final AudioClip hypnosis = new AudioClip(Paths.get("resources", "sounds", "fx", "energetics", "hypnosis.mp3").toUri().toString());
    public static final AudioClip pistolShot = new AudioClip(Paths.get("resources", "sounds", "fx", "weapons", "pistolShot.mp3").toUri().toString());
    public static final Media machineGunReload = new Media(Paths.get("resources", "sounds", "fx", "weapons", "machineGunReload.mp3").toUri().toString());
    public static final AudioClip rpgExplosion = new AudioClip(Paths.get("resources", "sounds", "fx", "weapons", "rpgExplosion.mp3").toUri().toString());
    public static final AudioClip closeCombat = new AudioClip(Paths.get("resources", "sounds", "voices", "booker", "closeCombat.mp3").toUri().toString());
    public static final AudioClip electricity = new AudioClip(Paths.get("resources", "sounds", "fx", "energetics", "electricity.mp3").toUri().toString());
    public static final Media rpgShotWithReload = new Media(Paths.get("resources", "sounds", "fx", "weapons", "rpgShotWithReload.mp3").toUri().toString());
    public static final AudioClip machineGunShot = new AudioClip(Paths.get("resources", "sounds", "fx", "weapons", "machineGunShot.mp3").toUri().toString());
    public static final AudioClip devilKissShot = new AudioClip(Paths.get("resources", "sounds", "fx", "energetics", "devilKissShot.mp3").toUri().toString());
    public static final AudioClip audioClipOpenMenu = new AudioClip(Paths.get("resources", "sounds", "fx", "vendingMachine", "openMenu.mp3").toUri().toString());
    public static final AudioClip audioClipPurchase = new AudioClip(Paths.get("resources", "sounds", "fx", "vendingMachine", "purchase.mp3").toUri().toString());
    public static final AudioClip changeToHypnosis = new AudioClip(Paths.get("resources", "sounds", "fx", "energetics", "changeToHypnosis.mp3").toUri().toString());
    public static final AudioClip electricityDeath = new AudioClip(Paths.get("resources", "sounds", "fx", "energetics", "electricityDeath.mp3").toUri().toString());
    public static final AudioClip audioClipChangeItem = new AudioClip(Paths.get("resources", "sounds", "fx", "vendingMachine", "changeItem.mp3").toUri().toString());
    public static final AudioClip changeToDevilKiss = new AudioClip(Paths.get("resources", "sounds", "fx", "energetics", "changeToDevilKiss.mp3").toUri().toString());
    public static final AudioClip changeToElectricity = new AudioClip(Paths.get("resources", "sounds", "fx", "energetics", "changeToElectricity.mp3").toUri().toString());

    //boss
    public static final AudioClip bossHit = new AudioClip(Paths.get("resources", "sounds", "fx", "boss", "hit.mp3").toUri().toString());
    public static final AudioClip bossHit2 = new AudioClip(Paths.get("resources", "sounds", "fx", "boss", "hit2.mp3").toUri().toString());
    public static final AudioClip bossHit3 = new AudioClip(Paths.get("resources", "sounds", "fx", "boss", "hit3.mp3").toUri().toString());
    public static final AudioClip bossTromp = new AudioClip(Paths.get("resources", "sounds", "fx", "boss", "tromp.mp3").toUri().toString());
    public static final MediaPlayer bossDeath = new MediaPlayer(new Media(Paths.get("resources", "sounds", "fx", "boss", "death.mp3").toUri().toString()));
}
