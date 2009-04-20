/* XMLElement.java
 *
 * $Revision: 1.4 $
 * $Date: 2002/03/24 10:27:59 $
 * $Name: RELEASE_2_2_1 $
 *
 * This file is part of NanoXML 2 Lite.
 * Copyright (C) 2000-2002 Marc De Scheemaecker, All Rights Reserved.
 *
 * This software is provided 'as-is', without any express or implied warranty.
 * In no event will the authors be held liable for any damages arising from the
 * use of this software.
 *
 * Permission is granted to anyone to use this software for any purpose,
 * including commercial applications, and to alter it and redistribute it
 * freely, subject to the following restrictions:
 *
 *  1. The origin of this software must not be misrepresented; you must not
 *     claim that you wrote the original software. If you use this software in
 *     a product, an acknowledgment in the product documentation would be
 *     appreciated but is not required.
 *
 *  2. Altered source versions must be plainly marked as such, and must not be
 *     misrepresented as being the original software.
 *
 *  3. This notice may not be removed or altered from any source distribution.
 *****************************************************************************/

package com.baseoneonline.java.nanoxml;

import java.io.ByteArrayOutputStream;
import java.io.CharArrayReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

import com.jme.renderer.ColorRGBA;

/**
 * XMLElement is a representation of an XML object. The object is able to parse
 * XML code.
 * <P>
 * <DL>
 * <DT><B>Parsing XML Data</B></DT>
 * <DD>You can parse XML data using the following code:
 * <UL>
 * <CODE>
 * XMLElement xml = new XMLElement();<BR>
 * FileReader reader = new FileReader("filename.xml");<BR>
 * xml.parseFromReader(reader);
 * </CODE>
 * </UL>
 * </DD>
 * </DL>
 * <DL>
 * <DT><B>Retrieving Attributes</B></DT>
 * <DD>You can enumerate the attributes of an element using the method
 * {@link #enumerateAttributeNames() enumerateAttributeNames}. The attribute
 * values can be retrieved using the method
 * {@link #getStringAttribute(java.lang.String) getStringAttribute}. The
 * following example shows how to list the attributes of an element:
 * <UL>
 * <CODE>
 * XMLElement element = ...;<BR>
 * Enumeration enum = element.getAttributeNames();<BR>
 * while (enum.hasMoreElements()) {<BR>
 * &nbsp;&nbsp;&nbsp;&nbsp;String key = (String) enum.nextElement();<BR>
 * &nbsp;&nbsp;&nbsp;&nbsp;String value = element.getStringAttribute(key);<BR>
 * &nbsp;&nbsp;&nbsp;&nbsp;System.out.println(key + " = " + value);<BR>
 * }
 * </CODE>
 * </UL>
 * </DD>
 * </DL>
 * <DL>
 * <DT><B>Retrieving Child Elements</B></DT>
 * <DD>You can enumerate the children of an element using
 * {@link #enumerateChildren() enumerateChildren}. The number of child elements
 * can be retrieved using {@link #countChildren() countChildren}.</DD>
 * </DL>
 * <DL>
 * <DT><B>Elements Containing Character Data</B></DT>
 * <DD>If an elements contains character data, like in the following example:
 * <UL>
 * <CODE>
 * &lt;title&gt;The Title&lt;/title&gt;
 * </CODE>
 * </UL>
 * you can retrieve that data using the method {@link #getContent() getContent}.
 * </DD>
 * </DL>
 * <DL>
 * <DT><B>Subclassing XMLElement</B></DT>
 * <DD>When subclassing XMLElement, you need to override the method
 * {@link #createAnotherElement() createAnotherElement} which has to return a
 * new copy of the receiver.</DD>
 * </DL>
 * <P>
 * 
 * @see com.baseoneonline.java.nanoxml.XMLParseException
 * @author Marc De Scheemaecker &lt;<A
 *         href="mailto:cyberelf@mac.com">cyberelf@mac.com</A>&gt;
 * @version $Name: RELEASE_2_2_1 $, $Revision: 1.4 $
 */

public class XMLElement {

	public static String TRUE_VALUE = "true";
	public static String FALSE_VALUE = "false";

	/**
	 * Serialization serial version ID.
	 */
	static final long serialVersionUID = 6685035139346394777L;

	/**
	 * Major version of NanoXML. Classes with the same major and minor version
	 * are binary compatible. Classes with the same major version are source
	 * compatible. If the major version is different, you may need to modify the
	 * client source code.
	 * 
	 * @see com.baseoneonline.java.nanoxml.XMLElement#NANOXML_MINOR_VERSION
	 */
	public static final int NANOXML_MAJOR_VERSION = 2;

	/**
	 * Minor version of NanoXML. Classes with the same major and minor version
	 * are binary compatible. Classes with the same major version are source
	 * compatible. If the major version is different, you may need to modify the
	 * client source code.
	 * 
	 * @see com.baseoneonline.java.nanoxml.XMLElement#NANOXML_MAJOR_VERSION
	 */
	public static final int NANOXML_MINOR_VERSION = 2;

	/**
	 * The attributes given to the element.
	 * <dl>
	 * <dt><b>Invariants:</b></dt>
	 * <dd>
	 * <ul>
	 * <li>The field can be empty.
	 * <li>The field is never <code>null</code>.
	 * <li>The keys and the values are strings.
	 * </ul>
	 * </dd>
	 * </dl>
	 */
	private Hashtable<String, String> attributes;

	/**
	 * Child elements of the element.
	 * <dl>
	 * <dt><b>Invariants:</b></dt>
	 * <dd>
	 * <ul>
	 * <li>The field can be empty.
	 * <li>The field is never <code>null</code>.
	 * <li>The elements are instances of <code>XMLElement</code> or a subclass
	 * of <code>XMLElement</code>.
	 * </ul>
	 * </dd>
	 * </dl>
	 */
	private Vector<XMLElement> children;

	/**
	 * The name of the element.
	 * <dl>
	 * <dt><b>Invariants:</b></dt>
	 * <dd>
	 * <ul>
	 * <li>The field is <code>null</code> iff the element is not initialized by
	 * either parse or setName.
	 * <li>If the field is not <code>null</code>, it's not empty.
	 * <li>If the field is not <code>null</code>, it contains a valid XML
	 * identifier.
	 * </ul>
	 * </dd>
	 * </dl>
	 */
	private String name;

	/**
	 * The #PCDATA content of the object.
	 * <dl>
	 * <dt><b>Invariants:</b></dt>
	 * <dd>
	 * <ul>
	 * <li>The field is <code>null</code> iff the element is not a #PCDATA
	 * element.
	 * <li>The field can be any string, including the empty string.
	 * </ul>
	 * </dd>
	 * </dl>
	 */
	private String contents;

	/**
	 * Conversion table for &amp;...; entities. The keys are the entity names
	 * without the &amp; and ; delimiters.
	 * <dl>
	 * <dt><b>Invariants:</b></dt>
	 * <dd>
	 * <ul>
	 * <li>The field is never <code>null</code>.
	 * <li>The field always contains the following associations:
	 * "lt"&nbsp;=&gt;&nbsp;"&lt;", "gt"&nbsp;=&gt;&nbsp;"&gt;",
	 * "quot"&nbsp;=&gt;&nbsp;"\"", "apos"&nbsp;=&gt;&nbsp;"'",
	 * "amp"&nbsp;=&gt;&nbsp;"&amp;"
	 * <li>The keys are strings
	 * <li>The values are char arrays
	 * </ul>
	 * </dd>
	 * </dl>
	 */
	private final Hashtable<String, char[]> entities;

	/**
	 * The line number where the element starts.
	 * <dl>
	 * <dt><b>Invariants:</b></dt>
	 * <dd>
	 * <ul>
	 * <li><code>lineNr &gt= 0</code>
	 * </ul>
	 * </dd>
	 * </dl>
	 */
	private final int lineNr;

	/**
	 * <code>true</code> if the case of the element and attribute names are case
	 * insensitive.
	 */
	public boolean ignoreCase;

	/**
	 * <code>true</code> if the leading and trailing whitespace of #PCDATA
	 * sections have to be ignored.
	 */
	private final boolean ignoreWhitespace;

	/**
	 * Character read too much. This character provides push-back functionality
	 * to the input reader without having to use a PushbackReader. If there is
	 * no such character, this field is '\0'.
	 */
	private char charReadTooMuch;

	/**
	 * The reader provided by the caller of the parse method.
	 * <dl>
	 * <dt><b>Invariants:</b></dt>
	 * <dd>
	 * <ul>
	 * <li>The field is not <code>null</code> while the parse method is running.
	 * </ul>
	 * </dd>
	 * </dl>
	 */
	private Reader reader;

	/**
	 * The current line number in the source content.
	 * <dl>
	 * <dt><b>Invariants:</b></dt>
	 * <dd>
	 * <ul>
	 * <li>parserLineNr &gt; 0 while the parse method is running.
	 * </ul>
	 * </dd>
	 * </dl>
	 */
	private int parserLineNr;

	/**
	 * Creates and initializes a new XML element. Calling the construction is
	 * equivalent to:
	 * <ul>
	 * <code>new XMLElement(new Hashtable(), false, true)
     * </code>
	 * </ul>
	 * <dl>
	 * <dt><b>Postconditions:</b></dt>
	 * <dd>
	 * <ul>
	 * <li>countChildren() => 0
	 * <li>enumerateChildren() => empty enumeration
	 * <li>enumeratePropertyNames() => empty enumeration
	 * <li>getChildren() => empty vector
	 * <li>getContent() => ""
	 * <li>getLineNr() => 0
	 * <li>getName() => null
	 * </ul>
	 * </dd>
	 * </dl>
	 * 
	 * @see com.baseoneonline.java.nanoxml.XMLElement#XMLElement(java.util.Hashtable)
	 *      XMLElement(Hashtable)
	 * @see com.baseoneonline.java.nanoxml.XMLElement#XMLElement(boolean)
	 * @see com.baseoneonline.java.nanoxml.XMLElement#XMLElement(java.util.Hashtable,boolean)
	 *      XMLElement(Hashtable, boolean)
	 */
	public XMLElement() {
		this(new Hashtable<String, char[]>(), false, true, true);
	}

	public XMLElement(final String name) {
		this(new Hashtable<String, char[]>(), false, true, true);
		setName(name);
	}

	/**
	 * Creates and initializes a new XML element. Calling the construction is
	 * equivalent to:
	 * <ul>
	 * <code>new XMLElement(entities, false, true)
     * </code>
	 * </ul>
	 * 
	 * @param entities
	 *            The entity conversion table. </dl>
	 *            <dl>
	 *            <dt><b>Preconditions:</b></dt>
	 *            <dd>
	 *            <ul>
	 *            <li><code>entities != null</code>
	 *            </ul>
	 *            </dd>
	 *            </dl>
	 *            <dl>
	 *            <dt><b>Postconditions:</b></dt>
	 *            <dd>
	 *            <ul>
	 *            <li>countChildren() => 0
	 *            <li>enumerateChildren() => empty enumeration
	 *            <li>enumeratePropertyNames() => empty enumeration
	 *            <li>getChildren() => empty vector
	 *            <li>getContent() => ""
	 *            <li>getLineNr() => 0
	 *            <li>getName() => null
	 *            </ul>
	 *            </dd>
	 *            </dl>
	 *            <dl>
	 * @see com.baseoneonline.java.nanoxml.XMLElement#XMLElement()
	 * @see com.baseoneonline.java.nanoxml.XMLElement#XMLElement(boolean)
	 * @see com.baseoneonline.java.nanoxml.XMLElement#XMLElement(java.util.Hashtable,boolean)
	 *      XMLElement(Hashtable, boolean)
	 */
	public XMLElement(final Hashtable<String, char[]> entities) {
		this(entities, false, true, true);
	}

