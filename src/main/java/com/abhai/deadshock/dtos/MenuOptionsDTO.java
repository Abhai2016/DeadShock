package com.abhai.deadshock.dtos;

public class MenuOptionsDTO {
    private String track;
    private double fxVolume;
    private double musicVolume;
    private double voiceVolume;

    public String getTrack() {
        return track;
    }

    public double getFxVolume() {
        return fxVolume;
    }

    public double getMusicVolume() {
        return musicVolume;
    }

    public double getVoiceVolume() {
        return voiceVolume;
    }

    public void setTrack(String track) {
        this.track = track;
    }

    public void setFxVolume(double fxVolume) {
        this.fxVolume = fxVolume;
    }

    public void setVoiceVolume(double voiceVolume) {
        this.voiceVolume = voiceVolume;
    }

    public void setMusicVolume(double musicVolume) {
        this.musicVolume = musicVolume;
    }
}
