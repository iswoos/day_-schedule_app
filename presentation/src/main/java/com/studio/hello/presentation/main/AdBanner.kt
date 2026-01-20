package com.studio.hello.presentation.main

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

@Composable
fun AdBanner(modifier: Modifier = Modifier) {
    AndroidView(
        modifier = modifier.fillMaxWidth(),
        factory = { context ->
            AdView(context).apply {
                // 적응형 배너 사이즈 설정 (화면 너비에 맞춤)
                val adSize = AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, 
                    context.resources.displayMetrics.widthPixels / context.resources.displayMetrics.density.toInt())
                setAdSize(adSize)
                
                adUnitId = "ca-app-pub-3940256099942544/6300978111" // 테스트 ID
                
                adListener = object : com.google.android.gms.ads.AdListener() {
                    override fun onAdFailedToLoad(error: com.google.android.gms.ads.LoadAdError) {
                        android.util.Log.e("AdMob", "광고 로드 실패: ${error.message}")
                    }
                    override fun onAdLoaded() {
                        android.util.Log.d("AdMob", "광고 로드 성공")
                    }
                }
                
                loadAd(AdRequest.Builder().build())
            }
        }
    )
}
