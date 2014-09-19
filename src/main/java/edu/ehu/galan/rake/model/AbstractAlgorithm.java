package edu.ehu.galan.rake.model;
/*
 *    AbstractAlgorithm.java
 *    Copyright (C) 2013 Angel Conde, neuw84 at gmail dot com
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

import com.google.gson.Gson;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An abstract class that represents an Algorithm for term extraction, all the
 * different extraction methods should extend this
 *
 * @author Angel Conde Manjon
 */
public abstract class AbstractAlgorithm implements Callable<Integer> {
    
    //TODO if we process a corpus instead a document, the termList in each 
    //document is unusable, thing about the model and refactor
    
    private List<Term> termList;
    private boolean scored;
    private transient Document doc;
    private String name;
    private transient Properties properties = null;
    transient final Logger logger = LoggerFactory.getLogger(AbstractAlgorithm.class);

    /**
     *
     * @param pScored - if the results of the algorithm will be scored
     * @param pName - The name of the algorithm
     */
    public AbstractAlgorithm(boolean pScored, String pName) {
        termList = new ArrayList<>();
        scored = pScored;
        name = pName;
    }

    /**
     * Returns the term list
     *
     * @return
     */
    public List<Term> getTermList() {
        return termList;
    }

    /**
     * Returns a list of the terms where all the scores will be > of the passed
     * threshold
     *
     * @param pThreshold
     * @return
     */
    public List<Term> getThresholdedTermList(float pThreshold) {
        if (isScored()) {
            return getTermList().stream().filter((scoredTerm) -> (scoredTerm.getScore() > pThreshold)).collect(Collectors.toList());
        } else {
            logger.warn("You can't get a thresholded list because this is not a scored algorithm");
            return null;
        }
    }

    /**
     * Apply a stopWord list to the term list, will search for the stopword in
     * each term component (if the term is "solar system" will try to match the
     * stopword in each component of the term (solar and system). will filter
     * the term list
     *
     * @param pStopwordList
     */
    public final void applyStopwordList(List<String> pStopwordList) {
        List<Term> stopList = new ArrayList<>();
        boolean stop;
        if (getTermList().size() > 0) {
            for (Term term : getTermList()) {
                String[] nGrams = term.getTerm().split("\\s");
                stop = false;
                for (String string : pStopwordList) {
                    if (nGrams.length == 1) {
                        if (nGrams[0].equalsIgnoreCase(string)) {
                            stop = true;
                            break;
                        }
                    } else {
                        for (String string1 : nGrams) {
                            if (string1.equalsIgnoreCase(string)) {
                                stop = true;
                                break;
                            }
                        }
                    }
                    if (!stop) {
                        stopList.add(term);
                    }
                }
            }
            setTermList(stopList);
        } else {
            logger.info("The term list appears to be empty, have you ran the algorithm?");
        }
    }

    /**
     * will try to match the stopword list to the first component of a multiword
     * term
     *
     * @param pFirstTermStopWordList
     */
    public final void firstTermStopWordList(List<String> pFirstTermStopWordList) {
        List<Term> stopList = new ArrayList<>();
        boolean stop;
        if (getTermList().size() > 0) {
            for (String string : pFirstTermStopWordList) {
                for (Term term : getTermList()) {
                    stop = false;
                    String[] nGrams = term.getTerm().split("\\s");
                    if (nGrams[0].equalsIgnoreCase(string)) {
                        stop = true;
                    }
                    if (!stop) {
                        stopList.add(term);
                    }
                }
            }
            setTermList(stopList);
        } else {
            logger.info("The term list appears to be empty, have you ran the algorithm?");
        }
    }

    /**
     * will try to match the stopword list to the last component of a multiword
     * term
     *
     * @param pFirstTermStopWordList
     */
    public void lastTermStopWordList(List<String> pFirstTermStopWordList) {
        List<Term> stopList = new ArrayList<>();
        boolean stop;
        if (getTermList().size() > 0) {
            for (String string : pFirstTermStopWordList) {
                for (Term term : getTermList()) {
                    stop = false;
                    String[] nGrams = term.getTerm().split("\\s");
                    if (nGrams[nGrams.length - 1].equalsIgnoreCase(string)) {
                        stop = true;
                    }
                    if (!stop) {
                        stopList.add(term);
                    }
                }
            }
            setTermList(stopList);
        } else {
            logger.info("The term list appears to be empty, have you ran the algorithm?");
        }

    }

