package com.example.app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OfficineTest {

    private Officine o;

    @BeforeEach
    void setUp() {
        // Arrange
        o = new Officine();
    }

    @Test
    @DisplayName("quantite() d’un item non rentré retourne 0")
    void quantiteInconnue_retourneZero() {
        // Act
        int q = o.quantite("oeil de grenouille");

        // Assert
        assertEquals(0, q);
    }

    @Test
    @DisplayName("rentrer() ajoute la quantité au stock (cas simple)")
    void rentrer_ajouteAuStock() {
        // Act
        o.rentrer("3 oeil de grenouille");

        // Assert
        assertEquals(3, o.quantite("oeil de grenouille"));
    }

        @Test
    @DisplayName("preparer() fabrique la quantité demandée quand les stocks sont suffisants")
    void preparer_stocksSuffisants_produitTout() {
        // Arrange
        o.rentrer("4 larmes de brume funèbre");
        o.rentrer("2 gouttes de sang de citrouille");

        // Act
        int faits = o.preparer("2 fioles de glaires purulentes");

        // Assert
        assertEquals(2, faits);
        assertEquals(2, o.quantite("fiole de glaires purulentes"));
        assertEquals(0, o.quantite("goutte de sang de citrouille"));
        assertEquals(0, o.quantite("larme de brume funèbre"));
    }

    @Test
    @DisplayName("preparer() produit partiellement si les stocks sont insuffisants")
    void preparer_stocksPartiels_productionPartielle() {
        // Arrange
        o.rentrer("2 larmes de brume funèbre");
        o.rentrer("1 goutte de sang de citrouille");

        // Act
        int faits = o.preparer("2 fioles de glaires purulentes");

        // Assert
        assertEquals(1, faits);
        assertEquals(1, o.quantite("fiole de glaires purulentes"));
    }

    @Test
    @DisplayName("preparer() d’une potion sans recette retourne 0 et n’affecte pas le stock")
    void preparer_potionInconnue_retourneZero() {
        // Arrange
        o.rentrer("10 pincées de poudre de lune");
        int avant = o.quantite("pincée de poudre de lune");

        // Act
        int faits = o.preparer("3 nuages d'arcane");

        // Assert
        assertEquals(0, faits);
        assertEquals(avant, o.quantite("pincée de poudre de lune"));
    }

    @Test
    @DisplayName("Singulier/Pluriel: 'yeux' ≡ 'oeil' (ajout & lecture)")
    void pluriel_irregulier_yeux_oeil() {
        // Arrange
        o.rentrer("2 yeux de grenouille");

        // Act
        int qSingulier = o.quantite("oeil de grenouille");

        // Assert
        assertEquals(2, qSingulier);
    }

    @Test
    @DisplayName("Pluriel simple: on aligne sur le premier mot sans 's'")
    void pluriel_simple_premierMotSansS() {
        // Arrange
        o.rentrer("3 larmes de brume funèbre");

        // Act
        int q = o.quantite("larme de brume funèbre");

        // Assert
        assertEquals(3, q);
    }

    @Test
    @DisplayName("preparer() accepte les libellés pluriels")
    void preparer_avecLibellePluriel_ok() {
        // Arrange
        o.rentrer("2 larmes de brume funèbre");
        o.rentrer("1 goutte de sang de citrouille");

        // Act
        int faits = o.preparer("1 fioles de glaires purulentes");

        // Assert
        assertEquals(1, faits);
        assertEquals(1, o.quantite("fiole de glaires purulentes"));
    }

    @Test
    @DisplayName("CRUD: creer() ajoute un nouvel item et initialise la quantité")
    void crud_creer_ajouteItem() {
        boolean created = o.creer("feuille de mandragore", 5);

        assertTrue(created);
        assertEquals(5, o.quantite("feuille de mandragore"));
    }

    @Test
    @DisplayName("CRUD: creer() sur un item existant retourne false et laisse le stock intact")
    void crud_creer_itemExistant_retourneFalse() {
        int avant = o.quantite("oeil de grenouille");

        boolean created = o.creer("oeil de grenouille", 10);

        assertFalse(created);
        assertEquals(avant, o.quantite("oeil de grenouille"));
    }

    @Test
    @DisplayName("CRUD: mettreAJour() change la quantité d'un item existant")
    void crud_mettreAJour_modifieQuantite() {
        o.rentrer("2 crocs de troll");

        boolean updated = o.mettreAJour("croc de troll", 7);

        assertTrue(updated);
        assertEquals(7, o.quantite("croc de troll"));
    }

    @Test
    @DisplayName("CRUD: mettreAJour() sur un item manquant retourne false")
    void crud_mettreAJour_itemManquant_retourneFalse() {
        boolean updated = o.mettreAJour("pierre de lune", 3);

        assertFalse(updated);
    }

    @Test
    @DisplayName("CRUD: supprimer() retire l'item et désactive sa recette le cas échéant")
    void crud_supprimer_retireItemEtRecette() {
        // Act
        boolean deleted = o.supprimer("fiole de glaires purulentes");

        // Assert
        assertTrue(deleted);
        assertEquals(0, o.preparer("1 fiole de glaires purulentes")); // plus de recette
        assertFalse(o.mettreAJour("fiole de glaires purulentes", 3)); // plus dans le stock
    }

    // ==== Tests supplémentaires pour améliorer la couverture ====

    @Test
    @DisplayName("creer() et mettreAJour() lèvent IllegalArgumentException pour quantités négatives")
    void crud_negativeQuantities_throw() {
        assertThrows(IllegalArgumentException.class, () -> o.creer("mandragore", -1));
        assertThrows(IllegalArgumentException.class, () -> o.mettreAJour("oeil de grenouille", -5));
    }

    @Test
    @DisplayName("canonical() gère un seul mot (ex: 'yeux' -> 'oeil' et 'pomme' -> 'pomme')")
    void canonical_singleWord_behaviour() throws Exception {
        // Utilise les méthodes publiques pour valider le comportement sur mots simples
        o.rentrer("2 yeux");
        assertEquals(2, o.quantite("oeil"));

        o.rentrer("3 pommes");
        assertEquals(3, o.quantite("pomme"));
    }

    @Test
    @DisplayName("parseQty/parseName: étiquetage sans quantité fonctionne (défaut=1)")
    void parse_withoutQuantity_defaultsToOne() {
        o.rentrer("larme de brume funèbre"); // équivaut à 1
        assertEquals(1, o.quantite("larme de brume funèbre"));
    }

}