	/**
	 * Creates and initializes a new XML element. Calling the construction is
	 * equivalent to:
	 * <ul>
	 * <code>new XMLElement(new Hashtable(), skipLeadingWhitespace, true)
     * </code>
	 * </ul>
	 * 
	 * @param skipLeadingWhitespace
	 *            <code>true</code> if leading and trailing whitespace in PCDATA
	 *            content has to be removed. </dl>
	 *            <dl>
	 *            <dt><b>Postconditions:</b></dt>
	 *            <dd>
	 *            <ul>
	 *            <li>countChildren() => 0
	 *            <li>enumerateChildren() => empty enumeration
	 *            <li>enumeratePropertyNames() => empty enumeration
	 *            <li>getChildren() => empty vector
	 *            <li>getContent() => ""
	 *            <li>getLineNr() => 0
	 *            <li>getName() => null
	 *            </ul>
	 *            </dd>
	 *            </dl>
	 *            <dl>
	 * @see com.baseoneonline.java.nanoxml.XMLElement#XMLElement()
	 * @see com.baseoneonline.java.nanoxml.XMLElement#XMLElement(java.util.Hashtable)
	 *      XMLElement(Hashtable)
	 * @see com.baseoneonline.java.nanoxml.XMLElement#XMLElement(java.util.Hashtable,boolean)
	 *      XMLElement(Hashtable, boolean)
	 */
	public XMLElement(final boolean skipLeadingWhitespace) {
		this(new Hashtable<String, char[]>(), skipLeadingWhitespace, true, true);
	}

	/**
	 * Creates and initializes a new XML element. Calling the construction is
	 * equivalent to:
	 * <ul>
	 * <code>new XMLElement(entities, skipLeadingWhitespace, true)
     * </code>
	 * </ul>
	 * 
	 * @param entities
	 *            The entity conversion table.
	 * @param skipLeadingWhitespace
	 *            <code>true</code> if leading and trailing whitespace in PCDATA
	 *            content has to be removed. </dl>
	 *            <dl>
	 *            <dt><b>Preconditions:</b></dt>
	 *            <dd>
	 *            <ul>
	 *            <li><code>entities != null</code>
	 *            </ul>
	 *            </dd>
	 *            </dl>
	 *            <dl>
	 *            <dt><b>Postconditions:</b></dt>
	 *            <dd>
	 *            <ul>
	 *            <li>countChildren() => 0
	 *            <li>enumerateChildren() => empty enumeration
	 *            <li>enumeratePropertyNames() => empty enumeration
	 *            <li>getChildren() => empty vector
	 *            <li>getContent() => ""
	 *            <li>getLineNr() => 0
	 *            <li>getName() => null
	 *            </ul>
	 *            </dd>
	 *            </dl>
	 *            <dl>
	 * @see com.baseoneonline.java.nanoxml.XMLElement#XMLElement()
	 * @see com.baseoneonline.java.nanoxml.XMLElement#XMLElement(boolean)
	 * @see com.baseoneonline.java.nanoxml.XMLElement#XMLElement(java.util.Hashtable)
	 *      XMLElement(Hashtable)
	 */
	public XMLElement(final Hashtable<String, char[]> entities,
			final boolean skipLeadingWhitespace) {
		this(entities, skipLeadingWhitespace, true, true);
	}

	/**
	 * Creates and initializes a new XML element.
	 * 
	 * @param entities
	 *            The entity conversion table.
	 * @param skipLeadingWhitespace
	 *            <code>true</code> if leading and trailing whitespace in PCDATA
	 *            content has to be removed.
	 * @param ignoreCase
	 *            <code>true</code> if the case of element and attribute names
	 *            have to be ignored. </dl>
	 *            <dl>
	 *            <dt><b>Preconditions:</b></dt>
	 *            <dd>
	 *            <ul>
	 *            <li><code>entities != null</code>
	 *            </ul>
	 *            </dd>
	 *            </dl>
	 *            <dl>
	 *            <dt><b>Postconditions:</b></dt>
	 *            <dd>
	 *            <ul>
	 *            <li>countChildren() => 0
	 *            <li>enumerateChildren() => empty enumeration
	 *            <li>enumeratePropertyNames() => empty enumeration
	 *            <li>getChildren() => empty vector
	 *            <li>getContent() => ""
	 *            <li>getLineNr() => 0
	 *            <li>getName() => null
	 *            </ul>
	 *            </dd>
	 *            </dl>
	 *            <dl>
	 * @see com.baseoneonline.java.nanoxml.XMLElement#XMLElement()
	 * @see com.baseoneonline.java.nanoxml.XMLElement#XMLElement(boolean)
	 * @see com.baseoneonline.java.nanoxml.XMLElement#XMLElement(java.util.Hashtable)
	 *      XMLElement(Hashtable)
	 * @see com.baseoneonline.java.nanoxml.XMLElement#XMLElement(java.util.Hashtable,boolean)
	 *      XMLElement(Hashtable, boolean)
	 */
	public XMLElement(final Hashtable<String, char[]> entities,
			final boolean skipLeadingWhitespace, final boolean ignoreCase) {
		this(entities, skipLeadingWhitespace, true, ignoreCase);
	}

	/**
	 * Creates and initializes a new XML element.
	 * <P>
	 * This constructor should <I>only</I> be called from
	 * {@link #createAnotherElement() createAnotherElement} to create child
	 * elements.
	 * 
	 * @param entities
	 *            The entity conversion table.
	 * @param skipLeadingWhitespace
	 *            <code>true</code> if leading and trailing whitespace in PCDATA
	 *            content has to be removed.
	 * @param fillBasicConversionTable
	 *            <code>true</code> if the basic entities need to be added to
	 *            the entity list.
	 * @param ignoreCase
	 *            <code>true</code> if the case of element and attribute names
	 *            have to be ignored. </dl>
	 *            <dl>
	 *            <dt><b>Preconditions:</b></dt><dd>
	 *            <ul>
	 *            <li><code>entities != null</code> <li>if <code>
	 *            fillBasicConversionTable == false</code> then <code>entities
	 *            </code> contains at least the following entries: <code>amp
	 *            </code>, <code>lt</code>, <code>gt</code>, <code>apos</code>
	 *            and <code>quot</code>
	 *            </ul>
	 *            </dd>
	 *            </dl>
	 *            <dl>
	 *            <dt><b>Postconditions:</b></dt><dd>
	 *            <ul>
	 *            <li>countChildren() => 0 <li>enumerateChildren() => empty
	 *            enumeration <li>enumeratePropertyNames() => empty enumeration
	 *            <li>getChildren() => empty vector <li>getContent() => "" <li>
	 *            getLineNr() => 0 <li>getName() => null
	 *            </ul>
	 *            </dd>
	 *            </dl>
	 *            <dl>
	 * @see com.baseoneonline.java.nanoxml.XMLElement#createAnotherElement()
	 */
	protected XMLElement(final Hashtable<String, char[]> entities,
			final boolean skipLeadingWhitespace,
			final boolean fillBasicConversionTable, final boolean ignoreCase) {
		this.ignoreWhitespace = skipLeadingWhitespace;
		this.ignoreCase = ignoreCase;
		this.name = null;
		this.contents = "";
		this.attributes = new Hashtable<String, String>();
		this.children = new Vector<XMLElement>();
		this.entities = entities;
		this.lineNr = 0;
		final Enumeration<String> enu = this.entities.keys();
		while (enu.hasMoreElements()) {
			final String key = enu.nextElement();
			this.entities.put(key, this.entities.get(key));
		}
		if (fillBasicConversionTable) {
			this.entities.put("amp", new char[] { '&' });
			this.entities.put("quot", new char[] { '"' });
			this.entities.put("apos", new char[] { '\'' });
			this.entities.put("lt", new char[] { '<' });
			this.entities.put("gt", new char[] { '>' });
		}
	}

	/**
	 * Adds a child element.
	 * 
	 * @param child
	 *            The child element to add. </dl>
	 *            <dl>
	 *            <dt><b>Preconditions:</b></dt>
	 *            <dd>
	 *            <ul>
	 *            <li><code>child != null</code>
	 *            <li><code>child.getName() != null</code>
	 *            <li><code>child</code> does not have a parent element
	 *            </ul>
	 *            </dd>
	 *            </dl>
	 *            <dl>
	 *            <dt><b>Postconditions:</b></dt>
	 *            <dd>
	 *            <ul>
	 *            <li>countChildren() => old.countChildren() + 1
	 *            <li>enumerateChildren() => old.enumerateChildren() + child
	 *            <li>getChildren() => old.enumerateChildren() + child
	 *            </ul>
	 *            </dd>
	 *            </dl>
	 *            <dl>
	 * @see com.baseoneonline.java.nanoxml.XMLElement#countChildren()
	 * @see com.baseoneonline.java.nanoxml.XMLElement#enumerateChildren()
	 * @see com.baseoneonline.java.nanoxml.XMLElement#getChildren()
	 * @see com.baseoneonline.java.nanoxml.XMLElement#removeChild(com.baseoneonline.java.nanoxml.XMLElement)
	 *      removeChild(XMLElement)
	 */
	public void addChild(final XMLElement child) {
		this.children.add(child);
	}

	/**
	 * Adds or modifies an attribute.
	 * 
	 * @param name
	 *            The name of the attribute.
	 * @param value
	 *            The value of the attribute. </dl>
	 *            <dl>
	 *            <dt><b>Preconditions:</b></dt>
	 *            <dd>
	 *            <ul>
	 *            <li><code>name != null</code>
	 *            <li><code>name</code> is a valid XML identifier
	 *            <li><code>value != null</code>
	 *            </ul>
	 *            </dd>
	 *            </dl>
	 *            <dl>
	 *            <dt><b>Postconditions:</b></dt>
	 *            <dd>
	 *            <ul>
	 *            <li>enumerateAttributeNames() => old.enumerateAttributeNames()
	 *            + name
	 *            <li>getAttribute(name) => value
	 *            </ul>
	 *            </dd>
	 *            </dl>
	 *            <dl>
	 * @see com.baseoneonline.java.nanoxml.XMLElement#setDoubleAttribute(java.lang.String, double)
	 *      setDoubleAttribute(String, double)
	 * @see com.baseoneonline.java.nanoxml.XMLElement#setIntAttribute(java.lang.String, int)
	 *      setIntAttribute(String, int)
	 * @see com.baseoneonline.java.nanoxml.XMLElement#enumerateAttributeNames()
	 * @see com.baseoneonline.java.nanoxml.XMLElement#getAttribute(java.lang.String)
	 *      getAttribute(String)
	 * @see com.baseoneonline.java.nanoxml.XMLElement#getAttribute(java.lang.String, java.lang.Object)
	 *      getAttribute(String, Object)
	 * @see com.baseoneonline.java.nanoxml.XMLElement#getAttribute(java.lang.String,
	 *      java.util.Hashtable, java.lang.String, boolean) getAttribute(String,
	 *      Hashtable, String, boolean)
	 * @see com.baseoneonline.java.nanoxml.XMLElement#getStringAttribute(java.lang.String)
	 *      getStringAttribute(String)
	 * @see com.baseoneonline.java.nanoxml.XMLElement#getStringAttribute(java.lang.String,
	 *      java.lang.String) getStringAttribute(String, String)
	 * @see com.baseoneonline.java.nanoxml.XMLElement#getStringAttribute(java.lang.String,
	 *      java.util.Hashtable, java.lang.String, boolean)
	 *      getStringAttribute(String, Hashtable, String, boolean)
	 */
	public void setAttribute(String name, final Object value) {
		if (this.ignoreCase) {
			name = name.toUpperCase();
		}
		if (value == null) {
			Logger.getLogger(getClass().getName()).severe(
				"Value is null: " + name);
		}
		this.attributes.put(name, value.toString());
	}

	/**
	 * Adds or modifies an attribute.
	 * 
	 * @param name
	 *            The name of the attribute.
	 * @param value
	 *            The value of the attribute. </dl>
	 *            <dl>
	 *            <dt><b>Preconditions:</b></dt>
	 *            <dd>
	 *            <ul>
	 *            <li><code>name != null</code>
	 *            <li><code>name</code> is a valid XML identifier
	 *            </ul>
	 *            </dd>
	 *            </dl>
	 *            <dl>
	 *            <dt><b>Postconditions:</b></dt>
	 *            <dd>
	 *            <ul>
	 *            <li>enumerateAttributeNames() => old.enumerateAttributeNames()
	 *            + name
	 *            <li>getIntAttribute(name) => value
	 *            </ul>
	 *            </dd>
	 *            </dl>
	 *            <dl>
	 * @see com.baseoneonline.java.nanoxml.XMLElement#setDoubleAttribute(java.lang.String, double)
	 *      setDoubleAttribute(String, double)
	 * @see com.baseoneonline.java.nanoxml.XMLElement#setAttribute(java.lang.String, java.lang.Object)
	 *      setAttribute(String, Object)
	 * @see com.baseoneonline.java.nanoxml.XMLElement#removeAttribute(java.lang.String)
	 *      removeAttribute(String)
	 * @see com.baseoneonline.java.nanoxml.XMLElement#enumerateAttributeNames()
	 * @see com.baseoneonline.java.nanoxml.XMLElement#getIntAttribute(java.lang.String)
	 *      getIntAttribute(String)
	 * @see com.baseoneonline.java.nanoxml.XMLElement#getIntAttribute(java.lang.String, int)
	 *      getIntAttribute(String, int)
	 * @see com.baseoneonline.java.nanoxml.XMLElement#getIntAttribute(java.lang.String,
	 *      java.util.Hashtable, java.lang.String, boolean)
	 *      getIntAttribute(String, Hashtable, String, boolean)
	 */
	public void setIntAttribute(String name, final int value) {
		if (this.ignoreCase) {
			name = name.toUpperCase();
		}
		this.attributes.put(name, Integer.toString(value));
	}

