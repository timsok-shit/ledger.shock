package com.accountbook.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import java.io.ByteArrayOutputStream
import java.io.File
import kotlin.math.PI
import kotlin.math.sin

class SoundManager(context: Context) {

    private val soundPool: SoundPool
    private var clickSoundId: Int = 0
    private var confirmSoundId: Int = 0
    private var deleteSoundId: Int = 0
    private var navSoundId: Int = 0
    private var toggleSoundId: Int = 0

    init {
        val attrs = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()
        soundPool = SoundPool.Builder()
            .setMaxStreams(4)
            .setAudioAttributes(attrs)
            .build()

        val sr = 44100

        clickSoundId = loadWav(context, sr, generateTone(sr, 880.0, 0.04, 0.3), "click.wav")
        confirmSoundId = loadWav(context, sr, generateConfirmTone(sr), "confirm.wav")
        deleteSoundId = loadWav(context, sr, generateTone(sr, 220.0, 0.08, 0.4), "delete.wav")
        navSoundId = loadWav(context, sr, generateTone(sr, 660.0, 0.05, 0.25), "nav.wav")
        toggleSoundId = loadWav(context, sr, generateToggleTone(sr), "toggle.wav")
    }

    fun playClick() { soundPool.play(clickSoundId, 0.6f, 0.6f, 1, 0, 1.0f) }
    fun playConfirm() { soundPool.play(confirmSoundId, 0.7f, 0.7f, 1, 0, 1.0f) }
    fun playDelete() { soundPool.play(deleteSoundId, 0.6f, 0.6f, 1, 0, 1.0f) }
    fun playNav() { soundPool.play(navSoundId, 0.5f, 0.5f, 1, 0, 1.0f) }
    fun playToggle() { soundPool.play(toggleSoundId, 0.5f, 0.5f, 1, 0, 1.0f) }

    fun release() { soundPool.release() }

    private fun loadWav(context: Context, sampleRate: Int, samples: ShortArray, fileName: String): Int {
        val file = File(context.cacheDir, fileName)
        if (!file.exists()) {
            writeWavFile(file, sampleRate, samples)
        }
        return soundPool.load(file.absolutePath, 1)
    }

    private fun generateTone(sampleRate: Int, freq: Double, duration: Double, volume: Double): ShortArray {
        val numSamples = (sampleRate * duration).toInt()
        val samples = ShortArray(numSamples)
        for (i in 0 until numSamples) {
            val t = i.toDouble() / sampleRate
            val envelope = (1.0 - t / duration).coerceIn(0.0, 1.0)
            val wave = sin(2.0 * PI * freq * t)
            samples[i] = (wave * envelope * volume * Short.MAX_VALUE).toInt().toShort()
        }
        return samples
    }

    private fun generateConfirmTone(sampleRate: Int): ShortArray {
        val dur1 = 0.06
        val dur2 = 0.08
        val n1 = (sampleRate * dur1).toInt()
        val n2 = (sampleRate * dur2).toInt()
        val samples = ShortArray(n1 + n2)
        for (i in 0 until n1) {
            val t = i.toDouble() / sampleRate
            val env = (1.0 - t / dur1).coerceIn(0.0, 1.0)
            samples[i] = (sin(2.0 * PI * 880.0 * t) * env * 0.35 * Short.MAX_VALUE).toInt().toShort()
        }
        for (i in 0 until n2) {
            val t = i.toDouble() / sampleRate
            val env = (1.0 - t / dur2).coerceIn(0.0, 1.0)
            samples[n1 + i] = (sin(2.0 * PI * 1320.0 * t) * env * 0.35 * Short.MAX_VALUE).toInt().toShort()
        }
        return samples
    }

    private fun generateToggleTone(sampleRate: Int): ShortArray {
        val dur = 0.05
        val n = (sampleRate * dur).toInt()
        val samples = ShortArray(n)
        for (i in 0 until n) {
            val t = i.toDouble() / sampleRate
            val env = (1.0 - t / dur).coerceIn(0.0, 1.0)
            val freq = 440.0 + 440.0 * (t / dur)
            samples[i] = (sin(2.0 * PI * freq * t) * env * 0.3 * Short.MAX_VALUE).toInt().toShort()
        }
        return samples
    }

    private fun writeWavFile(file: File, sampleRate: Int, samples: ShortArray) {
        val numChannels = 1
        val bitsPerSample = 16
        val byteRate = sampleRate * numChannels * bitsPerSample / 8
        val blockAlign = numChannels * bitsPerSample / 8
        val dataSize = samples.size * blockAlign
        val buffer = ByteArrayOutputStream()

        fun writeString(s: String) { for (c in s) buffer.write(c.code) }
        fun writeInt32(v: Int) { buffer.write(v); buffer.write(v shr 8); buffer.write(v shr 16); buffer.write(v shr 24) }
        fun writeInt16(v: Short) { buffer.write(v.toInt()); buffer.write(v.toInt() shr 8) }

        writeString("RIFF")
        writeInt32(36 + dataSize)
        writeString("WAVE")
        writeString("fmt ")
        writeInt32(16)
        writeInt16(1)
        writeInt16(numChannels.toShort())
        writeInt32(sampleRate)
        writeInt32(byteRate)
        writeInt16(blockAlign.toShort())
        writeInt16(bitsPerSample.toShort())
        writeString("data")
        writeInt32(dataSize)
        for (s in samples) writeInt16(s)

        file.writeBytes(buffer.toByteArray())
    }
}
