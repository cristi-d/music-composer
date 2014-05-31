package app.lab.rules;

import app.lab.reactor.models.Matter;
import app.lab.reactor.models.Molecule;
import app.lab.reactor.models.Molecule.MoleculeLayer;
import app.music.MusicalPhrase;
import app.music.theory.HorizontalCoherenceMusicalRule;
import app.music.theory.HorizontalStabilityMusicalRule;
import app.music.theory.MusicalRule;

public class MoleculeMoleculeCollisionRule extends CollisionRule {

	@Override
	public boolean canApply(Matter base, Matter addition, MoleculeLayer targetLayer) {
		MusicalPhrase basePhrase = MusicalPhrase.fromMatter(base);
		MusicalPhrase additionPhrase = MusicalPhrase.fromMatter(addition);
		
		return MusicalRule.getRule(HorizontalCoherenceMusicalRule.class).complies(basePhrase, additionPhrase)
				&& MusicalRule.getRule(HorizontalStabilityMusicalRule.class).complies(basePhrase, additionPhrase);
	}

	@Override
	public Matter apply(Matter base, Matter addition, MoleculeLayer targetLayer) {
		Molecule baseMolecule = (Molecule) base;
		Molecule additionMolecule = (Molecule) addition;
		
		baseMolecule.linkTo(additionMolecule);
		return baseMolecule;
	}

}