	public void setBooleanAttribute(String name, final boolean value) {
		if (this.ignoreCase) {
			name = name.toUpperCase();
		}
		this.attributes.put(name, value ? TRUE_VALUE : FALSE_VALUE);
	}

	/**
	 * Adds or modifies an attribute.
	 * 
	 * @param name
	 *            The name of the attribute.
	 * @param value
	 *            The value of the attribute. </dl>
	 *            <dl>
	 *            <dt><b>Preconditions:</b></dt>
	 *            <dd>
	 *            <ul>
	 *            <li><code>name != null</code>
	 *            <li><code>name</code> is a valid XML identifier
	 *            </ul>
	 *            </dd>
	 *            </dl>
	 *            <dl>
	 *            <dt><b>Postconditions:</b></dt>
	 *            <dd>
	 *            <ul>
	 *            <li>enumerateAttributeNames() => old.enumerateAttributeNames()
	 *            + name
	 *            <li>getDoubleAttribute(name) => value
	 *            </ul>
	 *            </dd>
	 *            </dl>
	 *            <dl>
	 * @see com.baseoneonline.java.nanoxml.XMLElement#setIntAttribute(java.lang.String, int)
	 *      setIntAttribute(String, int)
	 * @see com.baseoneonline.java.nanoxml.XMLElement#setAttribute(java.lang.String, java.lang.Object)
	 *      setAttribute(String, Object)
	 * @see com.baseoneonline.java.nanoxml.XMLElement#removeAttribute(java.lang.String)
	 *      removeAttribute(String)
	 * @see com.baseoneonline.java.nanoxml.XMLElement#enumerateAttributeNames()
	 * @see com.baseoneonline.java.nanoxml.XMLElement#getDoubleAttribute(java.lang.String)
	 *      getDoubleAttribute(String)
	 * @see com.baseoneonline.java.nanoxml.XMLElement#getDoubleAttribute(java.lang.String, double)
	 *      getDoubleAttribute(String, double)
	 * @see com.baseoneonline.java.nanoxml.XMLElement#getDoubleAttribute(java.lang.String,
	 *      java.util.Hashtable, java.lang.String, boolean)
	 *      getDoubleAttribute(String, Hashtable, String, boolean)
	 */
	public void setDoubleAttribute(String name, final double value) {
		if (this.ignoreCase) {
			name = name.toUpperCase();
		}
		this.attributes.put(name, Double.toString(value));
	}

	public void setFloatAttribute(String name, final float value) {
		if (this.ignoreCase) {
			name = name.toUpperCase();
		}
		this.attributes.put(name, Float.toString(value));
	}

	/**
	 * Returns the number of child elements of the element.
	 * <dl>
	 * <dt><b>Postconditions:</b></dt>
	 * <dd>
	 * <ul>
	 * <li><code>result >= 0</code>
	 * </ul>
	 * </dd>
	 * </dl>
	 * 
	 * @see com.baseoneonline.java.nanoxml.XMLElement#addChild(com.baseoneonline.java.nanoxml.XMLElement) addChild(XMLElement)
	 * @see com.baseoneonline.java.nanoxml.XMLElement#enumerateChildren()
	 * @see com.baseoneonline.java.nanoxml.XMLElement#getChildren()
	 * @see com.baseoneonline.java.nanoxml.XMLElement#removeChild(com.baseoneonline.java.nanoxml.XMLElement)
	 *      removeChild(XMLElement)
	 */
	public int countChildren() {
		return this.children.size();
	}

	/**
	 * Enumerates the attribute names.
	 * <dl>
	 * <dt><b>Postconditions:</b></dt>
	 * <dd>
	 * <ul>
	 * <li><code>result != null</code>
	 * </ul>
	 * </dd>
	 * </dl>
	 * 
	 * @see com.baseoneonline.java.nanoxml.XMLElement#setDoubleAttribute(java.lang.String, double)
	 *      setDoubleAttribute(String, double)
	 * @see com.baseoneonline.java.nanoxml.XMLElement#setIntAttribute(java.lang.String, int)
	 *      setIntAttribute(String, int)
	 * @see com.baseoneonline.java.nanoxml.XMLElement#setAttribute(java.lang.String, java.lang.Object)
	 *      setAttribute(String, Object)
	 * @see com.baseoneonline.java.nanoxml.XMLElement#removeAttribute(java.lang.String)
	 *      removeAttribute(String)
	 * @see com.baseoneonline.java.nanoxml.XMLElement#getAttribute(java.lang.String)
	 *      getAttribute(String)
	 * @see com.baseoneonline.java.nanoxml.XMLElement#getAttribute(java.lang.String, java.lang.Object)
	 *      getAttribute(String, String)
	 * @see com.baseoneonline.java.nanoxml.XMLElement#getAttribute(java.lang.String,
	 *      java.util.Hashtable, java.lang.String, boolean) getAttribute(String,
	 *      Hashtable, String, boolean)
	 * @see com.baseoneonline.java.nanoxml.XMLElement#getStringAttribute(java.lang.String)
	 *      getStringAttribute(String)
	 * @see com.baseoneonline.java.nanoxml.XMLElement#getStringAttribute(java.lang.String,
	 *      java.lang.String) getStringAttribute(String, String)
	 * @see com.baseoneonline.java.nanoxml.XMLElement#getStringAttribute(java.lang.String,
	 *      java.util.Hashtable, java.lang.String, boolean)
	 *      getStringAttribute(String, Hashtable, String, boolean)
	 * @see com.baseoneonline.java.nanoxml.XMLElement#getIntAttribute(java.lang.String)
	 *      getIntAttribute(String)
	 * @see com.baseoneonline.java.nanoxml.XMLElement#getIntAttribute(java.lang.String, int)
	 *      getIntAttribute(String, int)
	 * @see com.baseoneonline.java.nanoxml.XMLElement#getIntAttribute(java.lang.String,
	 *      java.util.Hashtable, java.lang.String, boolean)
	 *      getIntAttribute(String, Hashtable, String, boolean)
	 * @see com.baseoneonline.java.nanoxml.XMLElement#getDoubleAttribute(java.lang.String)
	 *      getDoubleAttribute(String)
	 * @see com.baseoneonline.java.nanoxml.XMLElement#getDoubleAttribute(java.lang.String, double)
	 *      getDoubleAttribute(String, double)
	 * @see com.baseoneonline.java.nanoxml.XMLElement#getDoubleAttribute(java.lang.String,
	 *      java.util.Hashtable, java.lang.String, boolean)
	 *      getDoubleAttribute(String, Hashtable, String, boolean)
	 * @see com.baseoneonline.java.nanoxml.XMLElement#getBooleanAttribute(java.lang.String,
	 *      java.lang.String, java.lang.String, boolean)
	 *      getBooleanAttribute(String, String, String, boolean)
	 */
	public Enumeration<String> enumerateAttributeNames() {
		return this.attributes.keys();
	}

	/**
	 * Enumerates the child elements.
	 * <dl>
	 * <dt><b>Postconditions:</b></dt>
	 * <dd>
	 * <ul>
	 * <li><code>result != null</code>
	 * </ul>
	 * </dd>
	 * </dl>
	 * 
	 * @see com.baseoneonline.java.nanoxml.XMLElement#addChild(com.baseoneonline.java.nanoxml.XMLElement) addChild(XMLElement)
	 * @see com.baseoneonline.java.nanoxml.XMLElement#countChildren()
	 * @see com.baseoneonline.java.nanoxml.XMLElement#getChildren()
	 * @see com.baseoneonline.java.nanoxml.XMLElement#removeChild(com.baseoneonline.java.nanoxml.XMLElement)
	 *      removeChild(XMLElement)
	 */
	public Enumeration<XMLElement> enumerateChildren() {
		return this.children.elements();
	}

	/**
	 * Returns the child elements as a Vector. It is safe to modify this Vector.
	 * <dl>
	 * <dt><b>Postconditions:</b></dt>
	 * <dd>
	 * <ul>
	 * <li><code>result != null</code>
	 * </ul>
	 * </dd>
	 * </dl>
	 * 
	 * @see com.baseoneonline.java.nanoxml.XMLElement#addChild(com.baseoneonline.java.nanoxml.XMLElement) addChild(XMLElement)
	 * @see com.baseoneonline.java.nanoxml.XMLElement#countChildren()
	 * @see com.baseoneonline.java.nanoxml.XMLElement#enumerateChildren()
	 * @see com.baseoneonline.java.nanoxml.XMLElement#removeChild(com.baseoneonline.java.nanoxml.XMLElement)
	 *      removeChild(XMLElement)
	 */
	@SuppressWarnings("unchecked")
	public Vector<XMLElement> getChildren() {
		return (Vector<XMLElement>) children.clone();
	}

	public List<XMLElement> getChildren(final String name) {
		return getChildren(name, false);
	}

	public List<XMLElement> getChildren(final String name,
			final boolean recursive) {
		final List<XMLElement> lst = new ArrayList<XMLElement>();
		for (final XMLElement x : children) {
			if (name.equalsIgnoreCase(x.getName())) {
				lst.add(x);
			}
			if (recursive && x.getChildren().size() > 0) {
				lst.addAll(x.getChildren(name, recursive));
			}
		}
		return lst;
	}

	public XMLElement getChild(final String name) {
		for (final XMLElement x : getChildren()) {
			if (x.getName().equalsIgnoreCase(name)) {
				return x;
			}
		}
		Logger.getLogger(getClass().getName()).severe(
			"Child of " + this.getName() + " not found: " + name);
		return null;
	}

	/**
	 * Returns the PCDATA content of the object. If there is no such content,
	 * <CODE>null</CODE> is returned.
	 * 
	 * @see com.baseoneonline.java.nanoxml.XMLElement#setContent(java.lang.String) setContent(String)
	 */
	public String getContent() {
		return this.contents;
	}

	/**
	 * Returns the line nr in the source data on which the element is found.
	 * This method returns <code>0</code> there is no associated source data.
	 * <dl>
	 * <dt><b>Postconditions:</b></dt>
	 * <dd>
	 * <ul>
	 * <li><code>result >= 0</code>
	 * </ul>
	 * </dd>
	 * </dl>
	 */
	public int getLineNr() {
		return this.lineNr;
	}

	/**
	 * Returns an attribute of the element. If the attribute doesn't exist,
	 * <code>null</code> is returned.
	 * 
	 * @param name
	 *            The name of the attribute. </dl>
	 *            <dl>
	 *            <dt><b>Preconditions:</b></dt>
	 *            <dd>
	 *            <ul>
	 *            <li><code>name != null</code>
	 *            <li><code>name</code> is a valid XML identifier
	 *            </ul>
	 *            </dd>
	 *            </dl>
	 *            <dl>
	 * @see com.baseoneonline.java.nanoxml.XMLElement#setAttribute(java.lang.String, java.lang.Object)
	 *      setAttribute(String, Object)
	 * @see com.baseoneonline.java.nanoxml.XMLElement#removeAttribute(java.lang.String)
	 *      removeAttribute(String)
	 * @see com.baseoneonline.java.nanoxml.XMLElement#enumerateAttributeNames()
	 * @see com.baseoneonline.java.nanoxml.XMLElement#getAttribute(java.lang.String, java.lang.Object)
	 *      getAttribute(String, Object)
	 * @see com.baseoneonline.java.nanoxml.XMLElement#getAttribute(java.lang.String,
	 *      java.util.Hashtable, java.lang.String, boolean) getAttribute(String,
	 *      Hashtable, String, boolean)
	 */
	public Object getAttribute(final String name) {
		return this.getAttribute(name, null);
	}

