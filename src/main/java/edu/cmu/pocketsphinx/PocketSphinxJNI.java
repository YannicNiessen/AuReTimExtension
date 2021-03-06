/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 4.0.1
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package edu.cmu.pocketsphinx;

public class PocketSphinxJNI {
  public final static native void Hypothesis_hypstr_set(long jarg1, Hypothesis jarg1_, String jarg2);
  public final static native String Hypothesis_hypstr_get(long jarg1, Hypothesis jarg1_);
  public final static native void Hypothesis_bestScore_set(long jarg1, Hypothesis jarg1_, int jarg2);
  public final static native int Hypothesis_bestScore_get(long jarg1, Hypothesis jarg1_);
  public final static native void Hypothesis_prob_set(long jarg1, Hypothesis jarg1_, int jarg2);
  public final static native int Hypothesis_prob_get(long jarg1, Hypothesis jarg1_);
  public final static native long new_Hypothesis(String jarg1, int jarg2, int jarg3);
  public final static native void delete_Hypothesis(long jarg1);
  public final static native void Segment_word_set(long jarg1, Segment jarg1_, String jarg2);
  public final static native String Segment_word_get(long jarg1, Segment jarg1_);
  public final static native void Segment_ascore_set(long jarg1, Segment jarg1_, int jarg2);
  public final static native int Segment_ascore_get(long jarg1, Segment jarg1_);
  public final static native void Segment_lscore_set(long jarg1, Segment jarg1_, int jarg2);
  public final static native int Segment_lscore_get(long jarg1, Segment jarg1_);
  public final static native void Segment_lback_set(long jarg1, Segment jarg1_, int jarg2);
  public final static native int Segment_lback_get(long jarg1, Segment jarg1_);
  public final static native void Segment_prob_set(long jarg1, Segment jarg1_, int jarg2);
  public final static native int Segment_prob_get(long jarg1, Segment jarg1_);
  public final static native void Segment_startFrame_set(long jarg1, Segment jarg1_, int jarg2);
  public final static native int Segment_startFrame_get(long jarg1, Segment jarg1_);
  public final static native void Segment_endFrame_set(long jarg1, Segment jarg1_, int jarg2);
  public final static native int Segment_endFrame_get(long jarg1, Segment jarg1_);
  public final static native long Segment_fromIter(long jarg1);
  public final static native void delete_Segment(long jarg1);
  public final static native long new_segment();
  public final static native void NBest_hypstr_set(long jarg1, NBest jarg1_, String jarg2);
  public final static native String NBest_hypstr_get(long jarg1, NBest jarg1_);
  public final static native void NBest_score_set(long jarg1, NBest jarg1_, int jarg2);
  public final static native int NBest_score_get(long jarg1, NBest jarg1_);
  public final static native long NBest_fromIter(long jarg1);
  public final static native long NBest_hyp(long jarg1, NBest jarg1_);
  public final static native void delete_NBest(long jarg1);
  public final static native long new_nBest();
  public final static native void SegmentIterator_ptr_set(long jarg1, SegmentIterator jarg1_, long jarg2);
  public final static native long SegmentIterator_ptr_get(long jarg1, SegmentIterator jarg1_);
  public final static native long new_SegmentIterator(long jarg1);
  public final static native void delete_SegmentIterator(long jarg1);
  public final static native long SegmentIterator_next(long jarg1, SegmentIterator jarg1_);
  public final static native boolean SegmentIterator_hasNext(long jarg1, SegmentIterator jarg1_);
  public final static native void NBestIterator_ptr_set(long jarg1, NBestIterator jarg1_, long jarg2);
  public final static native long NBestIterator_ptr_get(long jarg1, NBestIterator jarg1_);
  public final static native long new_NBestIterator(long jarg1);
  public final static native void delete_NBestIterator(long jarg1);
  public final static native long NBestIterator_next(long jarg1, NBestIterator jarg1_);
  public final static native boolean NBestIterator_hasNext(long jarg1, NBestIterator jarg1_);
  public final static native long new_Decoder__SWIG_0();
  public final static native long new_Decoder__SWIG_1(long jarg1, Config jarg1_);
  public final static native void delete_Decoder(long jarg1);
  public final static native void Decoder_reinit(long jarg1, Decoder jarg1_, long jarg2, Config jarg2_);
  public final static native void Decoder_loadDict(long jarg1, Decoder jarg1_, String jarg2, String jarg3, String jarg4);
  public final static native void Decoder_saveDict(long jarg1, Decoder jarg1_, String jarg2, String jarg3);
  public final static native void Decoder_addWord(long jarg1, Decoder jarg1_, String jarg2, String jarg3, int jarg4);
  public final static native String Decoder_lookupWord(long jarg1, Decoder jarg1_, String jarg2);
  public final static native long Decoder_getLattice(long jarg1, Decoder jarg1_);
  public final static native long Decoder_getConfig(long jarg1, Decoder jarg1_);
  public final static native long Decoder_defaultConfig();
  public final static native long Decoder_fileConfig(String jarg1);
  public final static native void Decoder_startStream(long jarg1, Decoder jarg1_);
  public final static native void Decoder_startUtt(long jarg1, Decoder jarg1_);
  public final static native void Decoder_endUtt(long jarg1, Decoder jarg1_);
  public final static native int Decoder_processRaw(long jarg1, Decoder jarg1_, short[] jarg2, long jarg3, boolean jarg4, boolean jarg5);
  public final static native void Decoder_setRawdataSize(long jarg1, Decoder jarg1_, long jarg2);
  public final static native short[] Decoder_getRawdata(long jarg1, Decoder jarg1_);
  public final static native long Decoder_hyp(long jarg1, Decoder jarg1_);
  public final static native long Decoder_getFe(long jarg1, Decoder jarg1_);
  public final static native long Decoder_getFeat(long jarg1, Decoder jarg1_);
  public final static native boolean Decoder_getInSpeech(long jarg1, Decoder jarg1_);
  public final static native long Decoder_getFsg(long jarg1, Decoder jarg1_, String jarg2);
  public final static native void Decoder_setFsg(long jarg1, Decoder jarg1_, String jarg2, long jarg3, FsgModel jarg3_);
  public final static native void Decoder_setJsgfFile(long jarg1, Decoder jarg1_, String jarg2, String jarg3);
  public final static native void Decoder_setJsgfString(long jarg1, Decoder jarg1_, String jarg2, String jarg3);
  public final static native String Decoder_getKws(long jarg1, Decoder jarg1_, String jarg2);
  public final static native void Decoder_setKws(long jarg1, Decoder jarg1_, String jarg2, String jarg3);
  public final static native void Decoder_setKeyphrase(long jarg1, Decoder jarg1_, String jarg2, String jarg3);
  public final static native void Decoder_setAllphoneFile(long jarg1, Decoder jarg1_, String jarg2, String jarg3);
  public final static native long Decoder_getLm(long jarg1, Decoder jarg1_, String jarg2);
  public final static native void Decoder_setLm(long jarg1, Decoder jarg1_, String jarg2, long jarg3, NGramModel jarg3_);
  public final static native void Decoder_setLmFile(long jarg1, Decoder jarg1_, String jarg2, String jarg3);
  public final static native long Decoder_getLogmath(long jarg1, Decoder jarg1_);
  public final static native void Decoder_setSearch(long jarg1, Decoder jarg1_, String jarg2);
  public final static native String Decoder_getSearch(long jarg1, Decoder jarg1_);
  public final static native int Decoder_nFrames(long jarg1, Decoder jarg1_);
  public final static native long Decoder_seg(long jarg1, Decoder jarg1_);
  public final static native long Decoder_nbest(long jarg1, Decoder jarg1_);
  public final static native long new_Lattice__SWIG_0(String jarg1);
  public final static native long new_Lattice__SWIG_1(long jarg1, Decoder jarg1_, String jarg2);
  public final static native void delete_Lattice(long jarg1);
  public final static native void Lattice_write(long jarg1, Lattice jarg1_, String jarg2);
  public final static native void Lattice_writeHtk(long jarg1, Lattice jarg1_, String jarg2);
  public final static native long NBestList_iterator(long jarg1, NBestList jarg1_);
  public final static native void delete_NBestList(long jarg1);
  public final static native long SegmentList_iterator(long jarg1, SegmentList jarg1_);
  public final static native void delete_SegmentList(long jarg1);
}
