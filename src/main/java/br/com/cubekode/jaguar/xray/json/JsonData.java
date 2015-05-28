package br.com.cubekode.jaguar.xray.json;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

public class JsonData implements CharSequence {

	private static final char Q2 = '"';
	private static final char NL = '\n';
	private static final char SL = '\\';

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

	public JsonData openObj() {
		return write('{');
	}

	public JsonData closeObj() {
		return write('}');
	}

	public JsonData openArray() {
		return write('[');
	}

	public JsonData closeArray() {
		return write(']');
	}

	public JsonData sep() {
		return write(',');
	}

	public JsonData nullValue() {
		return write("null");
	}

	public JsonData newline() {
		return write(NL);
	}

	public JsonData value(Object val) {

		if (val == null) {

			nullValue();

		} else if (val instanceof CharSequence) {

			string((CharSequence) val);

		} else if (val instanceof Boolean || val instanceof Number) {

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

			string(entry.getKey().toString()).write(':').value(entry.getValue());
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

	public JsonData string(CharSequence str) {

		buffer.append(Q2);

		char ch = 0;
		int off = 0, len = str.length();
		// copy in blocks to use internal getChars, that use native arraycopy
		for (int i = 0; i < len; i++) {
			ch = str.charAt(i);
			if (ch == Q2 || ch == NL || ch == SL) {
				if (off < i) {
					buffer.append(str, off, i);
				}
				buffer.append(SL).append(ch);
				off = i + 1;
			}
		}

		if (off < len) {
			buffer.append(str, off, len);
		}

		buffer.append(Q2);

		return this;
	}

	protected JsonData write(char ch) {
		buffer.append(ch);
		return this;
	}

	protected JsonData write(CharSequence str) {
		buffer.append(str);
		return this;
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
