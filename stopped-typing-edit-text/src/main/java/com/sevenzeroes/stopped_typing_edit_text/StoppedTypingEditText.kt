package com.sevenzeroes.stopped_typing_edit_text

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.lifecycle.LifecycleCoroutineScope
import kotlinx.coroutines.*

class StoppedTypingEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = android.R.attr.editTextStyle
) : AppCompatEditText(context, attrs, defStyleAttr) {

    private var delay: Int = 0
    private var scope: LifecycleCoroutineScope? = null
    private var job: Job? = null

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.StoppedTypingEditText)
        delay = typedArray.getInt(R.styleable.StoppedTypingEditText_delay, 0)
        typedArray.recycle()
    }

    private var callback: (CharSequence) -> Unit = { }

    fun setOnStoppedTyping(scope: LifecycleCoroutineScope, callback: (CharSequence) -> Unit) {
        this.scope = scope
        this.callback = callback
    }

    override fun onTextChanged(
        text: CharSequence?,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int
    ) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
        job?.cancel()
        job = null
        if (delay!=0){
            launchCallback()
        }
    }

    private fun launchCallback() {

        job = scope?.launch(Dispatchers.IO) {
            delay(delay.toLong())
            if (isActive)
                withContext(Dispatchers.Main) {
                    callback.invoke("")
                } else return@launch
        }
    }

}