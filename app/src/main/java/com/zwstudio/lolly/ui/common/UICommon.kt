package com.zwstudio.lolly.ui.common

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckedTextView
import android.widget.Spinner
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import com.zwstudio.lolly.R

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
        .setMessage(message).setPositiveButton(context.getString(R.string.yes), dialogClickListener)
        .setNegativeButton(context.getString(R.string.no), dialogClickListener).show()
}
