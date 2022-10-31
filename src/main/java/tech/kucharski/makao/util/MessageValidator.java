package tech.kucharski.makao.util;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * A validator for the message.
 */
public class MessageValidator {
    /**
     * A list of requirements for the options.
     */
    private final List<Requirement> requirements = new ArrayList<>();

    /**
     * Adds an array requirement.
     *
     * @param key      Name of the option.
     * @param nullable Whether option can be null.
     * @return Self for chaining.
     */
    public MessageValidator requireArray(@NotNull String key, boolean nullable) {
        requirements.add(new ArrayRequirement(key, nullable));
        return this;
    }

    /**
     * Adds an array requirement.
     *
     * @param key      Name of the option.
     * @param nullable Whether option can be null.
     * @return Self for chaining.
     */
    public MessageValidator requireObject(@NotNull String key, boolean nullable) {
        requirements.add(new ObjectRequirement(key, nullable));
        return this;
    }

    /**
     * Adds an array requirement.
     *
     * @param key      Name of the option.
     * @param nullable Whether option can be null.
     * @return Self for chaining.
     */
    public MessageValidator requirePrimitive(@NotNull String key, boolean nullable) {
        requirements.add(new PrimitiveRequirement(key, nullable));
        return this;
    }

    /**
     * Validates a message using added requirements.
     *
     * @param message Message to be validated.
     * @return Whether validation passed.
     */
    @Contract("null -> false")
    public boolean validate(@Nullable JsonObject message) {
        if (message == null) {
            return false;
        }
        for (Requirement requirement : requirements) {
            if (!requirement.validate(message)) {
                return false;
            }
        }
        return true;
    }

    private interface Requirement {
        /**
         * Validates a message.
         *
         * @param message Message to be validated.
         * @return Whether validation passed.
         */
        boolean validate(@Nullable JsonObject message);
    }

    private record ArrayRequirement(@NotNull String key, boolean nullable) implements Requirement {
        @Override
        public boolean validate(@Nullable JsonObject message) {
            if (message == null) {
                return false;
            }
            return message.has(key) && (message.get(key).isJsonArray() || (nullable && message.get(key).isJsonNull()));
        }
    }

    private record ObjectRequirement(@NotNull String key, boolean nullable) implements Requirement {
        @Override
        public boolean validate(@Nullable JsonObject message) {
            if (message == null) {
                return false;
            }
            return message.has(key) && (message.get(key).isJsonObject() || (nullable && message.get(key).isJsonNull()));
        }
    }

    private record PrimitiveRequirement(@NotNull String key, boolean nullable) implements Requirement {
        @Override
        public boolean validate(@Nullable JsonObject message) {
            if (message == null) {
                return false;
            }
            return message.has(key) && (message.get(key).isJsonPrimitive() || (nullable && message.get(key).isJsonNull()));
        }
    }
}
