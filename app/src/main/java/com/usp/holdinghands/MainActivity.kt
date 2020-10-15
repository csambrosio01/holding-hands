package com.usp.holdinghands

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        launcherAnimation()
    }

    private fun launcherAnimation() {
        val topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation)
        val bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation)

        val image = findViewById<ImageView>(R.id.holding_hands_logo)
        val text = findViewById<TextView>(R.id.holding_hands_text)

        image.startAnimation(topAnim)
        text.startAnimation(bottomAnim)

        Handler().postDelayed({
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
            finish()
        }, 2000)
    }
}