    /**
     * Prints in the standar output the algorithm results
     */
    public final void print() {
        if (isScored()) {
            getTermList().stream().forEach((scoredTerm) -> {
                System.out.printf("%s \t %f", scoredTerm.getTerm(), scoredTerm.getScore());
            });
        } else {
            getTermList().stream().forEach((scoredTerm) -> {
                System.out.printf("%s \t %f", scoredTerm.getTerm());
            });
        }
    }

    /**
     * The class that represents the action of running an algorithm in a corpus
     * must be implemented
     */
    public abstract void runAlgorithm();

    /**
     * This will be used by the ThreadPool to execute the algorithm and return
     * the results
     *
     * @return List<Term> a list of the extracted terms by the algorithm
     * @throws Exception
     */
    @Override
    public final Integer call() throws Exception {
        runAlgorithm();
        return new Integer(0);
    }

    /**
     * Sets the term list of this algorithm
     *
     * @param termList the termList to set
     */
    public final void setTermList(List<Term> termList) {
        this.termList = termList;
    }

    /**
     * returns whether this algorithm is scored
     *
     * @return the scored
     */
    public final boolean isScored() {
        return scored;
    }

    /**
     * Sets if this algorithm has scored results
     *
     * @param scored the scored to set
     */
    public final void setScored(boolean scored) {
        this.scored = scored;
    }

    /**
     * Returns the document assigned to this algorithm
     *
     * @return the corpus
     */
    public final Document getDocument() {
        return doc;
    }

    /**
     * The corpus that will be processed by the algorithm
     *
     * @param pDoc the document to set
     */
    public final void setDocument(Document pDoc) {
        this.doc = pDoc;
    }

    /**
     * Saves the current term list to tmp folder (configured in the resources
     * folder)
     *
     */
    public void saveToTmp() {
        try (FileWriter outFile = new FileWriter("kpminer")) {
            boolean first = true;
            try (PrintWriter out = new PrintWriter(outFile)) {
                for (Term term : termList) {
                    out.printf("\n%s", term);
                }
            }
        } catch (IOException ex) {
            logger.warn(AbstractAlgorithm.class.getName(), "couldn't save the algorithm results to temp directory", ex);
        }
    }

    /**
     * Return a String with json extracted terms, name of algorithm and whether
     * is scored or not folder)
     *
     * @return - String with the contents of this algorithm in JSON format
     * (name,scored,termlist(
     */
    public String toJson() {
        Gson son = new Gson();
        return son.toJson(this);
    }
    
    
    public void sort(Comparator<Term> comparator){
       termList= this.getTermList().stream().sorted(comparator).collect(Collectors.toList());
    }

    /**
     * Save algorithms results in Json format to tmp directory configured in the 
     * the config
     *
     */
    public void saveGsonToTmp() {
        try ( FileWriter outFile = new FileWriter(properties.getProperty("tmpDir") + File.separator + this.getName() + ".json")){
            try (PrintWriter out = new PrintWriter(outFile)) {
                Gson son = new Gson();
                out.print(son.toJson(this));
            }
        } catch (IOException ex) {
            logger.warn(AbstractAlgorithm.class.getName(), "couldn't save the algorithm results to temp directory in json format", ex);
        }
    }

    /**
     * Return a list of terms from a text file that contains the results of
     * running an algorithm
     *
     * @param pFile
     * @return
     */
    public List<Term> readCandidates(String pFile) {
        //TODO improve the char recognition using YAGO char tools
        List<Term> list = null;
        try {
            Path path = Paths.get(pFile);
            List<String> listC = Files.readAllLines(path, StandardCharsets.UTF_8);
            for (String string : listC) {
                string = string.trim();
                String[] line = string.split(" ");
                String candidate;
                candidate = line[0];
                candidate = candidate.trim();
                float value = Float.parseFloat(line[1]);
                list.add(new Term(candidate, value));
            }
            return list;
        } catch (IOException ex) {
            logger.error(AbstractAlgorithm.class.getName(), "error while reading algorithm results", ex);
        } catch (NullPointerException ex1){
            logger.error(AbstractAlgorithm.class.getName(), "The file is not in the required format", ex1);
        }
        return null;

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
     * Method for class initialization, initializes the document that will
     * be processed for the given algorithm, and the directory where the program
     * is executed (standalone vs web server differences...)
     *
     * @param pDoc
     * @param pPropsDir
     */
    public abstract void init(Document pDoc, String pPropsDir);

    public void setProperties(Properties pProps) {
        properties=pProps;
    }
}