	/**
	 * Returns an attribute of the element. If the attribute doesn't exist,
	 * <code>defaultValue</code> is returned.
	 * 
	 * @param name
	 *            The name of the attribute.
	 * @param defaultValue
	 *            Key to use if the attribute is missing. </dl>
	 *            <dl>
	 *            <dt><b>Preconditions:</b></dt>
	 *            <dd>
	 *            <ul>
	 *            <li><code>name != null</code>
	 *            <li><code>name</code> is a valid XML identifier
	 *            </ul>
	 *            </dd>
	 *            </dl>
	 *            <dl>
	 * @see com.baseoneonline.java.nanoxml.XMLElement#setAttribute(java.lang.String, java.lang.Object)
	 *      setAttribute(String, Object)
	 * @see com.baseoneonline.java.nanoxml.XMLElement#removeAttribute(java.lang.String)
	 *      removeAttribute(String)
	 * @see com.baseoneonline.java.nanoxml.XMLElement#enumerateAttributeNames()
	 * @see com.baseoneonline.java.nanoxml.XMLElement#getAttribute(java.lang.String)
	 *      getAttribute(String)
	 * @see com.baseoneonline.java.nanoxml.XMLElement#getAttribute(java.lang.String,
	 *      java.util.Hashtable, java.lang.String, boolean) getAttribute(String,
	 *      Hashtable, String, boolean)
	 */
	public Object getAttribute(String name, final Object defaultValue) {
		if (this.ignoreCase) {
			name = name.toUpperCase();
		}
		Object value = this.attributes.get(name);
		if (value == null) {
			Logger.getLogger(getClass().getName()).warning(
				"Attribute not found: " + name);
			value = defaultValue;
		}
		return value;
	}

	/**
	 * Returns an attribute by looking up a key in a hashtable. If the attribute
	 * doesn't exist, the value corresponding to defaultKey is returned.
	 * <P>
	 * As an example, if valueSet contains the mapping <code>"one" =>
	 * "1"</code> and the element contains the attribute <code>attr="one"</code>
	 * , then <code>getAttribute("attr", mapping, defaultKey, false)</code>
	 * returns <code>"1"</code>.
	 * 
	 * @param name
	 *            The name of the attribute.
	 * @param valueSet
	 *            Hashtable mapping keys to values.
	 * @param defaultKey
	 *            Key to use if the attribute is missing.
	 * @param allowLiterals
	 *            <code>true</code> if literals are valid. </dl>
	 *            <dl>
	 *            <dt><b>Preconditions:</b></dt><dd>
	 *            <ul>
	 *            <li><code>name != null</code> <li><code>name</code> is a valid
	 *            XML identifier <li><code>valueSet</code> != null <li>the keys
	 *            of <code>valueSet</code> are strings
	 *            </ul>
	 *            </dd>
	 *            </dl>
	 *            <dl>
	 * @see com.baseoneonline.java.nanoxml.XMLElement#setAttribute(java.lang.String, java.lang.Object)
	 *      setAttribute(String, Object)
	 * @see com.baseoneonline.java.nanoxml.XMLElement#removeAttribute(java.lang.String)
	 *      removeAttribute(String)
	 * @see com.baseoneonline.java.nanoxml.XMLElement#enumerateAttributeNames()
	 * @see com.baseoneonline.java.nanoxml.XMLElement#getAttribute(java.lang.String)
	 *      getAttribute(String)
	 * @see com.baseoneonline.java.nanoxml.XMLElement#getAttribute(java.lang.String, java.lang.Object)
	 *      getAttribute(String, Object)
	 */
	public String getAttribute(String name,
			final Hashtable<String, String> valueSet, final String defaultKey,
			final boolean allowLiterals) {
		if (this.ignoreCase) {
			name = name.toUpperCase();
		}
		String key = this.attributes.get(name);
		String result;
		if (key == null) {
			key = defaultKey;
		}
		result = valueSet.get(key);
		if (result == null) {
			if (allowLiterals) {
				result = key;
			} else {
				throw this.invalidValue(name, key);
			}
		}
		return result;
	}

	/**
	 * Returns an attribute of the element. If the attribute doesn't exist,
	 * <code>null</code> is returned.
	 * 
	 * @param name
	 *            The name of the attribute. </dl>
	 *            <dl>
	 *            <dt><b>Preconditions:</b></dt>
	 *            <dd>
	 *            <ul>
	 *            <li><code>name != null</code>
	 *            <li><code>name</code> is a valid XML identifier
	 *            </ul>
	 *            </dd>
	 *            </dl>
	 *            <dl>
	 * @see com.baseoneonline.java.nanoxml.XMLElement#setAttribute(java.lang.String, java.lang.Object)
	 *      setAttribute(String, Object)
	 * @see com.baseoneonline.java.nanoxml.XMLElement#removeAttribute(java.lang.String)
	 *      removeAttribute(String)
	 * @see com.baseoneonline.java.nanoxml.XMLElement#enumerateAttributeNames()
	 * @see com.baseoneonline.java.nanoxml.XMLElement#getStringAttribute(java.lang.String,
	 *      java.lang.String) getStringAttribute(String, String)
	 * @see com.baseoneonline.java.nanoxml.XMLElement#getStringAttribute(java.lang.String,
	 *      java.util.Hashtable, java.lang.String, boolean)
	 *      getStringAttribute(String, Hashtable, String, boolean)
	 */
	public String getStringAttribute(final String name) {
		return this.getStringAttribute(name, null);
	}

	/**
	 * Returns an attribute of the element. If the attribute doesn't exist,
	 * <code>defaultValue</code> is returned.
	 * 
	 * @param name
	 *            The name of the attribute.
	 * @param defaultValue
	 *            Key to use if the attribute is missing. </dl>
	 *            <dl>
	 *            <dt><b>Preconditions:</b></dt>
	 *            <dd>
	 *            <ul>
	 *            <li><code>name != null</code>
	 *            <li><code>name</code> is a valid XML identifier
	 *            </ul>
	 *            </dd>
	 *            </dl>
	 *            <dl>
	 * @see com.baseoneonline.java.nanoxml.XMLElement#setAttribute(java.lang.String, java.lang.Object)
	 *      setAttribute(String, Object)
	 * @see com.baseoneonline.java.nanoxml.XMLElement#removeAttribute(java.lang.String)
	 *      removeAttribute(String)
	 * @see com.baseoneonline.java.nanoxml.XMLElement#enumerateAttributeNames()
	 * @see com.baseoneonline.java.nanoxml.XMLElement#getStringAttribute(java.lang.String)
	 *      getStringAttribute(String)
	 * @see com.baseoneonline.java.nanoxml.XMLElement#getStringAttribute(java.lang.String,
	 *      java.util.Hashtable, java.lang.String, boolean)
	 *      getStringAttribute(String, Hashtable, String, boolean)
	 */
	public String getStringAttribute(final String name,
			final String defaultValue) {
		return (String) this.getAttribute(name, defaultValue);
	}

	/**
	 * Returns an attribute by looking up a key in a hashtable. If the attribute
	 * doesn't exist, the value corresponding to defaultKey is returned.
	 * <P>
	 * As an example, if valueSet contains the mapping <code>"one" =>
	 * "1"</code> and the element contains the attribute <code>attr="one"</code>
	 * , then <code>getAttribute("attr", mapping, defaultKey, false)</code>
	 * returns <code>"1"</code>.
	 * 
	 * @param name
	 *            The name of the attribute.
	 * @param valueSet
	 *            Hashtable mapping keys to values.
	 * @param defaultKey
	 *            Key to use if the attribute is missing.
	 * @param allowLiterals
	 *            <code>true</code> if literals are valid. </dl>
	 *            <dl>
	 *            <dt><b>Preconditions:</b></dt><dd>
	 *            <ul>
	 *            <li><code>name != null</code> <li><code>name</code> is a valid
	 *            XML identifier <li><code>valueSet</code> != null <li>the keys
	 *            of <code>valueSet</code> are strings <li>the values of <code>
	 *            valueSet</code> are strings
	 *            </ul>
	 *            </dd>
	 *            </dl>
	 *            <dl>
	 * @see com.baseoneonline.java.nanoxml.XMLElement#setAttribute(java.lang.String, java.lang.Object)
	 *      setAttribute(String, Object)
	 * @see com.baseoneonline.java.nanoxml.XMLElement#removeAttribute(java.lang.String)
	 *      removeAttribute(String)
	 * @see com.baseoneonline.java.nanoxml.XMLElement#enumerateAttributeNames()
	 * @see com.baseoneonline.java.nanoxml.XMLElement#getStringAttribute(java.lang.String)
	 *      getStringAttribute(String)
	 * @see com.baseoneonline.java.nanoxml.XMLElement#getStringAttribute(java.lang.String,
	 *      java.lang.String) getStringAttribute(String, String)
	 */
	public String getStringAttribute(final String name,
			final Hashtable<String, String> valueSet, final String defaultKey,
			final boolean allowLiterals) {
		return this.getAttribute(name, valueSet, defaultKey, allowLiterals);
	}

	/**
	 * Returns an attribute of the element. If the attribute doesn't exist,
	 * <code>0</code> is returned.
	 * 
	 * @param name
	 *            The name of the attribute. </dl>
	 *            <dl>
	 *            <dt><b>Preconditions:</b></dt>
	 *            <dd>
	 *            <ul>
	 *            <li><code>name != null</code>
	 *            <li><code>name</code> is a valid XML identifier
	 *            </ul>
	 *            </dd>
	 *            </dl>
	 *            <dl>
	 * @see com.baseoneonline.java.nanoxml.XMLElement#setIntAttribute(java.lang.String, int)
	 *      setIntAttribute(String, int)
	 * @see com.baseoneonline.java.nanoxml.XMLElement#enumerateAttributeNames()
	 * @see com.baseoneonline.java.nanoxml.XMLElement#getIntAttribute(java.lang.String, int)
	 *      getIntAttribute(String, int)
	 * @see com.baseoneonline.java.nanoxml.XMLElement#getIntAttribute(java.lang.String,
	 *      java.util.Hashtable, java.lang.String, boolean)
	 *      getIntAttribute(String, Hashtable, String, boolean)
	 */
	public int getIntAttribute(final String name) {
		return this.getIntAttribute(name, 0);
	}

	/**
	 * Returns an attribute of the element. If the attribute doesn't exist,
	 * <code>defaultValue</code> is returned.
	 * 
	 * @param name
	 *            The name of the attribute.
	 * @param defaultValue
	 *            Key to use if the attribute is missing. </dl>
	 *            <dl>
	 *            <dt><b>Preconditions:</b></dt>
	 *            <dd>
	 *            <ul>
	 *            <li><code>name != null</code>
	 *            <li><code>name</code> is a valid XML identifier
	 *            </ul>
	 *            </dd>
	 *            </dl>
	 *            <dl>
	 * @see com.baseoneonline.java.nanoxml.XMLElement#setIntAttribute(java.lang.String, int)
	 *      setIntAttribute(String, int)
	 * @see com.baseoneonline.java.nanoxml.XMLElement#enumerateAttributeNames()
	 * @see com.baseoneonline.java.nanoxml.XMLElement#getIntAttribute(java.lang.String)
	 *      getIntAttribute(String)
	 * @see com.baseoneonline.java.nanoxml.XMLElement#getIntAttribute(java.lang.String,
	 *      java.util.Hashtable, java.lang.String, boolean)
	 *      getIntAttribute(String, Hashtable, String, boolean)
	 */
	public int getIntAttribute(String name, final int defaultValue) {
		if (this.ignoreCase) {
			name = name.toUpperCase();
		}
		final String value = this.attributes.get(name);
		if (value == null) {
			return defaultValue;
		} else {
			try {
				return Integer.parseInt(value);
			} catch (final NumberFormatException e) {
				throw this.invalidValue(name, value);
			}
		}
	}

	public ColorRGBA getColorAttribute(String name, ColorRGBA defaultValue) {
		name = name.toUpperCase();
		String c = this.attributes.get(name);
		if (c == null) {
			Logger.getLogger(getClass().getName()).warning(
				"Attribute not found: " + name);
			return defaultValue;
		}
		String[] col = c.split(",");
		if (col.length > 0 && col.length <= 4 && col.length != 2) {
			float[] colf = new float[col.length];
			for (int i = 0; i < col.length; i++) {
				colf[i] = Float.parseFloat(col[i]);
			}

			if (colf.length == 1) {
				return new ColorRGBA(colf[0], colf[0], colf[0], 1);
			} else {
				float a = colf.length == 4 ? colf[3] : 1;
				ColorRGBA color = new ColorRGBA(colf[0], colf[1], colf[2], a);
				return color;

			}

		} else {
			Logger.getLogger(getClass().getName())
					.warning("Faulty color value");
			return defaultValue;
		}
	}

