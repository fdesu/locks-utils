package com.github.fdesu.lia.transform;

import com.github.fdesu.lia.transform.visitors.MonitorInstructionsClassVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.ProtectionDomain;
import java.util.logging.Logger;

public class MonitorManipulationsSearchTransformer implements ClassFileTransformer {
	private static final Logger LOG = Logger.getLogger(MonitorManipulationsSearchTransformer.class.getName());

	@Override
	public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
			ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
		LOG.info("Trying class: " + className);
		ClassReader reader = new ClassReader(classfileBuffer);
		ClassWriter writer = new ClassWriter(reader, 0);
		MonitorInstructionsClassVisitor monitorVisitor = new MonitorInstructionsClassVisitor(Version.ASM_VERSION, writer);
		reader.accept(monitorVisitor, 0);
		if (monitorVisitor.isModified()) {
			LOG.info("Class " + className + " has been modified");
			byte[] bytes = writer.toByteArray();
			try {
				Files.write(Paths.get("./debug.class"), bytes, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return bytes;
		}

		LOG.info("Class " + className + " has been skipped...");
		return null;
	}
}
