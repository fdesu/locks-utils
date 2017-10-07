package com.github.fdesu.lia.transform;

import com.github.fdesu.lia.transform.visitors.MonitorInstructionsClassVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.logging.Logger;

public class MonitorManipulationsSearchTransformer implements ClassFileTransformer {
	private static final Logger LOG = Logger.getLogger(MonitorManipulationsSearchTransformer.class.getName());

	@Override
	public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
			ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
		LOG.info("Trying class: " + className);
		ClassReader reader = new ClassReader(classfileBuffer);
		MonitorInstructionsClassVisitor monitorVisitor = new MonitorInstructionsClassVisitor(Version.ASM_VERSION);
		reader.accept(monitorVisitor, 0);
		if (monitorVisitor.isModified()) {
			LOG.info("Class " + className + " has been modified");
			ClassWriter writer = new ClassWriter(reader, ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
			return writer.toByteArray();
		}

		LOG.info("Class " + className + " has been skipped...");
		return null;
	}
}
