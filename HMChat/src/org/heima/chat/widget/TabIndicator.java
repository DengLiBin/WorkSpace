package org.heima.chat.widget;

import org.heima.chat.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TabIndicator extends RelativeLayout {
	private ImageView ivTabIcon;
	private TextView tvTabHint;
	private TextView tvTabUnRead;

	private int focusId = -1, normalId = -1;

	private int unreadCount = 0;

	public TabIndicator(Context context) {
		this(context, null);
	}

	public TabIndicator(Context context, AttributeSet attrs) {
		super(context, attrs);

		View.inflate(context, R.layout.tab_indicator, this);
		ivTabIcon = (ImageView) findViewById(R.id.tab_indicator_icon);
		tvTabHint = (TextView) findViewById(R.id.tab_indicator_hint);
		tvTabUnRead = (TextView) findViewById(R.id.tab_indicator_unread);

		setUnread(0);
	}

	public void setTabIcon(int normalId, int focusId) {
		this.normalId = normalId;
		this.focusId = focusId;
	}

	public void setTabHint(int hintId) {
		tvTabHint.setText(hintId);
	}

	public void setUnread(int unreadCount) {
		this.unreadCount = unreadCount;

		if (unreadCount <= 0) {
			tvTabUnRead.setVisibility(View.GONE);
		} else {
			if (unreadCount >= 100) {
				tvTabUnRead.setText("99+");
			} else {
				tvTabUnRead.setText("" + unreadCount);
			}
			tvTabUnRead.setVisibility(View.VISIBLE);
		}
	}

	public int getUnreadCount() {
		return this.unreadCount;
	}

	public void setCurrentFocus(boolean current) {
		if (current) {
			if (focusId != -1) {
				ivTabIcon.setImageResource(focusId);
			}
		} else {
			if (normalId != -1) {
				ivTabIcon.setImageResource(normalId);
			}
		}
	}
}
