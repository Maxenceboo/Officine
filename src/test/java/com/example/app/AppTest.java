package com.example.app;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AppTest {

    @Test
    void sum_addsTwoNumbers() {
        App app = new App();
        assertEquals(5, app.sum(2, 3));
        assertEquals(0, app.sum(-2, 2));
    }
}

