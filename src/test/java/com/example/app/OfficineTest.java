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
}
