package com.morningcoffee.guava;


import com.google.common.base.Optional;
import junit.framework.Assert;
import org.junit.Test;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * TODO Document this type
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
     * Optional#toString does not return value.toString
     * @throws Exception
     */
    @Test
    public void testOptionalToString() throws Exception {
        final Optional<String> optionalString = Optional.of("string");
        assertFalse(optionalString.toString().equals("string"));

        System.out.println(optionalString.toString());
    }
}
