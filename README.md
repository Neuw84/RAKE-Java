CValue-TermExtraction
=====================

A free Java implementation of the C-Value algorithm based on this paper:

http://personalpages.manchester.ac.uk/staff/sophia.ananiadou/ijodl2000.pdf

The CValue is an algorithm for keyphrase extraction that has good results without any trainning combining statistical measures with 
POS information. 

It supports English using Penn Treebank POS Tags for english and Spanish using EAGLES tag set.

This implementation requires a POS tagger to be used in order to work. For example The Illinois POS tagger could be used for English. 

http://cogcomp.cs.illinois.edu/page/software_view/POS

For Spanish it has been tested Freeling. 

http://nlp.lsi.upc.edu/freeling/

The implementation has been tested with English and works well, for documents that contains a lot of noise (like the extracted via OCR recognition) there are some fixes but the Filters for english should change to use Regular Expressions like the Spanish one in order to avoid problems with the Java Stack (although 26,000 papers have been tested with the current implementation). 

TODO: 

     - Change the English filters. 
     - Implement the NC-Value measure (will require a corpus)


Then an example parser for english that will provide the required data (using Illinois POS Tagger)


```java

    import LBJ2.nlp.SentenceSplitter;
    import LBJ2.nlp.WordSplitter;
    import LBJ2.nlp.seg.PlainToTokenParser;
    import LBJ2.parse.Parser;
    import edu.illinois.cs.cogcomp.lbj.chunk.Chunker;
    import edu.illinois.cs.cogcomp.lbj.pos.POSTagger;
    import edu.ehu.galan.cvalue.model.Token;
     ......

     List<LinkedList<Token>> tokenizedSentenceList;
     List<String> sentenceList;
     POSTagger tagger = new POSTagger();
     Chunker chunker = new Chunker();
     boolean first = true;
     parser = new PlainToTokenParser(new WordSplitter(new SentenceSplitter(pFile)));
     String sentence = "";
     LinkedList<Token> tokenList = null;
     for (LBJ2.nlp.seg.Token word = (LBJ2.nlp.seg.Token) parser.next(); word != null;
            word = (LBJ2.nlp.seg.Token) parser.next()) {
            String chunked = chunker.discreteValue(word);
            tagger.discreteValue(word);
            if (first) {
                tokenList = new LinkedList<>();
                tokenizedSentenceList.add(tokenList);
                first = false;
            }
            tokenList.add(new Token(word.form, word.partOfSpeech, null, chunked));
            sentence = sentence + " " + (word.form);
            if (word.next == null) {
                sentenceList.add(sentence);
                first = true;
                sentence = "";
            }
     }
     parser.reset();
     
```

Then The CValue can be processed then.....


```java

    Document doc=new Document(full_path,name);
    doc.setSentenceList(sentences);
    doc.setTokenList(tokenized_sentences); 
    CValueAlgortithm cvalue=new CValueAlgortithm();
    cvalue.init(doc); // initializes the algorithm for processing the desired document. 
    cvalue.addNewProcessingFilter(use_one_of_the_provides); //for example the AdjNounFilter
    cvalue.runAlgorithm(); //process the CValue algorithm with the provided filters
    doc.getTermList(); //get the results
```



