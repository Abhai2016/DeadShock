package com.abhai.deadshock;

public class Options {
    private double musicVolume;
    private double fxVolume;
    private double voiceVolume;
    private String track;

    public double getMusicVolume() {
        return musicVolume;
    }

    public void setMusicVolume(double musicVolume) {
        this.musicVolume = musicVolume;
    }

    public double getFxVolume() {
        return fxVolume;
    }

    public void setFxVolume(double fxVolume) {
        this.fxVolume = fxVolume;
    }

    public double getVoiceVolume() {
        return voiceVolume;
    }

    public void setVoiceVolume(double voiceVolume) {
        this.voiceVolume = voiceVolume;
    }

    public String getTrack() {
        return track;
    }

    public void setTrack(String track) {
        this.track = track;
    }
}
