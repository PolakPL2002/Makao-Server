package tech.kucharski.makao.util;

import com.google.common.io.ByteStreams;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.lang.reflect.Array;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static tech.kucharski.makao.util.Logger.debug;


/**
 * Utilities
 */
@SuppressWarnings("unused")
public class Utilities {
    /**
     * @param tClass Type class
     * @param array  Array to append to
     * @param object Object to be appended
     * @param <T>    Type
     * @return Appended array
     */
    @NotNull
    @Contract("_,_,_ -> new")
    public static <T> T[] appendToArray(Class<T> tClass, @NotNull T[] array, T object) {
        @SuppressWarnings("unchecked") T[] out = (T[]) Array.newInstance(tClass, array.length + 1);
        System.arraycopy(array, 0, out, 0, array.length);
        out[array.length] = object;
        return out;
    }

    /**
     * Loops x in y
     *
     * @param x x
     * @param y y
     * @return Better modulo
     */
    public static int boundInt(int x, int y) {
        int i = x % y;
        if (i < 0) i += y;
        return i;
    }

    /**
     * Compresses file to file.zip
     *
     * @param file File to be compressed
     * @throws IOException On IO error
     */
    public static void compressFile(@NotNull File file) throws IOException {
        String zipFileName = file.getPath().concat(".zip");

        FileOutputStream fos = new FileOutputStream(zipFileName);
        ZipOutputStream zos = new ZipOutputStream(fos);

        zos.putNextEntry(new ZipEntry(file.getName()));

        FileInputStream from = new FileInputStream(file);
        ByteStreams.copy(from, zos);
        from.close();
        zos.closeEntry();
        zos.close();
    }

    /**
     * @param s1 String 1
     * @param s2 String 2
     * @return Whether s1 equals s2 after removing whitespaces
     */
    public static boolean equalsIgnoreWhitespaces(String s1, String s2) {
        s1 = s1.replaceAll("\\s", "");
        s2 = s2.replaceAll("\\s", "");
        return s1.equals(s2);
    }

    /**
     * @param filename Filename to be escaped
     * @return Escaped filename
     */
    @NotNull
    public static String escapeFilename(@NotNull String filename) {
        filename = filename.replace("<", "_")
                .replace("*", "_")
                .replace(".", "_")
                .replace("#", "_")
                .replace("$", "_")
                .replace(">", "_")
                .replace(":", "_")
                .replace("\"", "_")
                .replace("/", "_")
                .replace("\\", "_")
                .replace("|", "_")
                .replace("?", "_");
        while (filename.endsWith(" ") || filename.endsWith("\t") || filename.endsWith("\0"))
            filename = filename.substring(0, filename.length() - 1);
        return filename;
    }

    /**
     * Puts exception stacktrace in String
     *
     * @param e Exception to be converted
     * @return String with stacktrace
     */
    @NotNull
    public static String exceptionToString(@NotNull Throwable e) {
        StringBuilder stackTrace = new StringBuilder();
        OutputStream os = new OutputStream() {
            @Override
            public void write(int b) {
                stackTrace.append((char) b);
            }
        };
        PrintStream ps = new PrintStream(os);
        e.printStackTrace(ps);
        return stackTrace.toString();
    }

    /**
     * Generates formatted caller signature
     *
     * @param depth 0 returns caller of this function, 1 caller of caller of this function etc.
     * @return Formatted caller signature
     */
    @NotNull
    public static String getMethodIdentifier(int depth) {
        StackTraceElement[] stackTrace = (new Exception()).getStackTrace();
        if (stackTrace.length < depth + 2) return "<Invalid stack>";
        StackTraceElement element = stackTrace[depth + 1];
        return (element.getClassName() + "#" + element.getMethodName() + ":" + element.getLineNumber());
    }

    /**
     * @param value Money value
     * @return Money string
     */
    @NotNull
    public static String makeMoney(long value) {
        boolean negative = value < 0;
        if (negative) value *= -1;
        String s = String.valueOf(value);
        int j = (3 - s.length() % 3) % 3;
        StringBuilder out = new StringBuilder();
        if (negative)
            out.append("-");
        for (int i = 0; i < s.length(); i++) {
            out.append(s.charAt(i));
            j++;
            if (j == 3 && i < s.length() - 1) {
                j = 0;
                out.append(",");
            }
        }
        return out.append("$").toString();
    }

    /**
     * Parses data from InputStream to byte array and String
     *
     * @param input InputStream with data
     * @return Processed data
     * @throws IOException Thrown on IO error
     */
    @NotNull
    @Contract("_ -> new")
    public static InputStreamData processInputStream(@NotNull InputStream input) throws IOException {
        debug("[Utilities] Processing input stream...");
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[65536];
        while ((nRead = input.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        buffer.flush();
        byte[] bytes = buffer.toByteArray();
        return new InputStreamData(bytes);
    }

    /**
     * Removes all keys with given value from map
     *
     * @param map   Map to be altered
     * @param value Value to be removed
     * @param <T>   Key type
     * @param <U>   Value type
     */
    public static <T, U> void removeFromMapByValue(@NotNull Map<T, U> map, U value) {
        T key = null;
        for (T k : map.keySet())
            if (map.get(k) == value) {
                key = k;
                break;
            }
        if (key != null) {
            map.remove(key);
            removeFromMapByValue(map, value);
        }
    }

    /**
     * @param url URL to split
     * @return Map with parameters
     */
    @NotNull
    public static Map<String, String> splitQuery(@NotNull URI url) {
        Map<String, String> query_pairs = new LinkedHashMap<>();
        String query = url.getQuery();
        if (query == null) return query_pairs;
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            query_pairs.put(URLDecoder.decode(pair.substring(0, idx), StandardCharsets.UTF_8), URLDecoder.decode(pair.substring(idx + 1), StandardCharsets.UTF_8));
        }
        return query_pairs;
    }

    /**
     * @param message Message to validate
     * @param keys    Keys to validate
     * @return Whether message contains all keys and they are primitives
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean validatePrimitiveOrNull(@NotNull JsonObject message, @NotNull String[] keys) {
        for (String key : keys)
            if (!message.has(key) || !(message.get(key).isJsonPrimitive() || message.get(key).isJsonNull()))
                return false;
        return true;
    }

    /**
     * @param message Message to validate
     * @param keys    Keys to validate
     * @return Whether message contains all keys and they are primitives
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean validatePrimitives(@NotNull JsonObject message, @NotNull String[] keys) {
        for (String key : keys)
            if (!message.has(key) || !message.get(key).isJsonPrimitive())
                return false;
        return true;
    }
}