	/**
	 * Returns an attribute by looking up a key in a hashtable. If the attribute
	 * doesn't exist, the value corresponding to defaultKey is returned.
	 * <P>
	 * As an example, if valueSet contains the mapping <code>"one" => 1</code>
	 * and the element contains the attribute <code>attr="one"</code>, then
	 * <code>getIntAttribute("attr", mapping, defaultKey, false)</code> returns
	 * <code>1</code>.
	 * 
	 * @param name
	 *            The name of the attribute.
	 * @param valueSet
	 *            Hashtable mapping keys to values.
	 * @param defaultKey
	 *            Key to use if the attribute is missing.
	 * @param allowLiteralNumbers
	 *            <code>true</code> if literal numbers are valid. </dl>
	 *            <dl>
	 *            <dt><b>Preconditions:</b></dt><dd>
	 *            <ul>
	 *            <li><code>name != null</code> <li><code>name</code> is a valid
	 *            XML identifier <li><code>valueSet</code> != null <li>the keys
	 *            of <code>valueSet</code> are strings <li>the values of <code>
	 *            valueSet</code> are Integer objects <li><code>defaultKey
	 *            </code> is either <code>null</code>, a key in <code>valueSet
	 *            </code> or an integer.
	 *            </ul>
	 *            </dd>
	 *            </dl>
	 *            <dl>
	 * @see com.baseoneonline.java.nanoxml.XMLElement#setIntAttribute(java.lang.String, int)
	 *      setIntAttribute(String, int)
	 * @see com.baseoneonline.java.nanoxml.XMLElement#enumerateAttributeNames()
	 * @see com.baseoneonline.java.nanoxml.XMLElement#getIntAttribute(java.lang.String)
	 *      getIntAttribute(String)
	 * @see com.baseoneonline.java.nanoxml.XMLElement#getIntAttribute(java.lang.String, int)
	 *      getIntAttribute(String, int)
	 */
	public int getIntAttribute(String name,
			final Hashtable<String, Object> valueSet, final String defaultKey,
			final boolean allowLiteralNumbers) {
		if (this.ignoreCase) {
			name = name.toUpperCase();
		}
		Object key = this.attributes.get(name);
		Integer result;
		if (key == null) {
			key = defaultKey;
		}
		try {
			result = (Integer) valueSet.get(key);
		} catch (final ClassCastException e) {
			throw this.invalidValueSet(name);
		}
		if (result == null) {
			if (!allowLiteralNumbers) {
				throw this.invalidValue(name, (String) key);
			}
			try {
				result = Integer.valueOf((String) key);
			} catch (final NumberFormatException e) {
				throw this.invalidValue(name, (String) key);
			}
		}
		return result.intValue();
	}

	/**
	 * Returns an attribute of the element. If the attribute doesn't exist,
	 * <code>0.0</code> is returned.
	 * 
	 * @param name
	 *            The name of the attribute. </dl>
	 *            <dl>
	 *            <dt><b>Preconditions:</b></dt>
	 *            <dd>
	 *            <ul>
	 *            <li><code>name != null</code>
	 *            <li><code>name</code> is a valid XML identifier
	 *            </ul>
	 *            </dd>
	 *            </dl>
	 *            <dl>
	 * @see com.baseoneonline.java.nanoxml.XMLElement#setDoubleAttribute(java.lang.String, double)
	 *      setDoubleAttribute(String, double)
	 * @see com.baseoneonline.java.nanoxml.XMLElement#enumerateAttributeNames()
	 * @see com.baseoneonline.java.nanoxml.XMLElement#getDoubleAttribute(java.lang.String, double)
	 *      getDoubleAttribute(String, double)
	 * @see com.baseoneonline.java.nanoxml.XMLElement#getDoubleAttribute(java.lang.String,
	 *      java.util.Hashtable, java.lang.String, boolean)
	 *      getDoubleAttribute(String, Hashtable, String, boolean)
	 */
	public double getDoubleAttribute(final String name) {
		return this.getDoubleAttribute(name, 0.);
	}

	public float getFloatAttribute(final String name) {
		return this.getFloatAttribute(name, 0.f);
	}

	/**
	 * Returns an attribute of the element. If the attribute doesn't exist,
	 * <code>defaultValue</code> is returned.
	 * 
	 * @param name
	 *            The name of the attribute.
	 * @param defaultValue
	 *            Key to use if the attribute is missing. </dl>
	 *            <dl>
	 *            <dt><b>Preconditions:</b></dt>
	 *            <dd>
	 *            <ul>
	 *            <li><code>name != null</code>
	 *            <li><code>name</code> is a valid XML identifier
	 *            </ul>
	 *            </dd>
	 *            </dl>
	 *            <dl>
	 * @see com.baseoneonline.java.nanoxml.XMLElement#setDoubleAttribute(java.lang.String, double)
	 *      setDoubleAttribute(String, double)
	 * @see com.baseoneonline.java.nanoxml.XMLElement#enumerateAttributeNames()
	 * @see com.baseoneonline.java.nanoxml.XMLElement#getDoubleAttribute(java.lang.String)
	 *      getDoubleAttribute(String)
	 * @see com.baseoneonline.java.nanoxml.XMLElement#getDoubleAttribute(java.lang.String,
	 *      java.util.Hashtable, java.lang.String, boolean)
	 *      getDoubleAttribute(String, Hashtable, String, boolean)
	 */
	public double getDoubleAttribute(String name, final double defaultValue) {
		if (this.ignoreCase) {
			name = name.toUpperCase();
		}
		final String value = this.attributes.get(name);
		if (value == null) {
			return defaultValue;
		} else {
			try {
				return Double.valueOf(value).doubleValue();
			} catch (final NumberFormatException e) {
				throw this.invalidValue(name, value);
			}
		}
	}

	public float getFloatAttribute(String name, final float defaultValue) {
		if (this.ignoreCase) {
			name = name.toUpperCase();
		}
		final String value = this.attributes.get(name);
		if (value == null) {
			return defaultValue;
		} else {
			try {
				return Float.valueOf(value).floatValue();
			} catch (final NumberFormatException e) {
				throw this.invalidValue(name, value);
			}
		}
	}

	/**
	 * Returns an attribute by looking up a key in a hashtable. If the attribute
	 * doesn't exist, the value corresponding to defaultKey is returned.
	 * <P>
	 * As an example, if valueSet contains the mapping <code>"one" =&gt;
	 * 1.0</code> and the element contains the attribute <code>attr="one"</code>
	 * , then
	 * <code>getDoubleAttribute("attr", mapping, defaultKey, false)</code>
	 * returns <code>1.0</code>.
	 * 
	 * @param name
	 *            The name of the attribute.
	 * @param valueSet
	 *            Hashtable mapping keys to values.
	 * @param defaultKey
	 *            Key to use if the attribute is missing.
	 * @param allowLiteralNumbers
	 *            <code>true</code> if literal numbers are valid. </dl>
	 *            <dl>
	 *            <dt><b>Preconditions:</b></dt><dd>
	 *            <ul>
	 *            <li><code>name != null</code> <li><code>name</code> is a valid
	 *            XML identifier <li><code>valueSet != null</code> <li>the keys
	 *            of <code>valueSet</code> are strings <li>the values of <code>
	 *            valueSet</code> are Double objects <li><code>defaultKey</code>
	 *            is either <code>null</code>, a key in <code>valueSet</code> or
	 *            a double.
	 *            </ul>
	 *            </dd>
	 *            </dl>
	 *            <dl>
	 * @see com.baseoneonline.java.nanoxml.XMLElement#setDoubleAttribute(java.lang.String, double)
	 *      setDoubleAttribute(String, double)
	 * @see com.baseoneonline.java.nanoxml.XMLElement#enumerateAttributeNames()
	 * @see com.baseoneonline.java.nanoxml.XMLElement#getDoubleAttribute(java.lang.String)
	 *      getDoubleAttribute(String)
	 * @see com.baseoneonline.java.nanoxml.XMLElement#getDoubleAttribute(java.lang.String, double)
	 *      getDoubleAttribute(String, double)
	 */
	public double getDoubleAttribute(String name,
			final Hashtable<String, Object> valueSet, final String defaultKey,
			final boolean allowLiteralNumbers) {
		if (this.ignoreCase) {
			name = name.toUpperCase();
		}
		Object key = this.attributes.get(name);
		Double result;
		if (key == null) {
			key = defaultKey;
		}
		try {
			result = (Double) valueSet.get(key);
		} catch (final ClassCastException e) {
			throw this.invalidValueSet(name);
		}
		if (result == null) {
			if (!allowLiteralNumbers) {
				throw this.invalidValue(name, (String) key);
			}
			try {
				result = Double.valueOf((String) key);
			} catch (final NumberFormatException e) {
				throw this.invalidValue(name, (String) key);
			}
		}
		return result.doubleValue();
	}

	/**
	 * Returns an attribute of the element. If the attribute doesn't exist,
	 * <code>defaultValue</code> is returned. If the value of the attribute is
	 * equal to <code>trueValue</code>, <code>true</code> is returned. If the
	 * value of the attribute is equal to <code>falseValue</code>,
	 * <code>false</code> is returned. If the value doesn't match
	 * <code>trueValue</code> or <code>falseValue</code>, an exception is
	 * thrown.
	 * 
	 * @param name
	 *            The name of the attribute.
	 * @param trueValue
	 *            The value associated with <code>true</code>.
	 * @param falseValue
	 *            The value associated with <code>true</code>.
	 * @param defaultValue
	 *            Value to use if the attribute is missing. </dl>
	 *            <dl>
	 *            <dt><b>Preconditions:</b></dt>
	 *            <dd>
	 *            <ul>
	 *            <li><code>name != null</code>
	 *            <li><code>name</code> is a valid XML identifier
	 *            <li><code>trueValue</code> and <code>falseValue</code> are
	 *            different strings.
	 *            </ul>
	 *            </dd>
	 *            </dl>
	 *            <dl>
	 * @see com.baseoneonline.java.nanoxml.XMLElement#setAttribute(java.lang.String, java.lang.Object)
	 *      setAttribute(String, Object)
	 * @see com.baseoneonline.java.nanoxml.XMLElement#removeAttribute(java.lang.String)
	 *      removeAttribute(String)
	 * @see com.baseoneonline.java.nanoxml.XMLElement#enumerateAttributeNames()
	 */
	public boolean getBooleanAttribute(final String name) {
		return getBooleanAttribute(name, TRUE_VALUE, FALSE_VALUE, false);
	}

	public boolean getBooleanAttribute(String name, final String trueValue,
			final String falseValue, final boolean defaultValue) {
		if (this.ignoreCase) {
			name = name.toUpperCase();
		}
		final Object value = this.attributes.get(name);
		if (value == null) {
			return defaultValue;
		} else if (value.equals(trueValue)) {
			return true;
		} else if (value.equals(falseValue)) {
			return false;
		} else {
			throw this.invalidValue(name, (String) value);
		}
	}

	public boolean hasName(final String name) {
		return name.equalsIgnoreCase(this.name);
	}

	/**
	 * Returns the name of the element.
	 * 
	 * @see com.baseoneonline.java.nanoxml.XMLElement#setName(java.lang.String) setName(String)
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Reads one XML element from a java.io.Reader and parses it.
	 * 
	 * @param reader
	 *            The reader from which to retrieve the XML data. </dl>
	 *            <dl>
	 *            <dt><b>Preconditions:</b></dt>
	 *            <dd>
	 *            <ul>
	 *            <li><code>reader != null</code>
	 *            <li><code>reader</code> is not closed
	 *            </ul>
	 *            </dd>
	 *            </dl>
	 *            <dl>
	 *            <dt><b>Postconditions:</b></dt>
	 *            <dd>
	 *            <ul>
	 *            <li>the state of the receiver is updated to reflect the XML
	 *            element parsed from the reader
	 *            <li>the reader points to the first character following the
	 *            last '&gt;' character of the XML element
	 *            </ul>
	 *            </dd>
	 *            </dl>
	 *            <dl>
	 * @throws java.io.IOException
	 *             If an error occured while reading the input.
	 * @throws com.baseoneonline.java.nanoxml.XMLParseException
	 *             If an error occured while parsing the read data.
	 */
	public void parseFromReader(final Reader reader) throws IOException,
			XMLParseException {
		this.parseFromReader(reader, /* startingLineNr */1);
	}

