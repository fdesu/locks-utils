package com.github.fdesu.lia.transform.visitors;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class MonitorInstructionsMethodVisitor extends MethodVisitor implements ModifyingVisitor {

	private boolean modified;

	public MonitorInstructionsMethodVisitor(int api, MethodVisitor methodVisitor) {
		super(api, methodVisitor);
	}

	@Override
	public void visitInsn(int opcode) {
		super.visitInsn(opcode);
		switch (opcode) {
			case Opcodes.MONITORENTER:
				MonitorUtils.onMonitorEnter(opcode);
				modified = true;
				break;

			case Opcodes.MONITOREXIT:
				MonitorUtils.onMonitorExit(opcode);
				modified = true;
				break;

			default:
				break;
		}
	}

	@Override
	public boolean isModified() {
		return modified;
	}
}
