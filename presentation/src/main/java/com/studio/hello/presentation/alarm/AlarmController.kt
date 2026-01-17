package com.studio.hello.presentation.alarm

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.*
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AlarmController @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private var vibrator: Vibrator? = null
    private var ringtone: Ringtone? = null
    private val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    fun start() {
        // 진동 시작 (무음 모드에서도 강제 실행을 위해)
        vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }

        val vibrationEffect = VibrationEffect.createWaveform(longArrayOf(0, 1000, 1000), 1)
        vibrator?.vibrate(vibrationEffect)

        // 벨소리 설정
        val alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
            ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
        
        ringtone = RingtoneManager.getRingtone(context, alarmUri)
        
        // 현재 벨소리 모드 확인 (벨소리 모드일 때만 소리 재생)
        if (audioManager.ringerMode == AudioManager.RINGER_MODE_NORMAL) {
            ringtone?.play()
        }
    }

    fun stop() {
        vibrator?.cancel()
        ringtone?.stop()
    }
}
