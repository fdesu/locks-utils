package com.github.fdesu.lia.transform.visitors;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

import java.util.Arrays;

public class MonitorInstructionsClassVisitor extends ClassVisitor {

	public boolean modified;

	public MonitorInstructionsClassVisitor(int api) {
		super(api);
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
		return new MonitorInstructionsMethodVisitor(api);
	}
}
