package de.ovgu.featureide.code.odoo.util;

import com.owlike.genson.Genson;
import com.owlike.genson.GensonBuilder;
import com.owlike.genson.JsonBindingException;
import com.owlike.genson.reflect.VisibilityFilter;
import com.owlike.genson.stream.JsonStreamException;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;

/**
 * Util class for a global json persistence based on Genson.
 */
public class Json {

    // Not indented to instantiate
    private Json() {
    }

    private static final Genson MARHSALLER = new GensonBuilder()
            // Pretty formatting
            .useIndentation(true)
            .useConstructorWithArguments(true)
            .useFields(true, VisibilityFilter.PRIVATE)
            .useMethods(false, VisibilityFilter.ALL)
            .create();

    /**
     * @return A pre-configured instance of {@link Genson} to de-/serialize objects. Their classes needn't to have a public
     * non-args constructor or have public methods to access the fields.
     * Because {@link Genson} is already thread-safe, you can use it without synchronization.
     * @see Genson
     */
    public static Genson getGenson() {
        return MARHSALLER;
    }

    /**
     * Simple wrapper to serialize an object to a file containing its JSON representation.
     *
     * @param file   The file. Must exists and must not be null.
     * @param object The object. Can be null, but not indented.
     * @throws IOException          Can't write to file. Write permission or file is open.
     * @throws JsonBindingException Something went wrong while serializing. See {@link Genson#serialize(Object, Writer)}
     * @throws JsonStreamException  Something went wrong while serializing. See {@link Genson#serialize(Object, Writer)}
     */
    public static void serializeToFile(File file, Object object)
            throws IOException, JsonBindingException, JsonStreamException {
        if (file == null) {
            throw new NullPointerException("File cannot be null");
        }
        try (Writer writer = Files.newBufferedWriter(file.toPath())) {
            MARHSALLER.serialize(object, writer);
        }
    }

    /**
     * Simple wrapper to deserialize an object from a file containing its JSON representation.
     *
     * @param file   The file. Must exist and must not be null.
     * @param toClass The class of the object object. Must not be null.
     * @throws IOException          Can't write to file. Write permission or file is open.
     * @throws JsonBindingException Something went wrong while serializing. See {@link Genson#serialize(Object, Writer)}
     * @throws JsonStreamException  Something went wrong while serializing. See {@link Genson#serialize(Object, Writer)}
     */
    public static <T> T deserializeFromFile(File file, Class<? extends T> toClass)
            throws IOException, JsonBindingException, JsonStreamException {
        if (file == null) {
            throw new NullPointerException("file cannot be null!");
        }
        if (toClass == null) {
            throw new NullPointerException("targetClass cannot be null!");
        }

        try (Reader reader = Files.newBufferedReader(file.toPath())) {
            return MARHSALLER.deserialize(reader, toClass);
        }
    }
}
