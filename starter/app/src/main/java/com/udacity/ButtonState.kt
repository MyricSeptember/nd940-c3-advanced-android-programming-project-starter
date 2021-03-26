package com.udacity


sealed class ButtonState(val buttonText: Int) {
    object Downloading : ButtonState(R.string.button_downloading)
    object Loading : ButtonState(R.string.button_loading)
    object Completed : ButtonState(R.string.button_download)
}