
package edu.ehu.galan.rake.model;
/*
 *    Token.java
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


/**
 * A token is a simple "word" containing the word form, POS tag, lemma, etc....
 * 
 * @author Angel Conde Manjon
 */

public class Token{
   private String wordForm;
   private String posTag;
   private String chunkerTag;
   private String lemma;
   private int pos; //position inside the sentence?
    
   /**
     *
     * @param pWordForm
     */
    public Token(String pWordForm){
       wordForm=pWordForm;
   }
   
    /**
     *
     * @param pWordForm
     * @param pPostag
     */
    public Token(String pWordForm,String pPostag){
       wordForm=pWordForm;
       posTag=pPostag;
   }
       /**
     *
     * @param pWordForm
     * @param pPostag
     * @param pLemma
     */
    public Token(String pWordForm,String pPostag,String pLemma){
       wordForm=pWordForm;
       posTag=pPostag;
       lemma=pLemma;
   }

     /**
     * @param pChunker 
     * @param pWordForm
     * @param pPostag
     * @param pLemma
     */
    public Token(String pWordForm,String pPostag,String pLemma, String  pChunker){
       wordForm=pWordForm;
       posTag=pPostag;
       lemma=pLemma;
       chunkerTag=pChunker;
   }
    /**
     * @return the wordForm
     */
    public String getWordForm() {
        return wordForm;
    }

    /**
     * @param wordForm the wordForm to set
     */
    public void setWordForm(String wordForm) {
        this.wordForm = wordForm;
    }

    /**
     * @return the posTag
     */
    public String getPosTag() {
        return posTag;
    }

    /**
     * @param posTag the posTag to set
     */
    public void setPosTag(String posTag) {
        this.posTag = posTag;
    }

   @Override
   public String toString(){
       return wordForm+ "\t" + posTag;
   }

    /**
     * @return the lemma
     */
    public String getLemma() {
        return lemma;
    }

    /**
     * @param lemma the lemma to set
     */
    public void setLemma(String lemma) {
        this.lemma = lemma;
    }

    /**
     * @return the chunkerTag
     */
    public String getChunkerTag() {
        return chunkerTag;
    }

    /**
     * @param chunkerTag the chunkerTag to set
     */
    public void setChunkerTag(String chunkerTag) {
        this.chunkerTag = chunkerTag;
    }
}
