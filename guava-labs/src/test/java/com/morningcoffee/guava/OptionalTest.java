package com.morningcoffee.guava;


import com.google.common.base.Optional;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * Exploring Optional type that fight null-driven development designs
 *
 * @see <a href="https://code.google.com/p/guava-libraries/wiki/UsingAndAvoidingNullExplained">Guava Guide Article</a>
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
     * When you call Optional#get value must always be present
     * @throws Exception
     */
    @Test(expected = IllegalStateException.class)
    public void testOptionalAbsent() throws Exception {
        final Optional<?> absent = Optional.absent();

        absent.get();
    }

    /**
     * Optional#fromNullable factory method allows you to create optionals from from possibly null references. Still
     * you are forced to check for existence of value with Optional#isPresent before calling Optional#get
     * @throws Exception
     */
    @Test
    public void testOptionalFromNullable() throws Exception {
        final Optional<?> optionalNull = Optional.fromNullable(null);
        assertFalse(optionalNull.isPresent());

        final Optional<Object> optionalNotNull = Optional.fromNullable(new Object());
        assertTrue(optionalNotNull.isPresent());
        optionalNotNull.get();

    }


    /**
     * When specifying argument for Optional#or its value must always be defined otherwise NPE is thrown
     * @throws Exception
     */
    @Test(expected = NullPointerException.class)
    public void testOptionalOr1() throws Exception {
        final Optional<Object> optionalNull = Optional.fromNullable(null);

        final Object defaultValue = optionalNull.or("default");
        assertTrue(defaultValue == "default");
        assertTrue(defaultValue.equals("default"));

        optionalNull.or((Object)null); // throws NPE
    }

    /**
     * Optional#or can also accept optionals as argument
     * @throws Exception
     */
    @Test
    public void testOptionalOr2() throws Exception {
        final Optional<Object> optionalNull = Optional.fromNullable(null);
        final Optional<?> reference = Optional.of("reference");

        final Optional<?> defaultValue = optionalNull.or(reference);
        assertTrue(defaultValue == reference);
        assertTrue(defaultValue.isPresent());
        assertEquals(defaultValue.get(), "reference");
    }

    /**
     * If null optional is passed to Optional#or method than it's OK, no exception is thrown
     * @throws Exception
     */
    @Test
    public void testOptionalOr3() throws Exception {
        final Optional<Object> optionalNull = Optional.fromNullable(null);
        final Optional<Object> defaultOptional = Optional.fromNullable(null);
        final Optional<Object> resultingDefaultOptional = optionalNull.or(defaultOptional);

        assertTrue(defaultOptional == resultingDefaultOptional);
        assertFalse(resultingDefaultOptional.isPresent());
    }
}
