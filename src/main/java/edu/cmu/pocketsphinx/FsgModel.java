/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 4.0.1
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package edu.cmu.pocketsphinx;

public class FsgModel {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected FsgModel(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(FsgModel obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  @SuppressWarnings("deprecation")
  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        SphinxBaseJNI.delete_FsgModel(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public FsgModel(String name, LogMath logmath, float lw, int n) {
    this(SphinxBaseJNI.new_FsgModel__SWIG_0(name, LogMath.getCPtr(logmath), logmath, lw, n), true);
  }

  public FsgModel(SWIGTYPE_p_fsg_model_t ptr) {
    this(SphinxBaseJNI.new_FsgModel__SWIG_1(SWIGTYPE_p_fsg_model_t.getCPtr(ptr)), true);
  }

  public FsgModel(String path, LogMath logmath, float lw) {
    this(SphinxBaseJNI.new_FsgModel__SWIG_2(path, LogMath.getCPtr(logmath), logmath, lw), true);
  }

  public int wordId(String word) {
    return SphinxBaseJNI.FsgModel_wordId(swigCPtr, this, word);
  }

  public int wordAdd(String word) {
    return SphinxBaseJNI.FsgModel_wordAdd(swigCPtr, this, word);
  }

  public void transAdd(int src, int dst, int logp, int wid) {
    SphinxBaseJNI.FsgModel_transAdd(swigCPtr, this, src, dst, logp, wid);
  }

  public int nullTransAdd(int src, int dst, int logp) {
    return SphinxBaseJNI.FsgModel_nullTransAdd(swigCPtr, this, src, dst, logp);
  }

  public int tagTransAdd(int src, int dst, int logp, int wid) {
    return SphinxBaseJNI.FsgModel_tagTransAdd(swigCPtr, this, src, dst, logp, wid);
  }

  public int addSilence(String silword, int state, float silprob) {
    return SphinxBaseJNI.FsgModel_addSilence(swigCPtr, this, silword, state, silprob);
  }

  public int addAlt(String baseword, String altword) {
    return SphinxBaseJNI.FsgModel_addAlt(swigCPtr, this, baseword, altword);
  }

  public void writefile(String path) {
    SphinxBaseJNI.FsgModel_writefile(swigCPtr, this, path);
  }

}