	/**
	 * Reads one XML element from a java.io.Reader and parses it.
	 * 
	 * @param reader
	 *            The reader from which to retrieve the XML data.
	 * @param startingLineNr
	 *            The line number of the first line in the data. </dl>
	 *            <dl>
	 *            <dt><b>Preconditions:</b></dt>
	 *            <dd>
	 *            <ul>
	 *            <li><code>reader != null</code>
	 *            <li><code>reader</code> is not closed
	 *            </ul>
	 *            </dd>
	 *            </dl>
	 *            <dl>
	 *            <dt><b>Postconditions:</b></dt>
	 *            <dd>
	 *            <ul>
	 *            <li>the state of the receiver is updated to reflect the XML
	 *            element parsed from the reader
	 *            <li>the reader points to the first character following the
	 *            last '&gt;' character of the XML element
	 *            </ul>
	 *            </dd>
	 *            </dl>
	 *            <dl>
	 * @throws java.io.IOException
	 *             If an error occured while reading the input.
	 * @throws com.baseoneonline.java.nanoxml.XMLParseException
	 *             If an error occured while parsing the read data.
	 */
	public void parseFromReader(final Reader reader, final int startingLineNr)
			throws IOException, XMLParseException {
		this.name = null;
		this.contents = "";
		this.attributes = new Hashtable<String, String>();
		this.children = new Vector<XMLElement>();
		this.charReadTooMuch = '\0';
		this.reader = reader;
		this.parserLineNr = startingLineNr;

		for (;;) {
			char ch = this.scanWhitespace();

			if (ch != '<') {
				throw this.expectedInput("<");
			}

			ch = this.readChar();

			if ((ch == '!') || (ch == '?')) {
				this.skipSpecialTag(0);
			} else {
				this.unreadChar(ch);
				this.scanElement(this);
				return;
			}
		}
	}

	/**
	 * Reads one XML element from a String and parses it.
	 * 
	 * @param reader
	 *            The reader from which to retrieve the XML data. </dl>
	 *            <dl>
	 *            <dt><b>Preconditions:</b></dt>
	 *            <dd>
	 *            <ul>
	 *            <li><code>string != null</code>
	 *            <li><code>string.length() &gt; 0</code>
	 *            </ul>
	 *            </dd>
	 *            </dl>
	 *            <dl>
	 *            <dt><b>Postconditions:</b></dt>
	 *            <dd>
	 *            <ul>
	 *            <li>the state of the receiver is updated to reflect the XML
	 *            element parsed from the reader
	 *            </ul>
	 *            </dd>
	 *            </dl>
	 *            <dl>
	 * @throws com.baseoneonline.java.nanoxml.XMLParseException
	 *             If an error occured while parsing the string.
	 */
	public void parseString(final String string) throws XMLParseException {
		try {
			this.parseFromReader(new StringReader(string),
			/* startingLineNr */1);
		} catch (final IOException e) {
			// Java exception handling suxx
		}
	}

	/**
	 * Reads one XML element from a String and parses it.
	 * 
	 * @param reader
	 *            The reader from which to retrieve the XML data.
	 * @param offset
	 *            The first character in <code>string</code> to scan. </dl>
	 *            <dl>
	 *            <dt><b>Preconditions:</b></dt>
	 *            <dd>
	 *            <ul>
	 *            <li><code>string != null</code>
	 *            <li><code>offset &lt; string.length()</code>
	 *            <li><code>offset &gt;= 0</code>
	 *            </ul>
	 *            </dd>
	 *            </dl>
	 *            <dl>
	 *            <dt><b>Postconditions:</b></dt>
	 *            <dd>
	 *            <ul>
	 *            <li>the state of the receiver is updated to reflect the XML
	 *            element parsed from the reader
	 *            </ul>
	 *            </dd>
	 *            </dl>
	 *            <dl>
	 * @throws com.baseoneonline.java.nanoxml.XMLParseException
	 *             If an error occured while parsing the string.
	 */
	public void parseString(final String string, final int offset)
			throws XMLParseException {
		this.parseString(string.substring(offset));
	}

	/**
	 * Reads one XML element from a String and parses it.
	 * 
	 * @param reader
	 *            The reader from which to retrieve the XML data.
	 * @param offset
	 *            The first character in <code>string</code> to scan.
	 * @param end
	 *            The character where to stop scanning. This character is not
	 *            scanned. </dl>
	 *            <dl>
	 *            <dt><b>Preconditions:</b></dt>
	 *            <dd>
	 *            <ul>
	 *            <li><code>string != null</code>
	 *            <li><code>end &lt;= string.length()</code>
	 *            <li><code>offset &lt; end</code>
	 *            <li><code>offset &gt;= 0</code>
	 *            </ul>
	 *            </dd>
	 *            </dl>
	 *            <dl>
	 *            <dt><b>Postconditions:</b></dt>
	 *            <dd>
	 *            <ul>
	 *            <li>the state of the receiver is updated to reflect the XML
	 *            element parsed from the reader
	 *            </ul>
	 *            </dd>
	 *            </dl>
	 *            <dl>
	 * @throws com.baseoneonline.java.nanoxml.XMLParseException
	 *             If an error occured while parsing the string.
	 */
	public void parseString(final String string, final int offset, final int end)
			throws XMLParseException {
		this.parseString(string.substring(offset, end));
	}

	/**
	 * Reads one XML element from a String and parses it.
	 * 
	 * @param reader
	 *            The reader from which to retrieve the XML data.
	 * @param offset
	 *            The first character in <code>string</code> to scan.
	 * @param end
	 *            The character where to stop scanning. This character is not
	 *            scanned.
	 * @param startingLineNr
	 *            The line number of the first line in the data. </dl>
	 *            <dl>
	 *            <dt><b>Preconditions:</b></dt>
	 *            <dd>
	 *            <ul>
	 *            <li><code>string != null</code>
	 *            <li><code>end &lt;= string.length()</code>
	 *            <li><code>offset &lt; end</code>
	 *            <li><code>offset &gt;= 0</code>
	 *            </ul>
	 *            </dd>
	 *            </dl>
	 *            <dl>
	 *            <dt><b>Postconditions:</b></dt>
	 *            <dd>
	 *            <ul>
	 *            <li>the state of the receiver is updated to reflect the XML
	 *            element parsed from the reader
	 *            </ul>
	 *            </dd>
	 *            </dl>
	 *            <dl>
	 * @throws com.baseoneonline.java.nanoxml.XMLParseException
	 *             If an error occured while parsing the string.
	 */
	public void parseString(String string, final int offset, final int end,
			final int startingLineNr) throws XMLParseException {
		string = string.substring(offset, end);
		try {
			this.parseFromReader(new StringReader(string), startingLineNr);
		} catch (final IOException e) {
			// Java exception handling suxx
		}
	}

	/**
	 * Reads one XML element from a char array and parses it.
	 * 
	 * @param reader
	 *            The reader from which to retrieve the XML data.
	 * @param offset
	 *            The first character in <code>string</code> to scan.
	 * @param end
	 *            The character where to stop scanning. This character is not
	 *            scanned. </dl>
	 *            <dl>
	 *            <dt><b>Preconditions:</b></dt>
	 *            <dd>
	 *            <ul>
	 *            <li><code>input != null</code>
	 *            <li><code>end &lt;= input.length</code>
	 *            <li><code>offset &lt; end</code>
	 *            <li><code>offset &gt;= 0</code>
	 *            </ul>
	 *            </dd>
	 *            </dl>
	 *            <dl>
	 *            <dt><b>Postconditions:</b></dt>
	 *            <dd>
	 *            <ul>
	 *            <li>the state of the receiver is updated to reflect the XML
	 *            element parsed from the reader
	 *            </ul>
	 *            </dd>
	 *            </dl>
	 *            <dl>
	 * @throws com.baseoneonline.java.nanoxml.XMLParseException
	 *             If an error occured while parsing the string.
	 */
	public void parseCharArray(final char[] input, final int offset,
			final int end) throws XMLParseException {
		this.parseCharArray(input, offset, end, /* startingLineNr */1);
	}

	/**
	 * Reads one XML element from a char array and parses it.
	 * 
	 * @param reader
	 *            The reader from which to retrieve the XML data.
	 * @param offset
	 *            The first character in <code>string</code> to scan.
	 * @param end
	 *            The character where to stop scanning. This character is not
	 *            scanned.
	 * @param startingLineNr
	 *            The line number of the first line in the data. </dl>
	 *            <dl>
	 *            <dt><b>Preconditions:</b></dt>
	 *            <dd>
	 *            <ul>
	 *            <li><code>input != null</code>
	 *            <li><code>end &lt;= input.length</code>
	 *            <li><code>offset &lt; end</code>
	 *            <li><code>offset &gt;= 0</code>
	 *            </ul>
	 *            </dd>
	 *            </dl>
	 *            <dl>
	 *            <dt><b>Postconditions:</b></dt>
	 *            <dd>
	 *            <ul>
	 *            <li>the state of the receiver is updated to reflect the XML
	 *            element parsed from the reader
	 *            </ul>
	 *            </dd>
	 *            </dl>
	 *            <dl>
	 * @throws com.baseoneonline.java.nanoxml.XMLParseException
	 *             If an error occured while parsing the string.
	 */
	public void parseCharArray(final char[] input, final int offset,
			final int end, final int startingLineNr) throws XMLParseException {
		try {
			final Reader reader = new CharArrayReader(input, offset, end);
			this.parseFromReader(reader, startingLineNr);
		} catch (final IOException e) {
			// This exception will never happen.
		}
	}

	/**
	 * Removes a child element.
	 * 
	 * @param child
	 *            The child element to remove. </dl>
	 *            <dl>
	 *            <dt><b>Preconditions:</b></dt>
	 *            <dd>
	 *            <ul>
	 *            <li><code>child != null</code>
	 *            <li><code>child</code> is a child element of the receiver
	 *            </ul>
	 *            </dd>
	 *            </dl>
	 *            <dl>
	 *            <dt><b>Postconditions:</b></dt>
	 *            <dd>
	 *            <ul>
	 *            <li>countChildren() => old.countChildren() - 1
	 *            <li>enumerateChildren() => old.enumerateChildren() - child
	 *            <li>getChildren() => old.enumerateChildren() - child
	 *            </ul>
	 *            </dd>
	 *            </dl>
	 *            <dl>
	 * @see com.baseoneonline.java.nanoxml.XMLElement#addChild(com.baseoneonline.java.nanoxml.XMLElement) addChild(XMLElement)
	 * @see com.baseoneonline.java.nanoxml.XMLElement#countChildren()
	 * @see com.baseoneonline.java.nanoxml.XMLElement#enumerateChildren()
	 * @see com.baseoneonline.java.nanoxml.XMLElement#getChildren()
	 */
	public void removeChild(final XMLElement child) {
		this.children.removeElement(child);
	}

