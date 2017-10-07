package com.github.fdesu.lia;

import com.github.fdesu.lia.transform.MatchingClassDelegatingTransformer;
import com.github.fdesu.lia.transform.MonitorManipulationsSearchTransformer;

import java.lang.instrument.Instrumentation;

public class Bootstrap {

	public static void premain(String agentArgs, Instrumentation inst) {
		if (agentArgs == null) {
			inst.addTransformer(new MonitorManipulationsSearchTransformer());
		} else {
			inst.addTransformer(new MatchingClassDelegatingTransformer(agentArgs, new MonitorManipulationsSearchTransformer()));
		}
	}

}
