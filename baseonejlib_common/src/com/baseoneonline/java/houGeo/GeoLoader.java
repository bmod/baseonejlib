package com.baseoneonline.java.houGeo;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StreamTokenizer;
import java.util.HashMap;

public class GeoLoader {
	public static void main(final String[] args) throws IOException {
		new GeoLoader().load("testSection0.hip.geo");
	}

	public GeoLoader() {
	}

	public Geo load(final String resource) {
		try {
			return load(getClass().getClassLoader().getResourceAsStream(
					resource));
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}

	private Geo load(final InputStream is) throws IOException {
		final StreamTokenizer tk = new StreamTokenizer(
				new InputStreamReader(is));

		final Geo geo = readGeometry(tk);
		is.close();
		return geo;
	}

	private Geo readGeometry(final StreamTokenizer tk) throws IOException {
		final Geo geo = new Geo();

		expect(tk, "PGEOMETRY");
		tk.nextToken(); // V5
		expect(tk, "NPoints");
		final int nPoints = readInt(tk);
		expect(tk, "NPrims");
		final int nPrims = readInt(tk);
		expect(tk, "NPointGroups");
		final int nPointGroups = readInt(tk);
		expect(tk, "NPrimGroups");
		final int nPrimGroups = readInt(tk);
		expect(tk, "NPointAttrib");
		final int nPointAttrib = readInt(tk);
		expect(tk, "NVertexAttrib");
		final int nVertexAttrib = readInt(tk);
		expect(tk, "NPrimAttrib");
		final int nPrimAttrib = readInt(tk);
		expect(tk, "NAttrib");
		final int nAttrib = readInt(tk);

		// Point Attributes
		if (nPointAttrib > 0) {
			expect(tk, "PointAttrib");
			geo.pointAttributes = new Attribute<?>[nPointAttrib];
			for (int i = 0; i < nPointAttrib; i++) {
				geo.pointAttributes[i] = readAttribute(tk);
			}
		}

		// Points
		geo.points = new Point[nPoints];
		for (int i = 0; i < geo.points.length; i++) {
			final Point p = new Point();

			p.x = readDouble(tk);
			p.y = readDouble(tk);
			p.z = readDouble(tk);
			p.w = readDouble(tk);
			if (nPointAttrib > 0) {
				expect(tk, '(');

				for (final Attribute<?> at : geo.pointAttributes) {
					at.initValuesArray(nPoints);
					at.readValues(tk, i);
				}

				expect(tk, ')');
			}

			geo.points[i] = p;
		}

		// Prims
		tk.nextToken();
		if (tk.sval.equals("Run")) {
			tk.nextToken();
			final int count = (int) tk.nval;
			tk.nextToken();
			if ("NURBCurve".equals(tk.sval)) {
				geo.nurbCurves = new Nurbs[count];
				for (int i = 0; i < count; i++) {
					geo.nurbCurves[i] = readNurbCurve(tk);
				}
			}
		} else {
			throw new UnsupportedOperationException();
		}

		// Roll off
		expect(tk, "beginExtra");
		expect(tk, "endExtra");

		return geo;
	}

	private Nurbs readNurbCurve(final StreamTokenizer tk) throws IOException {
		final Nurbs cv = new Nurbs();
		cv.vertices = new int[readInt(tk)];

		// Open or closed
		final char open = readChar(tk);
		if (open == ':')
			cv.open = true;
		else if (open == '<')
			cv.open = false;
		else
			throw new UnexpectedTokenException(": or <", "" + open, tk.lineno());

		expect(tk, "Basis");
		cv.order = readInt(tk);
		String endCondition = readString(tk);
		if (endCondition.equals("end"))
			cv.clamped = true;
		else if (endCondition.equals("noend"))
			cv.clamped = false;
		else
			throw new UnexpectedTokenException("'end' or 'noend'",
					endCondition, tk.lineno());

		int nKnots = -1;
		if (!cv.clamped) {
			nKnots = cv.vertices.length + cv.order - 2;
		} else {
			if (cv.open)
				nKnots = cv.vertices.length - cv.order + 2;
			else
				nKnots = cv.vertices.length - cv.order + 3;
		}

		cv.knots = new double[nKnots];
		for (int i = 0; i < cv.knots.length; i++)
			cv.knots[i] = readDouble(tk);

		for (int i = 0; i < cv.vertices.length; i++)
			cv.vertices[i] = readInt(tk);

		return cv;
	}

	private Attribute<?> readAttribute(final StreamTokenizer tk)
			throws IOException {
		final String name = readString(tk);
		final int size = readInt(tk);
		final String type = readString(tk);

		final Attribute<?> at = createAttribute(type);
		at.name = name;
		at.size = size;
		at.type = type;
		at.readDefaultValues(tk);

		return at;
	}

	private Attribute<?> createAttribute(final String typeName) {
		final HashMap<String, Class<? extends Attribute>> atTypes = new HashMap<String, Class<? extends Attribute>>();
		atTypes.put("float", FloatAttribute.class);
		atTypes.put("vector", FloatAttribute.class);

		for (final String key : atTypes.keySet()) {
			if (key.equals(typeName)) {
				final Class<? extends Attribute> type = atTypes.get(key);
				try {
					return type.newInstance();
				} catch (final Exception e) {
					throw new RuntimeException(e);
				}
			}
		}
		throw new RuntimeException("No Attribute type for " + typeName);

	}

	private String readString(final StreamTokenizer tk) throws IOException {
		final char c = (char) tk.nextToken();
		if (tk.sval == null)
			throw new UnexpectedTokenException("String", "" + c, tk.lineno());
		return tk.sval;
	}

	private char readChar(final StreamTokenizer tk) throws IOException {
		return (char) tk.nextToken();
	}

	private double readDouble(final StreamTokenizer tk) throws IOException {
		tk.nextToken();
		if (tk.ttype != StreamTokenizer.TT_NUMBER)
			throw new UnexpectedTokenException("Number", tk.sval, tk.lineno());
		return tk.nval;
	}

	private int readInt(final StreamTokenizer tk) throws IOException {
		tk.nextToken();
		if (tk.ttype != StreamTokenizer.TT_NUMBER)
			throw new UnexpectedTokenException("Number", tk.sval, tk.lineno());
		return (int) tk.nval;
	}

	private void expect(final StreamTokenizer tk, final char c)
			throws IOException {
		final char r = (char) tk.nextToken();
		if (r != c)
			throw new UnexpectedTokenException("" + c, "" + r, tk.lineno());
	}

	private void expect(final StreamTokenizer tk, final String value)
			throws IOException {
		tk.nextToken();
		if (!tk.sval.equals(value))
			throw new UnexpectedTokenException(value, tk.sval, tk.lineno());

	}
}