	/**
	 * Removes an attribute.
	 * 
	 * @param name
	 *            The name of the attribute. </dl>
	 *            <dl>
	 *            <dt><b>Preconditions:</b></dt>
	 *            <dd>
	 *            <ul>
	 *            <li><code>name != null</code>
	 *            <li><code>name</code> is a valid XML identifier
	 *            </ul>
	 *            </dd>
	 *            </dl>
	 *            <dl>
	 *            <dt><b>Postconditions:</b></dt>
	 *            <dd>
	 *            <ul>
	 *            <li>enumerateAttributeNames() => old.enumerateAttributeNames()
	 *            - name
	 *            <li>getAttribute(name) => <code>null</code>
	 *            </ul>
	 *            </dd>
	 *            </dl>
	 *            <dl>
	 * @see com.baseoneonline.java.nanoxml.XMLElement#enumerateAttributeNames()
	 * @see com.baseoneonline.java.nanoxml.XMLElement#setDoubleAttribute(java.lang.String, double)
	 *      setDoubleAttribute(String, double)
	 * @see com.baseoneonline.java.nanoxml.XMLElement#setIntAttribute(java.lang.String, int)
	 *      setIntAttribute(String, int)
	 * @see com.baseoneonline.java.nanoxml.XMLElement#setAttribute(java.lang.String, java.lang.Object)
	 *      setAttribute(String, Object)
	 * @see com.baseoneonline.java.nanoxml.XMLElement#getAttribute(java.lang.String)
	 *      getAttribute(String)
	 * @see com.baseoneonline.java.nanoxml.XMLElement#getAttribute(java.lang.String, java.lang.Object)
	 *      getAttribute(String, Object)
	 * @see com.baseoneonline.java.nanoxml.XMLElement#getAttribute(java.lang.String,
	 *      java.util.Hashtable, java.lang.String, boolean) getAttribute(String,
	 *      Hashtable, String, boolean)
	 * @see com.baseoneonline.java.nanoxml.XMLElement#getStringAttribute(java.lang.String)
	 *      getStringAttribute(String)
	 * @see com.baseoneonline.java.nanoxml.XMLElement#getStringAttribute(java.lang.String,
	 *      java.lang.String) getStringAttribute(String, String)
	 * @see com.baseoneonline.java.nanoxml.XMLElement#getStringAttribute(java.lang.String,
	 *      java.util.Hashtable, java.lang.String, boolean)
	 *      getStringAttribute(String, Hashtable, String, boolean)
	 * @see com.baseoneonline.java.nanoxml.XMLElement#getIntAttribute(java.lang.String)
	 *      getIntAttribute(String)
	 * @see com.baseoneonline.java.nanoxml.XMLElement#getIntAttribute(java.lang.String, int)
	 *      getIntAttribute(String, int)
	 * @see com.baseoneonline.java.nanoxml.XMLElement#getIntAttribute(java.lang.String,
	 *      java.util.Hashtable, java.lang.String, boolean)
	 *      getIntAttribute(String, Hashtable, String, boolean)
	 * @see com.baseoneonline.java.nanoxml.XMLElement#getDoubleAttribute(java.lang.String)
	 *      getDoubleAttribute(String)
	 * @see com.baseoneonline.java.nanoxml.XMLElement#getDoubleAttribute(java.lang.String, double)
	 *      getDoubleAttribute(String, double)
	 * @see com.baseoneonline.java.nanoxml.XMLElement#getDoubleAttribute(java.lang.String,
	 *      java.util.Hashtable, java.lang.String, boolean)
	 *      getDoubleAttribute(String, Hashtable, String, boolean)
	 * @see com.baseoneonline.java.nanoxml.XMLElement#getBooleanAttribute(java.lang.String,
	 *      java.lang.String, java.lang.String, boolean)
	 *      getBooleanAttribute(String, String, String, boolean)
	 */
	public void removeAttribute(String name) {
		if (this.ignoreCase) {
			name = name.toUpperCase();
		}
		this.attributes.remove(name);
	}

	/**
	 * Creates a new similar XML element.
	 * <P>
	 * You should override this method when subclassing XMLElement.
	 */
	protected XMLElement createAnotherElement() {
		return new XMLElement(this.entities, this.ignoreWhitespace, false,
			this.ignoreCase);
	}

	/**
	 * Changes the content string.
	 * 
	 * @param content
	 *            The new content string.
	 */
	public void setContent(final String content) {
		this.contents = content;
	}

	/**
	 * Changes the name of the element.
	 * 
	 * @param name
	 *            The new name. </dl>
	 *            <dl>
	 *            <dt><b>Preconditions:</b></dt>
	 *            <dd>
	 *            <ul>
	 *            <li><code>name != null</code>
	 *            <li><code>name</code> is a valid XML identifier
	 *            </ul>
	 *            </dd>
	 *            </dl>
	 * @see com.baseoneonline.java.nanoxml.XMLElement#getName()
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * Writes the XML element to a string.
	 * 
	 * @see com.baseoneonline.java.nanoxml.XMLElement#write(java.io.Writer) write(Writer)
	 */
	@Override
	public String toString() {
		try {
			final ByteArrayOutputStream out = new ByteArrayOutputStream();
			final OutputStreamWriter writer = new OutputStreamWriter(out);
			this.write(writer);
			writer.flush();
			return new String(out.toByteArray());
		} catch (final IOException e) {
			// Java exception handling suxx
			return super.toString();
		}
	}

	/**
	 * Writes the XML element to a writer.
	 * 
	 * @param writer
	 *            The writer to write the XML data to. </dl>
	 *            <dl>
	 *            <dt><b>Preconditions:</b></dt>
	 *            <dd>
	 *            <ul>
	 *            <li><code>writer != null</code>
	 *            <li><code>writer</code> is not closed
	 *            </ul>
	 *            </dd>
	 *            </dl>
	 * @throws java.io.IOException
	 *             If the data could not be written to the writer.
	 * @see com.baseoneonline.java.nanoxml.XMLElement#toString()
	 */
	public void write(final Writer writer) throws IOException {
		if (this.name == null) {
			this.writeEncoded(writer, this.contents);
			return;
		}
		writer.write('<');
		writer.write(this.name);
		if (!this.attributes.isEmpty()) {
			final Enumeration<String> enu = this.attributes.keys();
			while (enu.hasMoreElements()) {
				writer.write(' ');
				final String key = enu.nextElement();
				final String value = this.attributes.get(key);
				writer.write(key);
				writer.write('=');
				writer.write('"');
				this.writeEncoded(writer, value);
				writer.write('"');
			}
		}
		if ((this.contents != null) && (this.contents.length() > 0)) {
			writer.write('>');
			this.writeEncoded(writer, this.contents);
			writer.write('<');
			writer.write('/');
			writer.write(this.name);
			writer.write('>');
		} else if (this.children.isEmpty()) {
			writer.write('/');
			writer.write('>');
		} else {
			writer.write('>');
			final Enumeration<XMLElement> enu = this.enumerateChildren();
			while (enu.hasMoreElements()) {
				final XMLElement child = enu.nextElement();
				child.write(writer);
			}
			writer.write('<');
			writer.write('/');
			writer.write(this.name);
			writer.write('>');
		}
	}

	/**
	 * Writes a string encoded to a writer.
	 * 
	 * @param writer
	 *            The writer to write the XML data to.
	 * @param str
	 *            The string to write encoded. </dl>
	 *            <dl>
	 *            <dt><b>Preconditions:</b></dt>
	 *            <dd>
	 *            <ul>
	 *            <li><code>writer != null</code>
	 *            <li><code>writer</code> is not closed
	 *            <li><code>str != null</code>
	 *            </ul>
	 *            </dd>
	 *            </dl>
	 */
	protected void writeEncoded(final Writer writer, final String str)
			throws IOException {
		for (int i = 0; i < str.length(); i += 1) {
			final char ch = str.charAt(i);
			switch (ch) {
			case '<':
				writer.write('&');
				writer.write('l');
				writer.write('t');
				writer.write(';');
				break;
			case '>':
				writer.write('&');
				writer.write('g');
				writer.write('t');
				writer.write(';');
				break;
			case '&':
				writer.write('&');
				writer.write('a');
				writer.write('m');
				writer.write('p');
				writer.write(';');
				break;
			case '"':
				writer.write('&');
				writer.write('q');
				writer.write('u');
				writer.write('o');
				writer.write('t');
				writer.write(';');
				break;
			case '\'':
				writer.write('&');
				writer.write('a');
				writer.write('p');
				writer.write('o');
				writer.write('s');
				writer.write(';');
				break;
			default:
				final int unicode = ch;
				if ((unicode < 32) || (unicode > 126)) {
					writer.write('&');
					writer.write('#');
					writer.write('x');
					writer.write(Integer.toString(unicode, 16));
					writer.write(';');
				} else {
					writer.write(ch);
				}
			}
		}
	}

	/**
	 * Scans an identifier from the current reader. The scanned identifier is
	 * appended to <code>result</code>.
	 * 
	 * @param result
	 *            The buffer in which the scanned identifier will be put. </dl>
	 *            <dl>
	 *            <dt><b>Preconditions:</b></dt>
	 *            <dd>
	 *            <ul>
	 *            <li><code>result != null</code>
	 *            <li>The next character read from the reader is a valid first
	 *            character of an XML identifier.
	 *            </ul>
	 *            </dd>
	 *            </dl>
	 *            <dl>
	 *            <dt><b>Postconditions:</b></dt>
	 *            <dd>
	 *            <ul>
	 *            <li>The next character read from the reader won't be an
	 *            identifier character.
	 *            </ul>
	 *            </dd>
	 *            </dl>
	 *            <dl>
	 */
	protected void scanIdentifier(final StringBuffer result) throws IOException {
		for (;;) {
			final char ch = this.readChar();
			if (((ch < 'A') || (ch > 'Z')) && ((ch < 'a') || (ch > 'z'))
				&& ((ch < '0') || (ch > '9')) && (ch != '_') && (ch != '.')
				&& (ch != ':') && (ch != '-') && (ch <= '\u007E')) {
				this.unreadChar(ch);
				return;
			}
			result.append(ch);
		}
	}

	/**
	 * This method scans an identifier from the current reader.
	 * 
	 * @return the next character following the whitespace.
	 */
	protected char scanWhitespace() throws IOException {
		for (;;) {
			final char ch = this.readChar();
			switch (ch) {
			case ' ':
			case '\t':
			case '\n':
			case '\r':
				break;
			default:
				return ch;
			}
		}
	}

	/**
	 * This method scans an identifier from the current reader. The scanned
	 * whitespace is appended to <code>result</code>.
	 * 
	 * @return the next character following the whitespace. </dl>
	 *         <dl>
	 *         <dt><b>Preconditions:</b></dt>
	 *         <dd>
	 *         <ul>
	 *         <li><code>result != null</code>
	 *         </ul>
	 *         </dd>
	 *         </dl>
	 */
	protected char scanWhitespace(final StringBuffer result) throws IOException {
		for (;;) {
			final char ch = this.readChar();
			switch (ch) {
			case ' ':
			case '\t':
			case '\n':
				result.append(ch);
			case '\r':
				break;
			default:
				return ch;
			}
		}
	}

	/**
	 * This method scans a delimited string from the current reader. The scanned
	 * string without delimiters is appended to <code>string</code>. </dl>
	 * <dl>
	 * <dt><b>Preconditions:</b></dt>
	 * <dd>
	 * <ul>
	 * <li><code>string != null</code>
	 * <li>the next char read is the string delimiter
	 * </ul>
	 * </dd>
	 * </dl>
	 */
	protected void scanString(final StringBuffer string) throws IOException {
		final char delimiter = this.readChar();
		if ((delimiter != '\'') && (delimiter != '"')) {
			throw this.expectedInput("' or \"");
		}
		for (;;) {
			final char ch = this.readChar();
			if (ch == delimiter) {
				return;
			} else if (ch == '&') {
				this.resolveEntity(string);
			} else {
				string.append(ch);
			}
		}
	}

	/**
	 * Scans a #PCDATA element. CDATA sections and entities are resolved. The
	 * next &lt; char is skipped. The scanned data is appended to
	 * <code>data</code>. </dl>
	 * <dl>
	 * <dt><b>Preconditions:</b></dt>
	 * <dd>
	 * <ul>
	 * <li><code>data != null</code>
	 * </ul>
	 * </dd>
	 * </dl>
	 */
	protected void scanPCData(final StringBuffer data) throws IOException {
		for (;;) {
			char ch = this.readChar();
			if (ch == '<') {
				ch = this.readChar();
				if (ch == '!') {
					this.checkCDATA(data);
				} else {
					this.unreadChar(ch);
					return;
				}
			} else if (ch == '&') {
				this.resolveEntity(data);
			} else {
				data.append(ch);
			}
		}
	}

	/**
	 * Scans a special tag and if the tag is a CDATA section, append its content
	 * to <code>buf</code>. </dl>
	 * <dl>
	 * <dt><b>Preconditions:</b></dt>
	 * <dd>
	 * <ul>
	 * <li><code>buf != null</code>
	 * <li>The first &lt; has already been read.
	 * </ul>
	 * </dd>
	 * </dl>
	 */
	protected boolean checkCDATA(final StringBuffer buf) throws IOException {
		char ch = this.readChar();
		if (ch != '[') {
			this.unreadChar(ch);
			this.skipSpecialTag(0);
			return false;
		} else if (!this.checkLiteral("CDATA[")) {
			this.skipSpecialTag(1); // one [ has already been read
			return false;
		} else {
			int delimiterCharsSkipped = 0;
			while (delimiterCharsSkipped < 3) {
				ch = this.readChar();
				switch (ch) {
				case ']':
					if (delimiterCharsSkipped < 2) {
						delimiterCharsSkipped += 1;
					} else {
						buf.append(']');
						buf.append(']');
						delimiterCharsSkipped = 0;
					}
					break;
				case '>':
					if (delimiterCharsSkipped < 2) {
						for (int i = 0; i < delimiterCharsSkipped; i++) {
							buf.append(']');
						}
						delimiterCharsSkipped = 0;
						buf.append('>');
					} else {
						delimiterCharsSkipped = 3;
					}
					break;
				default:
					for (int i = 0; i < delimiterCharsSkipped; i += 1) {
						buf.append(']');
					}
					buf.append(ch);
					delimiterCharsSkipped = 0;
				}
			}
			return true;
		}
	}

