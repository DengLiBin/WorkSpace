package org.heima.chat.widget;

import org.heima.chat.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager.LayoutParams;

public class DialogChooseImage extends Dialog {

	public DialogChooseImage(Context context) {
		super(context, R.style.dialog_logout);
		setCanceledOnTouchOutside(true);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutParams params = getWindow().getAttributes();
		params.gravity = Gravity.BOTTOM;
		setContentView(R.layout.dialog_choose_image);

		findViewById(R.id.dialog_btn_cancel).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						dismiss();
					}
				});
	}

	public void setClickCameraListener(View.OnClickListener listener) {
		findViewById(R.id.dialog_btn_camera).setOnClickListener(listener);
	}

	public void setClickGalleryListener(View.OnClickListener listener) {
		findViewById(R.id.dialog_btn_gallery).setOnClickListener(listener);
	}

}
