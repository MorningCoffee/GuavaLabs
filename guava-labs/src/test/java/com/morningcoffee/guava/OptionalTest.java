package com.morningcoffee.guava;


import com.google.common.base.Optional;
import org.junit.Test;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * Exploring Optional type that fight null-driven development designs
 * Date: 3/23/13
 *
 * @author osklyarenko
 */
public class OptionalTest {
    /**
     * Optional#of doesn't allow null passed as a value. Fails fast with NPE
     * @throws Exception
     */
    @Test(expected = NullPointerException.class)
    public void testOptionalOf1() throws Exception {
        Optional.of(null);
    }

    /**
     * Optional#isPresent method tests if actual value is there
     * @throws Exception
     */
    @Test
    public void testOptionalOf2() throws Exception {

        final Optional<String> optionalString = Optional.of("string reference");

        assertTrue(optionalString.isPresent());
        assertTrue(optionalString.get().equals("string reference"));
    }

    /**
     * Optional#toString value is NOT equal to value.toString()
     * @throws Exception
     */
    @Test
    public void testOptionalToString() throws Exception {
        final Optional<String> optionalString = Optional.of("string");
        assertFalse(optionalString.toString().equals("string"));

        System.out.println(optionalString.toString());
    }

    /**
     * Optional#hashCode value is NOT equal to value.hashCode()
     * @throws Exception
     */
    @Test
    public void testOptionalHashCode() throws Exception {
        final Optional<String> optionalString = Optional.of("string");
        final Optional<Integer> optionalInteger = Optional.of(47);


        assertFalse(optionalString.hashCode() == optionalString.get().hashCode());
        assertFalse(optionalInteger.hashCode() == optionalInteger.get().hashCode());
        assertFalse(Optional.absent().hashCode() == 0);
    }

    /**
     * You can't call Optional#get on absent optionals - throws IllegalStateException.
     * When you call #get value must always be present
     * @throws Exception
     */
    @Test(expected = IllegalStateException.class)
    public void testOptionalAbsent() throws Exception {
        final Optional<?> absent = Optional.absent();

        absent.get();
    }
}
