package com.github.fdesu.lia.transform;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class MatchingClassDelegatingTransformer implements ClassFileTransformer {

	private final String packagePrefix;
	private final ClassFileTransformer delegate;

	public MatchingClassDelegatingTransformer(String packagePrefix, ClassFileTransformer delegate) {
		this.packagePrefix = packagePrefix.replace('.', '/');
		this.delegate = delegate;
	}

	@Override
	public byte[] transform(Module module, ClassLoader loader, String className, Class<?> classBeingRedefined,
			ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
		if (className.startsWith(packagePrefix)) {
			System.out.println("transforming: " + className);
			return delegate.transform(module, loader, className, classBeingRedefined, protectionDomain, classfileBuffer);
		}

		return null;
	}
}
