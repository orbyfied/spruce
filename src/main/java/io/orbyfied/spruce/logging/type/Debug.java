package io.orbyfied.spruce.logging.type;

import io.orbyfied.spruce.util.color.TextFormat;
import org.w3c.dom.Text;

import java.util.Iterator;
import java.util.Map;

public class Debug {

    public static <T> Array<T> array(T... obj) {
        return (Array<T>) new Array<>(obj).compile();
    }

    public static <K, V> Dictionary<K, V> dictionary(Map<K, V> map) {
        return (Dictionary<K, V>) new Dictionary<>(map).compile();
    }

    public final static class Array<T> extends Struct<T> {
        T[] obj;

        public Array(T... obj) {
            this.obj = obj;
        }

        public T[] toArray() {
            return obj;
        }

        public T get(int i) {
            return obj[i];
        }

        @Override
        public String toString() {
            return toString(true);
        }

        public String toString(boolean ansi) {
            StringBuilder b = new StringBuilder();
            if (ansi) {
                b.append(TextFormat.DARK_GRAY_FG).append("[ ");
                int l = obj.length;
                for (int i = 0; i < l; i++) {
                    if (i != 0)
                        b.append(TextFormat.DARK_YELLOW_FG).append(", ");
                    Object o = obj[i];
                    if (o == null) b.append(TextFormat.DARK_AQUA_FG).append("null");
                    else {
                        boolean str = o instanceof String;
                        b.append(TextFormat.DARK_GREEN_FG);
                        if (str) b.append("\"");
                        b.append(o);
                        if (str) b.append("\"");
                    }
                }
                b.append(TextFormat.DARK_GRAY_FG).append(" ]");
            } else {
                b.append("[ ");
                int l = obj.length;
                for (int i = 0; i < l; i++) {
                    if (i != 0) b.append(", ");
                    Object o = obj[i];
                    if (o == null) b.append("null");
                    else if (o instanceof String) b.append("\"").append(o).append("\"");
                    else b.append(o);
                }
            }

            return b.toString();
        }

        @Override
        public Struct<T> compile() {
            return null;
        }
    }

    public final static class Dictionary<K, V> extends Struct<Map.Entry<K, V>> {

        final Map<K, V> map;

        public Dictionary(Map<K, V> map) {
            this.map = map;
        }

        public Map<K, V> toMap() {
            return map;
        }

        @Override
        public Struct<Map.Entry<K, V>> compile() {
            return null;
        }

        @Override
        public String toString(boolean ansi) {
            StringBuilder b = new StringBuilder();

            if (ansi) {
                b.append(TextFormat.DARK_GRAY_FG).append("{ ");
                map.forEach((k, v) -> {
                    b.append(TextFormat.DARK_GRAY_FG).append(k.toString());
                });
                b.append(TextFormat.DARK_GRAY_FG).append(" }");
            } else {

            }

            return b.toString();
        }

    }

    public abstract static class Struct<T> {
        public abstract Struct<T> compile();
        public abstract String    toString(boolean ansi);
    }

}
