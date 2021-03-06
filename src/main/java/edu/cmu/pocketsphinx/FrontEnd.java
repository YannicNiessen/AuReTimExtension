/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 4.0.1
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package edu.cmu.pocketsphinx;

public class FrontEnd {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected FrontEnd(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(FrontEnd obj) {
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
        SphinxBaseJNI.delete_FrontEnd(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public FrontEnd(SWIGTYPE_p_fe_t ptr) {
    this(SphinxBaseJNI.new_FrontEnd(SWIGTYPE_p_fe_t.getCPtr(ptr)), true);
  }

  public int outputSize() {
    return SphinxBaseJNI.FrontEnd_outputSize(swigCPtr, this);
  }

  public void startUtt() {
    SphinxBaseJNI.FrontEnd_startUtt(swigCPtr, this);
  }

  public int processUtt(SWIGTYPE_p_int16 spch, long nsamps, SWIGTYPE_p_p_p_mfcc_t cepBlock) {
    return SphinxBaseJNI.FrontEnd_processUtt(swigCPtr, this, SWIGTYPE_p_int16.getCPtr(spch), nsamps, SWIGTYPE_p_p_p_mfcc_t.getCPtr(cepBlock));
  }

  public int endUtt(SWIGTYPE_p_mfcc_t outCepvector) {
    return SphinxBaseJNI.FrontEnd_endUtt(swigCPtr, this, SWIGTYPE_p_mfcc_t.getCPtr(outCepvector));
  }

}
