package com.pedroaguilar.andarivel;

import android.util.JsonWriter;

import com.google.firebase.firestore.auth.User;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;

public class Json implements Cloneable, Override {
    //TODO:crear metodo para obtener un archivo json con los datos del usuario
    public void writeJsonStream(OutputStream out, List<Usuario> listaUsuarios) throws IOException {
        JsonWriter writer = new JsonWriter(new OutputStreamWriter(out, "UTF-8"));
        writer.setIndent("  ");
        writeUserArray(writer, listaUsuarios);
        writer.close();
    }

    public void writeUserArray(JsonWriter writer, List<Usuario> listaUsuarios) throws IOException {
        writer.beginArray();
        for (Usuario user : listaUsuarios) {
            writeUser(writer, user);
        }
        writer.endArray();
    }

    public void writeUser(JsonWriter writer, Usuario user) throws IOException {
        writer.beginObject();
        writer.name("id").value(user.getID());
        writer.name("nombre").value(user.getNombre());
        writer.name("nombre").value(user.getApellidos());
        writer.name("nombre").value(user.getDireccion());
        writer.name("nombre").value(user.getEmail());
        writer.name("nombre").value(user.getNombreUsuario());
        writer.name("nombre").value(user.getPassword());
        writer.name("user");
       // writeUser(writer, user.getUser());
        writer.endObject();
    }

    /*public void writeUser(JsonWriter writer, User user) throws IOException {
        writer.beginObject();
        writer.name("name").value(user.getName());
        writer.name("followers_count").value(user.getFollowersCount());
        writer.endObject();
    }*/

    public void writeDoublesArray(JsonWriter writer, List<Double> doubles) throws IOException {
        writer.beginArray();
        for (Double value : doubles) {
            writer.value(value);
        }
        writer.endArray();
    }

    /**
     * Returns true if the specified object represents an annotation
     * that is logically equivalent to this one.  In other words,
     * returns true if the specified object is an instance of the same
     * annotation type as this instance, all of whose members are equal
     * to the corresponding member of this annotation, as defined below:
     * <ul>
     *    <li>Two corresponding primitive typed members whose values are
     *    {@code x} and {@code y} are considered equal if {@code x == y},
     *    unless their type is {@code float} or {@code double}.
     *
     *    <li>Two corresponding {@code float} members whose values
     *    are {@code x} and {@code y} are considered equal if
     *    {@code Float.valueOf(x).equals(Float.valueOf(y))}.
     *    (Unlike the {@code ==} operator, NaN is considered equal
     *    to itself, and {@code 0.0f} unequal to {@code -0.0f}.)
     *
     *    <li>Two corresponding {@code double} members whose values
     *    are {@code x} and {@code y} are considered equal if
     *    {@code Double.valueOf(x).equals(Double.valueOf(y))}.
     *    (Unlike the {@code ==} operator, NaN is considered equal
     *    to itself, and {@code 0.0} unequal to {@code -0.0}.)
     *
     *    <li>Two corresponding {@code String}, {@code Class}, enum, or
     *    annotation typed members whose values are {@code x} and {@code y}
     *    are considered equal if {@code x.equals(y)}.  (Note that this
     *    definition is recursive for annotation typed members.)
     *
     *    <li>Two corresponding array typed members {@code x} and {@code y}
     *    are considered equal if {@code Arrays.equals(x, y)}, for the
     *    appropriate overloading of {@link Arrays#equals}.
     * </ul>
     *
     * @param obj
     * @return true if the specified object represents an annotation
     * that is logically equivalent to this one, otherwise false
     */
    @Override
    public boolean equals(Object obj) {
        return false;
    }

    /**
     * Returns the hash code of this annotation, as defined below:
     *
     * <p>The hash code of an annotation is the sum of the hash codes
     * of its members (including those with default values), as defined
     * below:
     * <p>
     * The hash code of an annotation member is (127 times the hash code
     * of the member-name as computed by {@link String#hashCode()}) XOR
     * the hash code of the member-value, as defined below:
     *
     * <p>The hash code of a member-value depends on its type:
     * <ul>
     * <li>The hash code of a primitive value <i>{@code v}</i> is equal to
     *     <code><i>WrapperType</i>.valueOf(<i>v</i>).hashCode()</code>, where
     *     <i>{@code WrapperType}</i> is the wrapper type corresponding
     *     to the primitive type of <i>{@code v}</i> ({@link Byte},
     *     {@link Character}, {@link Double}, {@link Float}, {@link Integer},
     *     {@link Long}, {@link Short}, or {@link Boolean}).
     *
     * <li>The hash code of a string, enum, class, or annotation member-value
     * I     <i>{@code v}</i> is computed as by calling
     *     <code><i>v</i>.hashCode()</code>.  (In the case of annotation
     *     member values, this is a recursive definition.)
     *
     * <li>The hash code of an array member-value is computed by calling
     *     the appropriate overloading of
     *     {@link Arrays#hashCode(long[]) Arrays.hashCode}
     *     on the value.  (There is one overloading for each primitive
     *     type, and one for object reference types.)
     * </ul>
     *
     * @return the hash code of this annotation
     */
    @Override
    public int hashCode() {
        return 0;
    }

    /**
     * Returns a string representation of this annotation.  The details
     * of the representation are implementation-dependent, but the following
     * may be regarded as typical:
     * <pre>
     *   &#064;com.acme.util.Name(first=Alfred, middle=E., last=Neuman)
     * </pre>
     *
     * @return a string representation of this annotation
     */
    @Override
    public String toString() {
        return null;
    }

    /**
     * Returns the annotation type of this annotation.
     *
     * @return the annotation type of this annotation
     */
    @Override
    public Class<? extends Annotation> annotationType() {
        return null;
    }
}
