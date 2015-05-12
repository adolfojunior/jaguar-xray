package br.com.cubekode.jaguar.xray.json;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

public class JsonData implements CharSequence {

    private StringBuilder buffer;

    public JsonData(int initialCapacity) {
        this.buffer = new StringBuilder(initialCapacity);
    }

    public JsonData() {
        this(1024);
    }

    public JsonData clear() {

        buffer.setLength(0);

        return this;
    }

    public JsonData write(CharSequence str) {

        buffer.append(str);

        return this;
    }

    public JsonData openObj() {
        return write("{");
    }

    public JsonData closeObj() {
        return write("}");
    }

    public JsonData openArray() {
        return write("[");
    }

    public JsonData closeArray() {
        return write("]");
    }

    public JsonData sep() {
        return write(",");
    }

    public JsonData nullValue() {
        return write("null");
    }

    public JsonData newline() {
        return write("\n");
    }

    public JsonData string(CharSequence str) {
        return write("\"").write(str).write("\"");
    }

    public JsonData value(Object val) {

        if (val == null) {

            nullValue();

        } else if (val instanceof CharSequence) {

            string((CharSequence) val);

        } else if (val instanceof Number) {

            write(val.toString());

        } else if (val instanceof Map) {

            object((Map<?, ?>) val);

        } else if (val instanceof Collection) {

            array((Collection<?>) val);

        } else {

            string(val.toString());
        }
        return this;
    }

    public JsonData object(Map<?, ?> map) {

        int properties = 0;

        openObj();

        for (Entry<?, ?> entry : map.entrySet()) {

            if (properties++ > 0) {
                sep();
            }

            string(entry.getKey().toString()).write(":").value(entry.getValue());
        }

        return closeObj();
    }

    public JsonData array(Collection<?> collection) {

        int properties = 0;

        openArray();

        for (Object item : collection) {

            if (properties++ > 0) {
                sep();
            }
            value(item);
        }

        return closeArray();
    }

    @Override
    public String toString() {
        return buffer.toString();
    }

    @Override
    public int length() {
        return buffer.length();
    }

    @Override
    public char charAt(int index) {
        return buffer.charAt(index);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return buffer.subSequence(start, end);
    }
}
