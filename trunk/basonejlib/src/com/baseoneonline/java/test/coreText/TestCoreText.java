package com.baseoneonline.java.test.coreText;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.baseoneonline.java.tools.FileUtils;

public class TestCoreText {

	public static void main(String[] args) {
		new TestCoreText();
	}

	public TestCoreText() {

		String file = "test.coreText";
		String data = FileUtils.readFile(file);
		parse(data, file);
		for (Element elm : elements) {
			System.out.println(elm);
		}
	}

	List<Element> elements = new ArrayList<Element>();

	private void parse(String data, String file) {

		StringTokenizer tkn = new StringTokenizer(data);

		ListElement elements = new ListElement();
		CoreTextElement core = new CoreTextElement(file, elements);
		while (tkn.hasMoreElements()) {
			Element elm = findElement(tkn);
			if (null != elm) {
				elements.add(elm);
			}
		}

		System.out.println(core.serialize(""));
	}

	private Element findElement(StringTokenizer tokenizer) {
		String token = tokenizer.nextToken();
		if (token.matches(Rgx.PAT_RTTI_ID)) {
			return new RTTIElement(token, findElement(tokenizer));
		} else if (token.matches(Rgx.PAT_BLOCK_OPEN)) {
			ListElement list = new ListElement();
			while(!token.matches(Rgx.PAT_BLOCK_CLOSE)) {
				tokenizer.nextElement();
				list.add(findElement(tokenizer));
			}
			return list;
		} else if (token.matches(Rgx.PAT_STRING_LITERAL)) {
			return new StringElement(token.substring(1, token.length() - 1));
		}
		return null;
	}

}

interface Element {
	public String serialize(String indent);
}

class KeyValueElement<K, V extends Element> implements Element {

	public K key;
	public V value;

	public KeyValueElement(K key, V value) {
		this.key = key;
		this.value = value;
	}

	@Override
	public String toString() {
		return key + ": " + value;
	}

	@Override
	public String serialize(String indent) {
		return indent + this;
	}

}

class StringElement implements Element {

	public String value;

	public StringElement(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}

	@Override
	public String serialize(String indent) {
		return indent + "\"" + value + "\"";
	}

}

class ListElement implements Element {
	public List<Element> list = new ArrayList<Element>();

	public ListElement() {

	}

	public void add(Element e) {
		list.add(e);
	}

	@Override
	public String serialize(String indent) {
		StringBuffer buf = new StringBuffer();
		for (Element e : list) {
			buf.append(indent + e.serialize(indent) + "\n");
		}
		return buf.toString();
	}

}

class CoreTextElement extends KeyValueElement<String, ListElement> {
	public CoreTextElement(String key, ListElement list) {
		super(key, list);
	}

	@Override
	public String serialize(String indent) {
		return value.serialize(indent);
	}
}

class RTTIElement extends KeyValueElement<String, Element> {

	public RTTIElement(String key, Element value) {
		super(key, value);
	}

}

interface Rgx {

	public String PAT_BLOCK_OPEN = "\\{";
	public String PAT_BLOCK_CLOSE = "\\}";
	public String PAT_STRING_LITERAL = "\"[^\"]*\"";
	public String PAT_RTTI_ID = "^!.*";
}
