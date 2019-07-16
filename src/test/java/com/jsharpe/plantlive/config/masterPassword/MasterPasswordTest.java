package com.jsharpe.plantlive.config.masterPassword;

import com.jsharpe.plantlive.UnitTest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(UnitTest.class)
public class MasterPasswordTest {

    private final static String MASTER_PASSWORD_ENV_KEY = "MASTER_PASSWORD";

    @Test
    public void testNopImplementation() {
        // Given
        final MasterPasswordCheck masterPasswordCheck = new NopMasterPasswordCheck();

        // When
        final boolean matches = masterPasswordCheck.matches("anything");

        // Then
        Assert.assertFalse(matches);
    }

    @Test
    public void testDirectImplementationMatches() {
        // Given
        final String masterPassword = "woops3rfj3cjif";
        final MasterPasswordCheck masterPasswordCheck = new DirectMasterPasswordCheck(masterPassword);

        // When
        final boolean matches = masterPasswordCheck.matches(masterPassword);

        // Then
        Assert.assertTrue(matches);
    }

    @Test
    public void testDirectImplementationNotMatches() {
        // Given
        final String masterPassword = "woops3rfj3cjif";
        final String masterPasswordCandidate = "woops3rfj3cjifdscsf";
        final MasterPasswordCheck masterPasswordCheck = new DirectMasterPasswordCheck(masterPassword);

        // When
        final boolean matches = masterPasswordCheck.matches(masterPasswordCandidate);

        // Then
        Assert.assertFalse(matches);
    }

    @Test(expected = RuntimeException.class)
    public void testEnvImplementationFails() {
        // Given
        new EnvMasterPasswordCheck("NO_ENV_VARIABLE_HERE");
    }

    @Test
    public void testEnvImplementationMatches() {
        // Given
        final String masterPasswordCandidate = "test_master_password";
        final MasterPasswordCheck masterPasswordCheck = new EnvMasterPasswordCheck(MASTER_PASSWORD_ENV_KEY);

        // When
        final boolean matches = masterPasswordCheck.matches(masterPasswordCandidate);

        // Then
        Assert.assertTrue(matches);
    }

    @Test
    public void testEnvImplementationNotMatches() {
        // Given
        final String masterPasswordCandidate = "woops3rfj3cjifdscsf";
        final MasterPasswordCheck masterPasswordCheck = new EnvMasterPasswordCheck(MASTER_PASSWORD_ENV_KEY);

        // When
        final boolean matches = masterPasswordCheck.matches(masterPasswordCandidate);

        // Then
        Assert.assertFalse(matches);
    }

}
