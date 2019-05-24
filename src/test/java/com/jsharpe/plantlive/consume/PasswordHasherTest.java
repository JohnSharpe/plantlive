package com.jsharpe.plantlive.consume;

import com.jsharpe.plantlive.exceptions.IllegalPasswordException;
import org.junit.Assert;
import org.junit.Test;

public class PasswordHasherTest {

    @Test(expected = IllegalPasswordException.class)
    public void testNullHash() throws IllegalPasswordException {
        // When
        PasswordHasher.hash(null);
    }

    @Test(expected = IllegalPasswordException.class)
    public void testEmptyHash() throws IllegalPasswordException {
        // Given
        final String password = "";

        // When
        PasswordHasher.hash(password);
    }

    @Test(expected = IllegalPasswordException.class)
    public void testBlankHash() throws IllegalPasswordException {
        // Given
        final String password = "\n   ";

        // When
        PasswordHasher.hash(password);
    }

    @Test
    public void testHash() throws IllegalPasswordException {
        // Given
        final String password = "Kqf4m4W)_TtHm.*>";
        final String expected = "346507bc83afb0a476c86fe44c07e31661925d3c201f0254f23ea850c34f3cd27ad7ec1060f74c22b0ec7b11c40555eff0fb38ed0c3cc97292626ef7d715a9bd";

        // When
        final String actual = PasswordHasher.hash(password);

        // Then
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testVerifyMatch() {
        // Given
        final String password = "U`!>+,?hV9_tDAGn";
        final String hashedPassword = "6f29a5bde74d5bc16094b94951e08b090601689b2135d721cfe6fcb54a1111cad50cf21d730c43284f2655b2844a29039b9a5308c9812b33ac861159806d8aae";

        // When
        final boolean match = PasswordHasher.verify(hashedPassword, password);

        // Then
        Assert.assertTrue(match);
    }

    @Test
    public void testVerifyDifferent() {
        // Given
        final String password = "z-A]fKF9\\z6,c[::";
        final String hashedPassword = "b6d90a546ab3db6cc226934793f15a5f6559e33d74e13a0a0bf9253d975704a21ebff7a30888ced3fb2c1312299c2c96f1f829ea25e28e7124407289c7ccd7d1";

        // When
        final boolean match = PasswordHasher.verify(hashedPassword, password);

        // Then
        Assert.assertFalse(match);
    }

}
