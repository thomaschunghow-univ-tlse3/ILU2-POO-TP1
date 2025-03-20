package histoire;

import personnages.Chef;
import personnages.Druide;
import personnages.Gaulois;
import villagegaulois.Etal;
import villagegaulois.Village;

public class ScenarioCasDegrade {

	public static void main(String[] args) {
		Etal etal = new Etal();
		Gaulois vendeur = new Gaulois("Vendeur", 5);
		etal.occuperEtal(vendeur, "fleurs", 0);
		System.out.println(etal.acheterProduit(5, null));
		System.out.println(etal.acheterProduit(-5, vendeur));
		Etal autreEtal = new Etal();
		System.out.println(autreEtal.libererEtal());
		try {
			if (!autreEtal.isEtalOccupe()) {
				throw new IllegalStateException("L'étal doit être occupé.");
			}
			System.out.println(autreEtal.acheterProduit(5, vendeur));
		} catch (IllegalStateException e) {
			System.out.println("L'étal doit être occupé\n");
		}
		System.out.println("Fin du test");
	}

}
