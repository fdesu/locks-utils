package com.github.fdesu.lia;

import com.github.fdesu.lia.transform.MatchingClassDelegatingTransformer;

import java.lang.instrument.Instrumentation;

public class Bootstrap {

	public static void premain(String agentArgs, Instrumentation inst) {
		inst.addTransformer(new MatchingClassDelegatingTransformer(agentArgs));
	}

}
