package tech.kucharski.makao.util;

import com.google.gson.JsonObject;

/**
 * The object that can be converted to JSON
 */
public interface JSONConvertible {
    /**
     * @return JSON representation of the object.
     */
    JsonObject toJSONObject();
}
