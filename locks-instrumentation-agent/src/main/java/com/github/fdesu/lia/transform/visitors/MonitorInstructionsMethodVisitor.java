package com.github.fdesu.lia.transform.visitors;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class MonitorInstructionsMethodVisitor extends MethodVisitor {
	public MonitorInstructionsMethodVisitor(int api) {
		super(api);
	}

	@Override
	public void visitInsn(int opcode) {
		switch (opcode) {
			case Opcodes.MONITORENTER:
				MonitorUtils.onMonitorEnter(opcode);
				break;

			case Opcodes.MONITOREXIT:
				MonitorUtils.onMonitorExit(opcode);
				break;

			default:
				break;
		}
	}
}
