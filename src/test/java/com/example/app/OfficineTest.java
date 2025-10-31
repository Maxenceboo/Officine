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

}
