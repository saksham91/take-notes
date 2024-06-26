package com.example.takenotes


object Util {

    fun getBgImageResource(bgName: Int): Int {
        var bgRes: Int? = null
        when (bgName) {
            R.id.bg_bright -> bgRes = R.drawable.bg_rain
            R.id.blurry_bg -> bgRes = R.drawable.blurry_background
            R.id.bg_concentric_circle -> bgRes = R.drawable.bg_step
            R.id.bg_wave_dark -> bgRes = R.drawable.bg_side_wave_b
            R.id.bg_wave -> bgRes = R.drawable.wave_bg
        }
        return bgRes!!
    }

    fun getTextColorResource(color: Int): Int {
        var textColor: Int? = null
        when (color) {
            R.id.gradient1 -> textColor = R.color.gradient1
            R.id.gradient2 -> textColor = R.color.gradient2
            R.id.gradient3 -> textColor = R.color.gradient4
            R.id.gradient4 -> textColor = R.color.gradient5
            R.id.gradient5 -> textColor = R.color.gradient6
        }
        return textColor!!
    }

}