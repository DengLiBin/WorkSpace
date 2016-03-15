package org.heima.lib.msg;

import java.util.UUID;

public class SequenceCreater {

	public static String createSequence() {
		return UUID.randomUUID().toString();
	}
}
