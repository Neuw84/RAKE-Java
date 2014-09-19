package edu.ehu.galan.rake.model;

/*
 *    Document.java
 *    Copyright (C) 2014 Angel Conde, neuw84 at gmail dot com
 *
 *    This program is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program; if not, write to the Free Software
 *    Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */


import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A document represents the piece of a corpus containing text.
 *
 * @author Angel Conde Manjon
 */
public class Document {

    private transient String path;
    private transient List<String> sentenceList;
    private transient List<LinkedList<Token>> tokenList;
    private String name;
    private transient List<Term> termList;
    private transient static final Logger logger = LoggerFactory.getLogger(Document.class);

    /**
     *
     * @param pPath
     * @param pName
     */
    public Document(String pPath, String pName) {
        path = pPath;
        name = pName;
        termList = new ArrayList<>();
    }

    /**
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /**
     * @param path the path to set
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * @return the sentenceList
     */
    public List<String> getSentenceList() {
        return sentenceList;
    }

    /**
     * @param sentenceList the sentenceList to set
     */
    public void setSentenceList(List<String> sentenceList) {
        this.sentenceList = sentenceList;
    }

    /**
     * @return the tokenList
     */
    public List<LinkedList<Token>> getTokenList() {
        return tokenList;
    }

    /**
     * @param tokenList the tokenList to set
     */
    public void List(List<LinkedList<Token>> tokenList) {
        this.tokenList = tokenList;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     */
    public List<Term> getTermList() {
        return termList;
    }

    /**
     * Tries to convert the content of this document to UTF-8 using java
     * CharsetDecoders
     */
    public void convertToUTF8() {
        FileInputStream istream = null;
        Writer out = null;
        try {
            istream = new FileInputStream(path);
            BufferedInputStream in = new BufferedInputStream(istream);
            CharsetDecoder charsetDecoder = Charset.forName("UTF-8").newDecoder();
            charsetDecoder.onMalformedInput(CodingErrorAction.REPLACE);
            charsetDecoder.onUnmappableCharacter(CodingErrorAction.REPLACE);
            Reader inputReader = new InputStreamReader(in, charsetDecoder);
            StringWriter writer = new StringWriter();
            IOUtils.copy(inputReader, writer);
            String theString = writer.toString();
            FileUtils.deleteQuietly(new File(path));
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path), "UTF-8"));
            out.write(theString);
            out.close();
//            System.out.println("");
        } catch (FileNotFoundException ex) {
            logger.error("Error converting the file to utf8", ex);
        } catch (IOException ex) {
            logger.error("Error converting the file to utf8", ex);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (istream != null) {
                    istream.close();
                }
            } catch (IOException ex) {
                logger.error("Error converting the file to utf8", ex);
            }
        }

    }

    /**
     * @param termList the termList to set
     */
    public void setTermList(List<Term> termList) {
        this.termList = termList;
    }

}
