package villagegaulois;

import personnages.Chef;
import personnages.Gaulois;

public class Village {
	private String nom;
	private Chef chef;
	private Gaulois[] villageois;
	private int nbVillageois = 0;
	private Marche marche;

	public Village(String nom, int nbVillageoisMaximum, int nbEtals) {
		this.nom = nom;
		villageois = new Gaulois[nbVillageoisMaximum];
		this.marche = new Marche(nbEtals);
	}

	public String getNom() {
		return nom;
	}

	public void setChef(Chef chef) {
		this.chef = chef;
	}

	public void ajouterHabitant(Gaulois gaulois) {
		if (nbVillageois < villageois.length) {
			villageois[nbVillageois] = gaulois;
			nbVillageois++;
		}
	}

	public Gaulois trouverHabitant(String nomGaulois) {
		if (nomGaulois.equals(chef.getNom())) {
			return chef;
		}
		for (int i = 0; i < nbVillageois; i++) {
			Gaulois gaulois = villageois[i];
			if (gaulois.getNom().equals(nomGaulois)) {
				return gaulois;
			}
		}
		return null;
	}

	public String afficherVillageois() {
		if (chef == null) {
			throw new VillageSansChefException(nom);
		}
		StringBuilder chaine = new StringBuilder();
		if (nbVillageois < 1) {
			chaine.append("Il n'y a encore aucun habitant au village du chef " + chef.getNom() + ".\n");
		} else {
			chaine.append("Au village du chef " + chef.getNom() + " vivent les légendaires gaulois :\n");
			for (int i = 0; i < nbVillageois; i++) {
				chaine.append("- " + villageois[i].getNom() + "\n");
			}
		}
		return chaine.toString();
	}

	public String installerVendeur(Gaulois vendeur, String produit, int nbProduit) {
		int indiceEtal = marche.trouverEtalLibre();
		marche.utiliserEtal(indiceEtal, vendeur, produit, nbProduit);
		StringBuilder chaine = new StringBuilder();
		chaine.append(vendeur.getNom() + " cherche un endroit pour vendre " + nbProduit + " " + produit + "\n");
		chaine.append("Le vendeur " + vendeur.getNom() + " vend des " + produit + " à l'étal n°" + indiceEtal);
		return chaine.toString();
	}

	public String rechercherVendeursProduit(String produit) {
		Etal[] etals = marche.trouverEtals(produit);
		if (etals.length == 0) {
			return "Il n'y a pas de vendeur qui propose des " + produit + " au marché.\n";
		}
		if (etals.length == 1) {
			return "Seul le vendeur " + etals[0].getVendeur().getNom() + " propose des " + produit + " au marché.\n";
		}
		StringBuilder chaine = new StringBuilder();
		chaine.append("Les vendeurs qui proposent des " + produit + " sont :\n");
		for (int i = 0; i < etals.length; i++) {
			chaine.append("- " + etals[i].getVendeur().getNom() + "\n");
		}
		return chaine.toString();
	}

	public Etal rechercherEtal(Gaulois vendeur) {
		return marche.trouverVendeur(vendeur);
	}

	public String partirVendeur(Gaulois vendeur) {
		Etal etal = rechercherEtal(vendeur);
		return etal.libererEtal();
	}

	public String afficherMarche() {
		return marche.afficherMarche();
	}

	private static class Marche {
		private Etal[] etals;

		public Marche(int nbEtals) {
			etals = new Etal[nbEtals];
			for (int i = 0; i < nbEtals; i++) {
				etals[i] = new Etal();
			}
		}

		public void utiliserEtal(int indiceEtal, Gaulois vendeur, String produit, int nbProduit) {
			(this.etals[indiceEtal]).occuperEtal(vendeur, produit, nbProduit);
		}

		public int trouverEtalLibre() {
			for (int i = 0; i < this.etals.length; i++) {
				if (!this.etals[i].isEtalOccupe()) {
					return i;
				}
			}
			return -1;
		}

		private int nombreEtalsVendantProduit(String produit) {
			int nbEtals = 0;
			for (int i = 0; i < trouverEtalLibre(); i++) {
				if (etals[i].contientProduit(produit)) {
					nbEtals++;
				}
			}
			return nbEtals;
		}

		public Etal[] trouverEtals(String produit) {
			int nbEtals = nombreEtalsVendantProduit(produit);
			Etal[] etalsVendantProduit = new Etal[nbEtals];
			int j = 0;
			for (int i = 0; i < trouverEtalLibre(); i++) {
				if (etals[i].contientProduit(produit)) {
					etalsVendantProduit[j] = etals[i];
					j++;
				}
			}
			return etalsVendantProduit;
		}

		public Etal trouverVendeur(Gaulois gaulois) {
			for (int i = 0; i < trouverEtalLibre(); i++) {
				if (etals[i].getVendeur() == gaulois) {
					return etals[i];
				}
			}
			return null;
		}

		public String afficherMarche() {
			StringBuilder chaine = new StringBuilder();
			for (int i = 0; i < etals.length; i++) {
				if (etals[i].isEtalOccupe()) {
					chaine.append(etals[i].afficherEtal());
				}
			}
			chaine.append("Il reste " + (etals.length - trouverEtalLibre()) + " étals non utilisés dans le marché.\n");
			return chaine.toString();
		}
	}

	public class VillageSansChefException extends IllegalArgumentException {

		public VillageSansChefException(String message) {
			super(message);
		}

	}
}