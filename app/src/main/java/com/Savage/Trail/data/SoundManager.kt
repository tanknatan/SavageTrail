package com.Color.Sphere.Challenge.gamecolor.data




import android.content.Context
import android.media.MediaPlayer
import com.Savage.Trail.R



object SoundManager {
    private lateinit var musicPlayer: MediaPlayer
    private lateinit var soundPlayer: MediaPlayer

    fun init(context: Context) = runCatching {
        musicPlayer = MediaPlayer.create(context, R.raw.music)
        soundPlayer = MediaPlayer.create(context, R.raw.sound)
    }

    fun playMusic() = runCatching {
        musicPlayer.setVolume(Prefs.music, Prefs.music)
        musicPlayer.start()
        musicPlayer.isLooping = true
    }
    fun playSound() = runCatching {
        soundPlayer.setVolume(Prefs.sound, Prefs.sound)
        soundPlayer.start()
        soundPlayer.isLooping = false
    }

    fun pauseMusic() = runCatching{
        musicPlayer.pause()

    }
    fun pauseSound() = runCatching{

        soundPlayer.pause()
    }

    fun resumeMusic() = runCatching {
        if (!musicPlayer.isPlaying) {
            musicPlayer.start()
        }
    }
    fun resumeSound()= runCatching {
        if (!soundPlayer.isPlaying) {
            soundPlayer.start()
        }
    }


    fun onDestroy() = runCatching {
        musicPlayer.release()
        soundPlayer.release()
    }

    fun setVolumeMusic() = runCatching {
        musicPlayer.setVolume(Prefs.music, Prefs.music)
    }

    fun setVolumeSound() = runCatching {
        soundPlayer.setVolume(Prefs.sound, Prefs.sound)
    }


}