package com.sevenzeroes.stopped_typing_edit_text

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatEditText
import androidx.lifecycle.LifecycleCoroutineScope
import kotlinx.coroutines.*
import kotlin.time.Duration.Companion.seconds

val second = 1.seconds.inWholeSeconds

class StoppedTypingEditText(
    context: Context,
    private val attrs: AttributeSet,
    private val scope: LifecycleCoroutineScope
) : AppCompatEditText(context, attrs) {

    private var delay: Int = 800

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.StoppedTypingEditText)
        delay = typedArray.getInt(R.styleable.StoppedTypingEditText_delay, this.delay)
    }

    private var callback: (CharSequence) -> Unit = {  }

    fun setOnStoppedTyping(callback: (CharSequence) -> Unit) {
        this.callback = callback
    }

    override fun onTextChanged(
        text: CharSequence?,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int
    ) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
        scope.launch(Dispatchers.IO) {
            delay(3000)
            withContext(Dispatchers.Main) {
                callback.invoke("")
            }
        }
        scope.launchWhenResumed {  }
    }
}