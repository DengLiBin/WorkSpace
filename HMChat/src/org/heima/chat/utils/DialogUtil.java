package org.heima.chat.utils;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface.OnCancelListener;

public class DialogUtil {

	public Dialog getProgressDialog(Context context, String title,
			String message, boolean indeterminate, boolean cancelable,
			OnCancelListener cancelListener) {
		return ProgressDialog.show(context, title, message, indeterminate,
				cancelable, cancelListener);
	}
}
