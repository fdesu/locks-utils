package com.github.fdesu.lia.transform.visitors;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class MonitorInstructionsClassVisitor extends ClassVisitor implements ModifyingVisitor {

	private boolean modified;
	private final Set<MonitorInstructionsMethodVisitor> visitors;

	public MonitorInstructionsClassVisitor(int api) {
		super(api);
		visitors = new HashSet<>();
	}

	public boolean isModified() {
		return modified;
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		System.out.println("visitMethod access " + access + " name " + name + " desc " + desc
				+ " signature " + signature + " exceptions " + Arrays.toString(exceptions));
		if (MonitorUtils.ifSynchronizedMethod(access)) {
			MonitorUtils.onSynchronizedMethod(access, name, desc);
		}
		MonitorInstructionsMethodVisitor methodVisitor = new MonitorInstructionsMethodVisitor(api);
		visitors.add(methodVisitor);
		return methodVisitor;
	}

	@Override
	public void visitEnd() {
		super.visitEnd();
		modified |= visitors.stream().anyMatch(ModifyingVisitor::isModified);
	}
}
