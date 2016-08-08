package com.augmentis.ayp.beatboz;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Amita on 8/8/2016.
 */
public class BeatBox {
    private static final String TAG = "BeatBox";
    private static final String SOUND_FOLDER = "sample_sound";
    private static final int MAX_SOUND = 5;

    private AssetManager assets;
    private List<Sound> sounds = new ArrayList<>();
    private SoundPool sondPool;

    public BeatBox(Context context) {
        assets = context.getAssets();
        sondPool = new SoundPool(MAX_SOUND, AudioManager.STREAM_MUSIC, 0);
        loadSound();
    }

    private void loadSound() {
        String[] soundNames;

        try {
            soundNames = assets.list(SOUND_FOLDER);

            Log.i(TAG, "found" + soundNames.length + "sounds");
        } catch (IOException e) {
            Log.e(TAG, "Could no list assets");
            return;
        }

        for (String filename : soundNames) {
            try {
                String assetPath = SOUND_FOLDER + File.separator + filename;
                Sound sound = new Sound(assetPath);
                load(sound);
                sounds.add(sound);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void load(Sound sound)throws IOException {
        AssetFileDescriptor afd = assets.openFd(sound.getAssetPath());
        int sounId = sondPool.load(afd, 1);
        sound.setSoundId(sounId);
    }

    public List<Sound> getSounds() {
        return sounds;
    }

    public void play(Sound sound) {
        Integer soundId = sound.getSoundId();

        if (soundId == null) {
            return;
        }

        sondPool.play(soundId, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    public void release() {
        sondPool.release();
    }
}
