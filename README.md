RAKE-Java
=====================

A Java 8 implementation of the Rapid Automatic Keyword Extraction (RAKE) algorithm as described in: Rose, S., Engel, D., Cramer, N., & Cowley, W. (2010). Automatic Keyword Extraction from Individual Documents. In M. W. Berry & J. Kogan (Eds.), Text Mining: Theory and Applications: John Wiley & Sons.

The implementation is based on the python one from https://github.com/aneesha/RAKE (however some changes have been made)
The source code is released under the GPL V3License. 

This implementation requires a POS tagger to be used in order to work. For example The Illinois POS tagger could be used for English.

http://cogcomp.cs.illinois.edu/page/software_view/POS

For Spanish or other languages: 

FreeLing --> http://nlp.lsi.upc.edu/freeling/ 

or Standford Pos tagger --> http://nlp.stanford.edu/software/tagger.shtml

could be used as tagger.

The implementation is in beta state 

TODO: 

     - More testing 


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

Then RAKE can be processed then.....


```java

    Document doc=new Document(full_path,name);
    doc.setSentenceList(sentences);
    doc.setTokenList(tokenized_sentences); 
    RakeAlgorithm ex = new RakeAlgorithm();
    ex.loadStopWordsList("resources/lite/stopWordLists/RakeStopLists/SmartStopListEn");
    ex.loadPunctStopWord("resources/lite/stopWordLists/RakeStopLists/RakePunctDefaultStopList");
    PlainTextDocumentReaderLBJEn parser = new PlainTextDocumentReaderLBJEn();
    parser.readSource("testCorpus/textAstronomy");
    Document doc = new Document("full_path", "name");
    ex.init(doc);
    ex.runAlgorithm();
    doc.getTermList();
```



