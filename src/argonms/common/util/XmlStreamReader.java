/*
 * ArgonMS MapleStory server emulator written in Java
 * Copyright (C) 2011-2013  GoldenKevin
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package argonms.common.util;

import java.io.FileInputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XmlStreamReader {
	private FileInputStream fis;
	private Document document;
	private Node node;
	
	public XmlStreamReader(FileInputStream fis) throws IOException {
		try {
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder;
			documentBuilder = documentBuilderFactory.newDocumentBuilder();
			this.fis = fis;
			this.document = documentBuilder.parse(fis);
		} catch (ParserConfigurationException | SAXException e) {
			throw new IOException(e);
		}
	}
	
	public boolean next() {
		if (this.node == null) {
			this.node = this.document.getFirstChild();
			return true;
		} else if (this.node.getNextSibling() != null) {
			Node node = this.node;
			while (node.getNextSibling() != null) {
				node = node.getNextSibling();
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					this.node = node;
					return true;
				}
			}
			return false;
		} else {
			return false;
		}
	}
	
	public boolean in() {
		if (!this.node.hasChildNodes()) {
			return false;
		}
		NodeList nodes = this.node.getChildNodes();
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				this.node = node;
				return true;
			}
		}
		return false;
	}
	
	public boolean out() {
		if (this.node.getParentNode() == null) {
			return false;
		} else {
			this.node = this.node.getParentNode();
			return true;
		}
	}
	
	public String element() {
		return this.node.getNodeName();
	}
	
	public String get(String attribute) {
		return this.node.getAttributes().getNamedItem(attribute).getNodeValue();
	}
	
	public void close() throws IOException {
		this.fis.close();
	}
}
