
========================================================================

Thirteenth Conference on Computational Natural Language Learning 
                            (CoNLL 2009)
            Shared Task Distribution -- Official Release
                http://ufal.mff.cuni.cz/conll2009-st/

Version 1.0: January 11, 2009

Organizers of this corpus:
Richard Johanson
Adam Meyers
Mihai Surdeanu
Lluis Marquez
Joakim Nivre

========================================================================

WARNING

The data of this distribution uses portions of the Penn Treebank II 
collection. For participants not owning a valid license of the Penn 
Treebank II collection, LDC is providing an "evaluation license",
valid during competition time, which allows the free download and use 
of this CoNLL-2009 shared task dataset. See the shared task website
for details.


(1) GENERAL

This distribution includes the data set for the English language, part
of the CoNLL 2009 shared task. The corpus includes syntactic dependencies 
(from the Penn Treebank [TB]) and semantic dependencies (from PropBank 
[PB] and NomBank [NB]). 


(2) LIST OF CHANGES
<void>


(3) CONTENT OF THIS DISTRIBUTION

The following files are included in this distribution:
* README.TXT - this file
* CoNLL2009-ST-English-trial.txt - the trial corpus. It contains the first 
  506 lines of the development file.
* CoNLL2009-ST-English-train.txt - the training corpus. It matches sections
  2 through 21 of Treebank. Note that a few sentences from the original 
  Treebank were removed due to merging problems between the input corpora.
* CoNLL2009-ST-English-development.txt - the development corpus. It matches
  section 24 of Treebank.
* pb_frames.tar.gz - English verbal lexicon. Contains the set of accepted
  argument frames for each verbal predicate in the training and 
  development corpora.
* nb_frames.tar.gz - English nominal lexicon. Contains the set of accepted
  argument frames for each nominal predicate in the training and
  development corpora.


(4) SPECIFICS OF THE ENGLISH DATA SET

The special features of this corpus are:
* Dependency trees are not always projective, although the vast majority 
  of trees are projective.
* Both verbal predicates (from PropBank) and nominal predicates (from
  NomBank) are annotated.
* The same word can be an argument to multiple predicates.

For more details please see Section 3 of Surdeanu et al (2008).


(5) PREPROCESSING SYSTEMS

The input annotations provided for both closed and open challenges are
generated using the following state-of-the-art systems:

*) The predicted Part-of-Speech (PoS) tags (i.e., the PPOS column)
   are generated using the PoS tagger of (Gimenez and Marquez 2004). 

*) The lemmas (LEMMA and PLEMMA columns) are extracted from WordNet
   using the most common sense for the corresponding tag. The difference 
   between LEMMA and PLEMMA is that LEMMA is generated using the POS tag 
   from the POS column, whereas PLEMMA is generated using the PPOS column.

*) Columns PHEAD and PDEPREL are generated using the MALT parser (Nivre et
   al 2006).


(6) DIFFERENCES FROM 2008

Most of the data in this corpus is extracted from the CoNLL 2008 data set.
The columns FORM, PLEMMA, PPOS, HEAD, DEPREL, PRED, and APRED are copied 
from the closed-challenge data set of 2008 (note that this year we discard 
the original Treebank tokenization and use only the SPLIT tokenization). 

The PHEAD and PDEPREL are copied from last year's open-challenge data set,
i.e., they are the output of the MALT parser.

The POS column contains the gold POS tags from NomBank. Note that we use
NomBank rather Treebank POS data because NomBank fixes some annotation 
errors in Treebank. Additionally, for the split cases where Treebank 
annotation is not available we set the POS tags using rules based on the
immediate syntactic environment and lexical/morphological factors.

The LEMMA column is generated as the lemma of the most common WN sense 
for the gold POS tag (the POS column). 

The FEAT and PFEAT columns are empty for English.


REFERENCES

(Ciaramita and Altun 2006)
    M. Ciaramita and Y. Altun
    "Broad Coverage Sense Disambiguation and Information Extraction
        with a Supersense Sequence Tagger"
    Proc. of EMNLP, 2006

(Gimenez and Marquez 2004)
    Gimenez J. and Marquez L. 
    "SVMTool: A general POS tagger generator based on 
        Support Vector Machines" 
    Proc. of LREC, 2004

(Nivre et al 2006)
    Nivre J., Hall J., Nilsson J. and Eryigit G. 
    "Labeled Pseudo-Projective Dependency Parsing with 
        Support Vector Machines"
    Proc. of the CoNLL-X Shared Task, 2006

(Surdeanu et al 2008)
    Surdeanu M., Johansson R., Meyers A., Marquez L. and Nivre J.
    "The CoNLL-2008 Shared Task on Joint Parsing of Syntactic and 
        Semantic Dependencies"
    Proc. of CoNLL, 2008

(Tjong Kim Sang and De Meulder 2003)
    Erik F. Tjong Kim Sang and Fien De Meulder
    "Introduction to the CoNLL-2003 Shared Task: 
        Language-Independent Named Entity Recognition"
    Proc. of CoNLL-2003, 2003

[PB] PropBank Project: http://verbs.colorado.edu/~mpalmer/projects/ace.html

[NB] NomBank Project: http://nlp.cs.nyu.edu/meyers/NomBank.html

[TB] Penn Treebank II Project: http://www.cis.upenn.edu/~treebank

[BBN] Pronoun coreference and entity type corpus:
      LDC catalog number LDC2005T33

[WN] WordNet: http://wordnet.princeton.edu/


ACKNOWLEDGMENTS

The organizers thank Massimiliano Ciaramita for the help with his 
semantic tagger and Jesus Gimenez for PoS tagging the corpus.