	/**
	 * Skips a comment. </dl>
	 * <dl>
	 * <dt><b>Preconditions:</b></dt>
	 * <dd>
	 * <ul>
	 * <li>The first &lt;!-- has already been read.
	 * </ul>
	 * </dd>
	 * </dl>
	 */
	protected void skipComment() throws IOException {
		int dashesToRead = 2;
		while (dashesToRead > 0) {
			final char ch = this.readChar();
			if (ch == '-') {
				dashesToRead -= 1;
			} else {
				dashesToRead = 2;
			}
		}
		if (this.readChar() != '>') {
			throw this.expectedInput(">");
		}
	}

	/**
	 * Skips a special tag or comment.
	 * 
	 * @param bracketLevel
	 *            The number of open square brackets ([) that have already been
	 *            read. </dl>
	 *            <dl>
	 *            <dt><b>Preconditions:</b></dt>
	 *            <dd>
	 *            <ul>
	 *            <li>The first &lt;! has already been read.
	 *            <li><code>bracketLevel >= 0</code>
	 *            </ul>
	 *            </dd>
	 *            </dl>
	 */
	protected void skipSpecialTag(int bracketLevel) throws IOException {
		int tagLevel = 1; // <
		char stringDelimiter = '\0';
		if (bracketLevel == 0) {
			char ch = this.readChar();
			if (ch == '[') {
				bracketLevel += 1;
			} else if (ch == '-') {
				ch = this.readChar();
				if (ch == '[') {
					bracketLevel += 1;
				} else if (ch == ']') {
					bracketLevel -= 1;
				} else if (ch == '-') {
					this.skipComment();
					return;
				}
			}
		}
		while (tagLevel > 0) {
			final char ch = this.readChar();
			if (stringDelimiter == '\0') {
				if ((ch == '"') || (ch == '\'')) {
					stringDelimiter = ch;
				} else if (bracketLevel <= 0) {
					if (ch == '<') {
						tagLevel += 1;
					} else if (ch == '>') {
						tagLevel -= 1;
					}
				}
				if (ch == '[') {
					bracketLevel += 1;
				} else if (ch == ']') {
					bracketLevel -= 1;
				}
			} else {
				if (ch == stringDelimiter) {
					stringDelimiter = '\0';
				}
			}
		}
	}

	/**
	 * Scans the data for literal text. Scanning stops when a character does not
	 * match or after the complete text has been checked, whichever comes first.
	 * 
	 * @param literal
	 *            the literal to check. </dl>
	 *            <dl>
	 *            <dt><b>Preconditions:</b></dt>
	 *            <dd>
	 *            <ul>
	 *            <li><code>literal != null</code>
	 *            </ul>
	 *            </dd>
	 *            </dl>
	 */
	protected boolean checkLiteral(final String literal) throws IOException {
		final int length = literal.length();
		for (int i = 0; i < length; i += 1) {
			if (this.readChar() != literal.charAt(i)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Reads a character from a reader.
	 */
	protected char readChar() throws IOException {
		if (this.charReadTooMuch != '\0') {
			final char ch = this.charReadTooMuch;
			this.charReadTooMuch = '\0';
			return ch;
		} else {
			final int i = this.reader.read();
			if (i < 0) {
				throw this.unexpectedEndOfData();
			} else if (i == 10) {
				this.parserLineNr += 1;
				return '\n';
			} else {
				return (char) i;
			}
		}
	}

	/**
	 * Scans an XML element.
	 * 
	 * @param elt
	 *            The element that will contain the result. </dl>
	 *            <dl>
	 *            <dt><b>Preconditions:</b></dt>
	 *            <dd>
	 *            <ul>
	 *            <li>The first &lt; has already been read.
	 *            <li><code>elt != null</code>
	 *            </ul>
	 *            </dd>
	 *            </dl>
	 */
	protected void scanElement(final XMLElement elt) throws IOException {
		final StringBuffer buf = new StringBuffer();
		this.scanIdentifier(buf);
		final String name = buf.toString();
		elt.setName(name);
		char ch = this.scanWhitespace();
		while ((ch != '>') && (ch != '/')) {
			buf.setLength(0);
			this.unreadChar(ch);
			this.scanIdentifier(buf);
			final String key = buf.toString();
			ch = this.scanWhitespace();
			if (ch != '=') {
				throw this.expectedInput("=");
			}
			this.unreadChar(this.scanWhitespace());
			buf.setLength(0);
			this.scanString(buf);
			elt.setAttribute(key, buf);
			ch = this.scanWhitespace();
		}
		if (ch == '/') {
			ch = this.readChar();
			if (ch != '>') {
				throw this.expectedInput(">");
			}
			return;
		}
		buf.setLength(0);
		ch = this.scanWhitespace(buf);
		if (ch != '<') {
			this.unreadChar(ch);
			this.scanPCData(buf);
		} else {
			for (;;) {
				ch = this.readChar();
				if (ch == '!') {
					if (this.checkCDATA(buf)) {
						this.scanPCData(buf);
						break;
					} else {
						ch = this.scanWhitespace(buf);
						if (ch != '<') {
							this.unreadChar(ch);
							this.scanPCData(buf);
							break;
						}
					}
				} else {
					if ((ch != '/') || this.ignoreWhitespace) {
						buf.setLength(0);
					}
					if (ch == '/') {
						this.unreadChar(ch);
					}
					break;
				}
			}
		}
		if (buf.length() == 0) {
			while (ch != '/') {
				if (ch == '!') {
					ch = this.readChar();
					if (ch != '-') {
						throw this.expectedInput("Comment or Element");
					}
					ch = this.readChar();
					if (ch != '-') {
						throw this.expectedInput("Comment or Element");
					}
					this.skipComment();
				} else {
					this.unreadChar(ch);
					final XMLElement child = this.createAnotherElement();
					this.scanElement(child);
					elt.addChild(child);
				}
				ch = this.scanWhitespace();
				if (ch != '<') {
					throw this.expectedInput("<");
				}
				ch = this.readChar();
			}
			this.unreadChar(ch);
		} else {
			if (this.ignoreWhitespace) {
				elt.setContent(buf.toString().trim());
			} else {
				elt.setContent(buf.toString());
			}
		}
		ch = this.readChar();
		if (ch != '/') {
			throw this.expectedInput("/");
		}
		this.unreadChar(this.scanWhitespace());
		if (!this.checkLiteral(name)) {
			throw this.expectedInput(name);
		}
		if (this.scanWhitespace() != '>') {
			throw this.expectedInput(">");
		}
	}

	/**
	 * Resolves an entity. The name of the entity is read from the reader. The
	 * value of the entity is appended to <code>buf</code>.
	 * 
	 * @param buf
	 *            Where to put the entity value. </dl>
	 *            <dl>
	 *            <dt><b>Preconditions:</b></dt>
	 *            <dd>
	 *            <ul>
	 *            <li>The first &amp; has already been read.
	 *            <li><code>buf != null</code>
	 *            </ul>
	 *            </dd>
	 *            </dl>
	 */
	protected void resolveEntity(final StringBuffer buf) throws IOException {
		char ch = '\0';
		final StringBuffer keyBuf = new StringBuffer();
		for (;;) {
			ch = this.readChar();
			if (ch == ';') {
				break;
			}
			keyBuf.append(ch);
		}
		final String key = keyBuf.toString();
		if (key.charAt(0) == '#') {
			try {
				if (key.charAt(1) == 'x') {
					ch = (char) Integer.parseInt(key.substring(2), 16);
				} else {
					ch = (char) Integer.parseInt(key.substring(1), 10);
				}
			} catch (final NumberFormatException e) {
				throw this.unknownEntity(key);
			}
			buf.append(ch);
		} else {
			final char[] value = this.entities.get(key);
			if (value == null) {
				throw this.unknownEntity(key);
			}
			buf.append(value);
		}
	}

	/**
	 * Pushes a character back to the read-back buffer.
	 * 
	 * @param ch
	 *            The character to push back. </dl>
	 *            <dl>
	 *            <dt><b>Preconditions:</b></dt>
	 *            <dd>
	 *            <ul>
	 *            <li>The read-back buffer is empty.
	 *            <li><code>ch != '\0'</code>
	 *            </ul>
	 *            </dd>
	 *            </dl>
	 */
	protected void unreadChar(final char ch) {
		this.charReadTooMuch = ch;
	}

	/**
	 * Creates a parse exception for when an invalid valueset is given to a
	 * method.
	 * 
	 * @param name
	 *            The name of the entity. </dl>
	 *            <dl>
	 *            <dt><b>Preconditions:</b></dt>
	 *            <dd>
	 *            <ul>
	 *            <li><code>name != null</code>
	 *            </ul>
	 *            </dd>
	 *            </dl>
	 */
	protected XMLParseException invalidValueSet(final String name) {
		final String msg = "Invalid value set (entity name = \"" + name + "\")";
		return new XMLParseException(this.getName(), this.parserLineNr, msg);
	}

	/**
	 * Creates a parse exception for when an invalid value is given to a method.
	 * 
	 * @param name
	 *            The name of the entity.
	 * @param value
	 *            The value of the entity. </dl>
	 *            <dl>
	 *            <dt><b>Preconditions:</b></dt>
	 *            <dd>
	 *            <ul>
	 *            <li><code>name != null</code>
	 *            <li><code>value != null</code>
	 *            </ul>
	 *            </dd>
	 *            </dl>
	 */
	protected XMLParseException invalidValue(final String name,
			final String value) {
		final String msg =
			"Attribute \"" + name + "\" does not contain a valid "
				+ "value (\"" + value + "\")";
		return new XMLParseException(this.getName(), this.parserLineNr, msg);
	}

	/**
	 * Creates a parse exception for when the end of the data input has been
	 * reached.
	 */
	protected XMLParseException unexpectedEndOfData() {
		final String msg = "Unexpected end of data reached";
		return new XMLParseException(this.getName(), this.parserLineNr, msg);
	}

	/**
	 * Creates a parse exception for when a syntax error occured.
	 * 
	 * @param context
	 *            The context in which the error occured. </dl>
	 *            <dl>
	 *            <dt><b>Preconditions:</b></dt>
	 *            <dd>
	 *            <ul>
	 *            <li><code>context != null</code>
	 *            <li><code>context.length() &gt; 0</code>
	 *            </ul>
	 *            </dd>
	 *            </dl>
	 */
	protected XMLParseException syntaxError(final String context) {
		final String msg = "Syntax error while parsing " + context;
		return new XMLParseException(this.getName(), this.parserLineNr, msg);
	}

	/**
	 * Creates a parse exception for when the next character read is not the
	 * character that was expected.
	 * 
	 * @param charSet
	 *            The set of characters (in human readable form) that was
	 *            expected. </dl>
	 *            <dl>
	 *            <dt><b>Preconditions:</b></dt>
	 *            <dd>
	 *            <ul>
	 *            <li><code>charSet != null</code>
	 *            <li><code>charSet.length() &gt; 0</code>
	 *            </ul>
	 *            </dd>
	 *            </dl>
	 */
	protected XMLParseException expectedInput(final String charSet) {
		final String msg = "Expected: " + charSet;
		return new XMLParseException(this.getName(), this.parserLineNr, msg);
	}

	/**
	 * Creates a parse exception for when an entity could not be resolved.
	 * 
	 * @param name
	 *            The name of the entity. </dl>
	 *            <dl>
	 *            <dt><b>Preconditions:</b></dt>
	 *            <dd>
	 *            <ul>
	 *            <li><code>name != null</code>
	 *            <li><code>name.length() &gt; 0</code>
	 *            </ul>
	 *            </dd>
	 *            </dl>
	 */
	protected XMLParseException unknownEntity(final String name) {
		final String msg = "Unknown or invalid entity: &" + name + ";";
		return new XMLParseException(this.getName(), this.parserLineNr, msg);
	}

}
