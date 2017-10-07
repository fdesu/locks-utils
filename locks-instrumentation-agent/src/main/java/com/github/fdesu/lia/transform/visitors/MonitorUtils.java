package com.github.fdesu.lia.transform.visitors;

import org.objectweb.asm.Opcodes;

class MonitorUtils {

	private MonitorUtils() {}

	static boolean ifSynchronizedMethod(int methodAccessFlags) {
		return (methodAccessFlags & Opcodes.ACC_SYNCHRONIZED) != 0;
	}

	static void onSynchronizedMethod(int methodAccess, String name, String description) {
		if (!ifSynchronizedMethod(methodAccess)) {
			throw new IllegalArgumentException("Method [" + name + "] isn't SYNCHRONIZED");
		}

		System.out.println("Method " + name + " w/ description " + description + " is synchronized!");
	}

	static void onMonitorEnter(int opcode) {
		if (opcode != Opcodes.MONITORENTER) {
			throw new IllegalArgumentException("Current insn [" + opcode +"] isn't MONITORENTER");
		}
		System.out.println("MONITORENTER visited");
	}

	static void onMonitorExit(int opcode) {
		if (opcode != Opcodes.MONITOREXIT) {
			throw new IllegalArgumentException("Current insn [" + opcode +"] isn't MONITOREXIT");
		}
		System.out.println("MONITOREXIT visited");
	}

}
