package com.example.app;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Officine {

    private final Map<String, Integer> stock = new HashMap<>();
    private final Map<String, List<String>> recettes = new HashMap<>();

    private static final Map<String, String> IRREG_PL_TO_SG = Map.of(
            "yeux", "oeil"
    );

    private static final Pattern QTE_NOM = Pattern.compile("^\\s*(\\d+)\\s+(.+?)\\s*$");

    public Officine() {
        // Ingrédients de base (stocks à 0 au départ)
        List<String> ingredients = List.of(
                "oeil de grenouille",
                "larme de brume funèbre",
                "radicelle de racine hurlante",
                "pincée de poudre de lune",
                "croc de troll",
                "fragment d'écaille de dragonnet",
                "goutte de sang de citrouille"
        );
        ingredients.forEach(n -> stock.put(canonical(n), 0));

        // Potions (on les suit aussi en stock)
        List<String> potions = List.of(
                "fiole de glaires purulentes",
                "bille d'âme évanescente",
                "soupçon de sels suffocants",
                "baton de pâte sépulcrale",
                "bouffée d'essence de cauchemar"
        );
        potions.forEach(n -> stock.putIfAbsent(canonical(n), 0));

        // Recettes
        recettes.put(canonical("fiole de glaires purulentes"),
                List.of("2 larmes de brume funèbre", "1 goutte de sang de citrouille"));
        recettes.put(canonical("bille d'âme évanescente"),
                List.of("3 pincées de poudre de lune", "1 oeil de grenouille"));
        recettes.put(canonical("soupçon de sels suffocants"),
                List.of("2 crocs de troll", "1 fragment d'écaille de dragonnet", "1 radicelle de racine hurlante"));
        recettes.put(canonical("baton de pâte sépulcrale"),
                List.of("3 radicelles de racine hurlante", "1 fiole de glaires purulentes"));
        recettes.put(canonical("bouffée d'essence de cauchemar"),
                List.of("2 pincées de poudre de lune", "2 larmes de brume funèbre"));
        // S'assurer que tous les items mentionnés par les recettes existent en stock (à 0 si besoin)
        recettes.values().forEach(l -> l.forEach(req -> {
            String name = parseName(req);
            stock.putIfAbsent(canonical(name), 0);
        }));
    }

    /** Ajoute au stock. Exemple: rentrer("3 yeux de grenouille"). */
    public void rentrer(String entree) {
        int qty = parseQty(entree);
        String name = canonical(parseName(entree));
        stock.put(name, stock.getOrDefault(name, 0) + qty);
    }

    /** Quantité actuelle en stock (ingrédient ou potion). */
    public int quantite(String nom) {
        return stock.getOrDefault(canonical(nom), 0);
    }

    /**
     * Prépare des potions selon les stocks et la recette.
     * Exemple: preparer("2 fioles de glaires purulentes") -> nombre réellement préparé.
     * Met à jour les stocks: -ingrédients, +potions produites.
     */
    public int preparer(String demande) {
        int voulu = parseQty(demande);
        String potionNom = canonical(parseName(demande));

        if (!recettes.containsKey(potionNom)) {
            // Pas de recette -> on ne peut rien fabriquer
            return 0;
        }

        // Besoins par unité de potion
        Map<String, Integer> besoinUnitaire = new HashMap<>();
        for (String r : recettes.get(potionNom)) {
            int q = parseQty(r);
            String n = canonical(parseName(r));
            besoinUnitaire.merge(n, q, Integer::sum);
            // Assurer présence au stock
            stock.putIfAbsent(n, stock.getOrDefault(n, 0));
        }

        // Calcul du nombre réalisable en fonction des stocks
        int realisable = Integer.MAX_VALUE;
        for (Map.Entry<String, Integer> e : besoinUnitaire.entrySet()) {
            String ingr = e.getKey();
            int besoin = e.getValue();
            int enStock = stock.getOrDefault(ingr, 0);
            realisable = Math.min(realisable, enStock / besoin);
        }
        if (realisable == Integer.MAX_VALUE) realisable = 0;

        int aFaire = Math.min(voulu, realisable);
        if (aFaire <= 0) return 0;

        // Décrémente ingrédients
        for (Map.Entry<String, Integer> e : besoinUnitaire.entrySet()) {
            String ingr = e.getKey();
            int totalBesoin = e.getValue() * aFaire;
            stock.put(ingr, stock.get(ingr) - totalBesoin);
        }
        // Incrémente le stock de la potion produite
        stock.put(potionNom, stock.getOrDefault(potionNom, 0) + aFaire);

        return aFaire;
    }

    /* ===================== Utilitaires ===================== */

    private static int parseQty(String s) {
        Matcher m = QTE_NOM.matcher(s);
        if (m.matches()) {
            return Integer.parseInt(m.group(1));
        }
        // si pas de nombre, on considère 1 par défaut
        return 1;
    }

    private static String parseName(String s) {
        Matcher m = QTE_NOM.matcher(s);
        String name = s;
        if (m.matches()) {
            name = m.group(2);
        }
        return normalizeSpaces(name.toLowerCase(Locale.ROOT));
    }

    private static String normalizeSpaces(String s) {
        return s.trim().replaceAll("\\s+", " ");
    }

    /** Canonicalise en "singulier" grossier pour faire correspondre singulier/pluriel. */
    private static String canonical(String locution) {
        locution = normalizeSpaces(locution.toLowerCase(Locale.ROOT));
        String[] parts = locution.split(" ", 2);
        String first = parts[0];

        // Irréguliers (yeux -> oeil)
        if (IRREG_PL_TO_SG.containsKey(first)) first = IRREG_PL_TO_SG.get(first);
        else if (first.endsWith("s") && first.length() > 1) first = first.substring(0, first.length() - 1);

        return parts.length > 1 ? (first + " " + parts[1]) : first;
    }

    /* ========= Petit main de démo (facultatif) ========= */
    public static void main(String[] args) {
        Officine o = new Officine();
        o.rentrer("3 yeux de grenouille");
        o.rentrer("4 larmes de brume funèbre");
        o.rentrer("2 gouttes de sang de citrouille");

        System.out.println("oeil de grenouille = " + o.quantite("oeil de grenouille")); // 3
        System.out.println("yeux de grenouille = " + o.quantite("yeux de grenouille")); // 3

        int fait = o.preparer("2 fioles de glaires purulentes");
        System.out.println("Préparées: " + fait); // selon stocks, 2 si possible
        System.out.println("Stock fioles = " + o.quantite("fiole de glaires purulentes"));

        // check stocks restants
        System.out.println("larmes de brume funèbre = " + o.quantite("larmes de brume funèbre"));
        System.out.println("gouttes de sang de citrouille = " + o.quantite("gouttes de sang de citrouille"));
        System.out.println("oeil de grenouille = " + o.quantite("oeil de grenouille"));
    }
}
