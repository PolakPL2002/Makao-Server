package tech.kucharski.makao.util;

import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link Utilities}.
 */
public class UtilitiesTest {

    /**
     * Test for {@link Utilities#appendToArray(Class, Object[], Object)}.
     */
    @Test
    public void appendToArray() {
        assertArrayEquals(new Integer[]{1234}, Utilities.appendToArray(Integer.class, new Integer[0], 1234));
        assertArrayEquals(new Integer[]{1234, 5678}, Utilities.appendToArray(Integer.class, new Integer[]{1234}, 5678));
        assertArrayEquals(new Integer[]{1234, null}, Utilities.appendToArray(Integer.class, new Integer[]{1234}, null));
    }

    /**
     * Test for {@link Utilities#boundInt(int, int)}.
     */
    @Test
    public void boundInt() {
        assertEquals(0, Utilities.boundInt(123, 1));
        assertEquals(0, Utilities.boundInt(-123, 1));
        assertEquals(0, Utilities.boundInt(-123, 123));
        assertEquals(122, Utilities.boundInt(-1, 123));
        assertEquals(0, Utilities.boundInt(-123, 123));
        assertEquals(6, Utilities.boundInt(42, 9));
        assertEquals(3, Utilities.boundInt(-42, 9));
    }

    /**
     * Test for {@link Utilities#equalsIgnoreWhitespaces(String, String)}.
     */
    @Test
    public void equalsIgnoreWhitespaces() {
        assertTrue(Utilities.equalsIgnoreWhitespaces(" ", ""));
        assertTrue(Utilities.equalsIgnoreWhitespaces("", " "));
        assertTrue(Utilities.equalsIgnoreWhitespaces("", ""));
        assertTrue(Utilities.equalsIgnoreWhitespaces("abc", "abc"));
        assertTrue(Utilities.equalsIgnoreWhitespaces("a b c", "abc"));
        assertTrue(Utilities.equalsIgnoreWhitespaces("a b c", "a b c"));
        assertTrue(Utilities.equalsIgnoreWhitespaces("abc", "a b c"));
        assertFalse(Utilities.equalsIgnoreWhitespaces("abc", ""));
        assertFalse(Utilities.equalsIgnoreWhitespaces("", "abc"));
        assertFalse(Utilities.equalsIgnoreWhitespaces("abcd", "abc"));
        assertFalse(Utilities.equalsIgnoreWhitespaces("abc d", "abc"));
        assertFalse(Utilities.equalsIgnoreWhitespaces("abc", "abc d"));
    }

    /**
     * Test for {@link Utilities#escapeFilename(String)}.
     */
    @Test
    public void escapeFilename() {
        assertEquals("____________test1234", Utilities.escapeFilename("<*.#$>:\"/\\|?test1234      \t\t \0 \0 "));
    }

    /**
     * Test for {@link Utilities#getMethodIdentifier(int)}.
     */
    @Test
    public void getMethodIdentifier() {
        assertTrue(Utilities.getMethodIdentifier(0).startsWith("tech.kucharski.makao.util.UtilitiesTest#getMethodIdentifier:"));
    }

    /**
     * Test for {@link Utilities#removeFromMapByValue(Map, Object)}.
     */
    @Test
    public void removeFromMapByValue() {
        final Map<String, Object> map = new HashMap<>();
        final Object a = new Object(), b = new Object(), c = new Object();
        //noinspection ConstantConditions
        assertEquals(0, map.size());
        Utilities.removeFromMapByValue(map, a);
        assertEquals(0, map.size());
        Utilities.removeFromMapByValue(map, b);
        assertEquals(0, map.size());
        map.put("a", a);
        map.put("b", b);
        map.put("c", c);
        assertEquals(3, map.size());
        Utilities.removeFromMapByValue(map, b);
        assertEquals(2, map.size());
        assertFalse(map.containsKey("b"));
        Utilities.removeFromMapByValue(map, b);
        assertEquals(2, map.size());
    }

    /**
     * Test for {@link Utilities#splitQuery(URI)}.
     */
    @Test
    public void splitQuery() {
        try {
            URI uri = new URI("https://test.kucharski.tech/");
            assertEquals(0, Utilities.splitQuery(uri).size());
            uri = new URI("https://test.kucharski.tech/?a");
            Map<String, String> map = Utilities.splitQuery(uri);
            assertEquals(1, map.size());
            assertTrue(map.containsKey("a"));
            assertEquals("", map.get("a"));
            uri = new URI("https://test.kucharski.tech/?a=b%20c");
            map = Utilities.splitQuery(uri);
            assertEquals(1, map.size());
            assertTrue(map.containsKey("a"));
            assertEquals("b c", map.get("a"));
        } catch (URISyntaxException e) {
            fail(e);
        }
    }
}