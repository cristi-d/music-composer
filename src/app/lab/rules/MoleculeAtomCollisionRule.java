package app.lab.rules;

import app.lab.reactor.models.Atom;
import app.lab.reactor.models.Matter;
import app.lab.reactor.models.Molecule;
import app.lab.reactor.models.Molecule.MoleculeLayer;
import app.music.MusicalPhrase;
import app.music.PitchedSound;
import app.music.theory.HorizontalCoherenceMusicalRule;
import app.music.theory.HorizontalStabilityMusicalRule;
import app.music.theory.MusicalRule;
import app.music.theory.VerticalCoherenceMusicalRule;

public class MoleculeAtomCollisionRule extends CollisionRule {

	@Override
	public boolean canApply(Matter base, Matter addition, MoleculeLayer targetLayer) {
		Molecule baseMolecule = (Molecule) base;
		Atom additionAtom = (Atom) addition;
		PitchedSound baseLastSound = baseMolecule.getLastTopLayerAtom().getInfo();
		PitchedSound additionAtomSound = additionAtom.getInfo();
		
		Class<? extends MusicalRule> ruleClass = null;
		
		MusicalPhrase basePhrase = MusicalPhrase.fromMatter(baseMolecule);
		MusicalPhrase additionPhrase = MusicalPhrase.fromMatter(additionAtom);
		
		if (targetLayer == null) {
			return (MusicalRule.getRule(VerticalCoherenceMusicalRule.class).intervalComplies(baseLastSound, additionAtomSound)
					|| MusicalRule.getRule(HorizontalCoherenceMusicalRule.class).intervalComplies(baseLastSound, additionAtomSound))
					&& MusicalRule.getRule(HorizontalStabilityMusicalRule.class).complies(basePhrase, additionPhrase);
		}
		
		switch (targetLayer) {
		case Bottom:
			ruleClass = VerticalCoherenceMusicalRule.class;
			break;
		case Top:
			ruleClass = HorizontalCoherenceMusicalRule.class;
			break;
		}
		
		
		
		return MusicalRule.getRule(ruleClass).intervalComplies(baseLastSound, additionAtomSound) &&
				MusicalRule.getRule(HorizontalStabilityMusicalRule.class).complies(basePhrase, additionPhrase);
	}

	@Override
	public Matter apply(Matter base, Matter addition, MoleculeLayer targetLayer) {
		Molecule baseMolecule = (Molecule) base;
		Atom additionAtom = (Atom) addition;
		
		baseMolecule.addAtom(additionAtom, targetLayer);
		
		return baseMolecule;
	}


}
