package com.zwstudio.lolly.views.misc

import android.app.AlertDialog
import android.content.*
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckedTextView
import android.widget.Spinner
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import com.zwstudio.lolly.views.R
import java.net.URLEncoder

fun View.copyText(text: String) {
    // https://stackoverflow.com/questions/19177231/android-copy-paste-from-clipboard-manager
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("", text)
    // https://stackoverflow.com/questions/57128725/kotlin-android-studio-var-is-seen-as-val-in-sdk-29
    clipboard.setPrimaryClip(clip)
}

fun View.openPage(url: String) {
    // https://stackoverflow.com/questions/12013416/is-there-any-way-in-android-to-force-open-a-link-to-open-in-chrome
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    intent.`package` = "com.android.chrome"
    try {
        context.startActivity(intent)
    } catch (ex: ActivityNotFoundException) {
        // Chrome browser presumably not installed so allow user to choose instead
        intent.`package` = null
        context.startActivity(intent)
    }
}

fun View.googleString(text: String) {
    val url = "https://www.google.com/search?q=" + URLEncoder.encode(text, "UTF-8")
    openPage(url)
}

fun <T> makeAdapter(context: Context, @LayoutRes resource: Int, @IdRes textViewResourceId: Int, objects: List<T>, convert: ArrayAdapter<T>.(View, Int) -> View): ArrayAdapter<T> =
    object : ArrayAdapter<T>(context, resource, textViewResourceId, objects) {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup) =
            convert(super.getView(position, convertView, parent), position)

        override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup) =
            convert(super.getDropDownView(position, convertView, parent), position)
    }

fun <T> makeCustomAdapter(context: Context, objects: List<T>, labelFunc: (T) -> String): ArrayAdapter<T> =
    makeAdapter(context, android.R.layout.simple_spinner_item, 0, objects) { v, position ->
        val tv = v.findViewById<TextView>(android.R.id.text1)
        tv.text = labelFunc(getItem(position)!!)
        v
    }.apply {
        setDropDownViewResource(android.R.layout.simple_list_item_single_choice)
    }

fun <T> Spinner.makeCustomAdapter2(context: Context, objects: List<T>, labelFunc: (T) -> String, labelFunc2: (T) -> String) {
    adapter = makeAdapter(context, R.layout.spinner_item_2, android.R.id.text1, objects) { v, position ->
        val item = getItem(position)!!
        var tv = v.findViewById<TextView>(android.R.id.text1)
        tv.text = labelFunc(item)
        (tv as? CheckedTextView)?.isChecked = selectedItemPosition == position
        tv = v.findViewById(android.R.id.text2)
        tv.text = labelFunc2(item)
        v
    }.apply {
        setDropDownViewResource(R.layout.list_item_2)
    }
}

fun yesNoDialog(context: Context, message: String, yesAction: () -> Unit, noAction: () -> Unit) {
    val dialogClickListener = DialogInterface.OnClickListener { _, which ->
        when (which) {
            DialogInterface.BUTTON_POSITIVE ->
                //Yes button clicked
                yesAction()
            DialogInterface.BUTTON_NEGATIVE -> {
                //No button clicked
                noAction()
            }
        }
    }

    AlertDialog.Builder(context)
        .setMessage(message).setPositiveButton("Yes", dialogClickListener)
        .setNegativeButton("No", dialogClickListener).show()
}
