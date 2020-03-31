package com.exampleproject

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.androidadvance.topsnackbar.TSnackbar
import com.haroldadmin.vector.VectorFragment


abstract class BaseFragment : VectorFragment() {

    val dialog = ProgressDialog()

    abstract val layoutId: Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layoutId, container, false)
    }


    fun hideLoading() {
        childFragmentManager.hide(dialog)
    }

    fun showLoading() {
        if (!dialog.isAdded) {
            childFragmentManager.showDialog(dialog)
        }
    }

    fun showError(text: String) {
        if (!isAdded)
            return
        val viewGroup = activity?.window?.decorView?.findViewById<ViewGroup>(android.R.id.content)!!
        val snackbar: TSnackbar = TSnackbar.make(
            viewGroup.rootView,
            text,
            TSnackbar.LENGTH_LONG
        )
        snackbar.setActionTextColor(Color.WHITE)
        val snackbarView: View = snackbar.view
        snackbarView.setBackgroundColor(ContextCompat.getColor(activity!!, R.color.СЕRed))
        val textView =
            snackbarView.findViewById<View>(com.androidadvance.topsnackbar.R.id.snackbar_text) as TextView
        textView.setTextColor(Color.WHITE)
        snackbar.show()
    }

    fun showError(errorCode: ErrorCode) {
        val text = requireActivity().getString(errorCode.toStringRes())
        showError(text)
    }

    fun notImplemented() {
        Toast.makeText(requireContext(), "Not implemented yet", Toast.LENGTH_SHORT).show()
    }
}