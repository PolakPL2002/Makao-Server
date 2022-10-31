package tech.kucharski.makao.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for {@link MessageValidator}.
 */
public class MessageValidatorTest {
    /**
     * Test for {@link MessageValidator#requireArray(String, boolean)}.
     */
    @Test
    public void requireArray() {
        final MessageValidator messageValidator = new MessageValidator();
        final JsonObject jsonObject = new JsonObject();
        assertTrue(messageValidator.validate(jsonObject));

        messageValidator.requireArray("test", false);
        assertFalse(messageValidator.validate(jsonObject));

        jsonObject.add("test", null);
        assertFalse(messageValidator.validate(jsonObject));

        jsonObject.remove("test");
        jsonObject.add("test", new JsonArray());
        assertTrue(messageValidator.validate(jsonObject));

        messageValidator.requireArray("test2", true);
        assertFalse(messageValidator.validate(jsonObject));

        jsonObject.add("test2", null);
        assertTrue(messageValidator.validate(jsonObject));

        jsonObject.remove("test2");
        jsonObject.add("test2", new JsonArray());
        assertTrue(messageValidator.validate(jsonObject));
    }

    /**
     * Test for {@link MessageValidator#requireObject(String, boolean)}.
     */
    @Test
    public void requireObject() {
        final MessageValidator messageValidator = new MessageValidator();
        final JsonObject jsonObject = new JsonObject();
        assertTrue(messageValidator.validate(jsonObject));

        messageValidator.requireObject("test", false);
        assertFalse(messageValidator.validate(jsonObject));

        jsonObject.add("test", null);
        assertFalse(messageValidator.validate(jsonObject));

        jsonObject.remove("test");
        jsonObject.add("test", new JsonObject());
        assertTrue(messageValidator.validate(jsonObject));

        messageValidator.requireObject("test2", true);
        assertFalse(messageValidator.validate(jsonObject));

        jsonObject.add("test2", null);
        assertTrue(messageValidator.validate(jsonObject));

        jsonObject.remove("test2");
        jsonObject.add("test2", new JsonObject());
        assertTrue(messageValidator.validate(jsonObject));
    }

    /**
     * Test for {@link MessageValidator#requirePrimitive(String, boolean)}.
     */
    @Test
    public void requirePrimitive() {
        final MessageValidator messageValidator = new MessageValidator();
        final JsonObject jsonObject = new JsonObject();
        assertTrue(messageValidator.validate(jsonObject));

        messageValidator.requirePrimitive("test", false);
        assertFalse(messageValidator.validate(jsonObject));

        jsonObject.add("test", null);
        assertFalse(messageValidator.validate(jsonObject));

        jsonObject.remove("test");
        jsonObject.addProperty("test", 42);
        assertTrue(messageValidator.validate(jsonObject));

        messageValidator.requirePrimitive("test2", true);
        assertFalse(messageValidator.validate(jsonObject));

        jsonObject.add("test2", null);
        assertTrue(messageValidator.validate(jsonObject));

        jsonObject.remove("test2");
        jsonObject.addProperty("test2", 42);
        assertTrue(messageValidator.validate(jsonObject));
    }

    /**
     * Test for {@link MessageValidator#validate(JsonObject)}.
     */
    @Test
    public void validate() {
        final MessageValidator messageValidator = new MessageValidator()
                .requirePrimitive("prim1", false)
                .requirePrimitive("prim2", true)
                .requireObject("obj1", false)
                .requireObject("obj2", true)
                .requireArray("arr1", false)
                .requireArray("arr2", true);
        assertFalse(messageValidator.validate(null));

        final JsonObject jsonObject = new JsonObject();
        assertFalse(messageValidator.validate(jsonObject));

        jsonObject.add("prim1", null);
        jsonObject.add("prim2", null);
        jsonObject.add("obj1", null);
        jsonObject.add("obj2", null);
        jsonObject.add("arr1", null);
        jsonObject.add("arr2", null);
        assertFalse(messageValidator.validate(jsonObject));

        jsonObject.remove("prim1");
        jsonObject.remove("obj1");
        jsonObject.remove("arr1");
        jsonObject.addProperty("prim1", 42);
        jsonObject.add("obj1", new JsonObject());
        jsonObject.add("arr1", new JsonArray());
        assertTrue(messageValidator.validate(jsonObject));

        jsonObject.remove("prim2");
        jsonObject.remove("obj2");
        jsonObject.remove("arr2");
        jsonObject.addProperty("prim2", 42);
        jsonObject.add("obj2", new JsonObject());
        jsonObject.add("arr2", new JsonArray());
        assertTrue(messageValidator.validate(jsonObject));
    }
}