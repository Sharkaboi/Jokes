@file:Suppress("unused")

package com.cybershark.jokes.util

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cybershark.jokes.R
import com.google.android.material.snackbar.Snackbar

internal fun View.shortSnackBar(
    message: String,
    action: (Snackbar.() -> Unit)? = null
) {
    val snackBar = Snackbar.make(this, message, Snackbar.LENGTH_SHORT)
    action?.let { snackBar.it() }
    snackBar.setAnchorView(R.id.bottomNavigation)
    snackBar.show()
}

internal fun Snackbar.action(message: String, action: (View) -> Unit) {
    this.setAction(message, action)
}

internal inline fun <reified T : Activity> Activity.launchAndFinishAffinity(block: Intent.() -> Unit = {}) {
    val intent = Intent(this, T::class.java)
    intent.apply(block)
    startActivity(intent)
    finishAffinity()
}

fun <T> Fragment.observe(liveData: LiveData<T>, action: (t: T) -> Unit) {
    liveData.observe(viewLifecycleOwner) { t ->
        action(t)
    }
}

fun MutableLiveData<UIState>.getDefault() =
    this.apply { value = UIState.Idle }

fun MutableLiveData<UIState>.setLoading() =
    this.apply { value = UIState.Loading }

fun MutableLiveData<UIState>.setSuccess(message: String) =
    this.apply { value = UIState.Success(message) }

fun MutableLiveData<UIState>.setError(message: String) =
    this.apply { value = UIState.Error(message) }

fun String?.getErrorString(): String = this ?: "Error occurred"

fun View.makeVisible() = this.apply { isVisible = true }
fun View.makeGone() = this.apply { isGone = true }

fun Fragment.openLink(url: String) {
    val intent = Intent()
    intent.action = Intent.ACTION_VIEW
    intent.data = Uri.parse(url)
    this.startActivity(intent)
}

fun Fragment.openShareChooser(message: String) {
    val intent = Intent()
    intent.action = Intent.ACTION_SEND
    intent.putExtra(Intent.EXTRA_TEXT, message)
    intent.type = "text/plain"
    this.startActivity(Intent.createChooser(intent, "Share To:"))
}