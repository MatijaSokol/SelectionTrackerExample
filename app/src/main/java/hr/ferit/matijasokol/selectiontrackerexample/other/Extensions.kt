package hr.ferit.matijasokol.selectiontrackerexample.other

import android.content.Context
import android.view.View
import android.widget.Toast

fun Context.displayToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}