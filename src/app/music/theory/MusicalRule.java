package app.music.theory;

import java.util.HashMap;
import java.util.Map;

import app.lab.tools.TapeMeasure;
import app.music.BasicInterval;
import app.music.MusicalPhrase;
import app.music.PitchedSound;

public abstract class MusicalRule {
	private int priority;
	private static Map<Class<? extends MusicalRule>, MusicalRule> instances;
	
	static {
		instances = new HashMap<Class<? extends MusicalRule>, MusicalRule>();
	}
	
	protected MusicalRule() {
		;
	}
	
	public static MusicalRule getRule(Class<? extends MusicalRule> ruleClass) {
		
		if (instances.get(ruleClass) != null) {
			return instances.get(ruleClass);
		} else {
			try {
				MusicalRule instance = ruleClass.getDeclaredConstructor((Class<?>[]) null).newInstance((Object[])null);
				instances.put(ruleClass, instance);
				
				return instance;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
		return null;
	}
	
	public abstract boolean complies(MusicalPhrase startPhrase, MusicalPhrase addition);
	
	public boolean intervalComplies(PitchedSound from, PitchedSound to) {
		BasicInterval interval = TapeMeasure.measureBasicInterval(from, to);
		boolean doesComply = true;
		
		switch (interval) {
		case FourthAug: 
			case SecondMin: 
				case SeventhMin:
					doesComply = false;
			break;
		default:
			doesComply = true;
			break;
	}
		
		return doesComply;
	}
	
	public int getPriority() {
		return priority;
	}
	
	
}
