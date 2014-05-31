package app.lab.reactor;

import app.exceptions.lab.reactor.ReactorAlgorithmSetupException;


public final class ReactorAlgorithmFactory {

	private ReactorAlgorithmFactory() {
		;
	}
		
	public static void setupReactorAlgorithm(Class<? extends ReactorAlgorithm> type) throws ReactorAlgorithmSetupException {
		try {
			type.getDeclaredConstructor((Class<?>[]) null).newInstance((Object[]) null);
		} catch (Exception e) {
			throw new ReactorAlgorithmSetupException();
		}
	}
}
