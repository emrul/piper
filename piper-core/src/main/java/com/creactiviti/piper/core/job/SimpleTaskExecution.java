
package com.creactiviti.piper.core.job;

import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

import com.creactiviti.piper.core.DSL;
import com.creactiviti.piper.core.error.Error;
import com.creactiviti.piper.core.error.ErrorObject;
import com.creactiviti.piper.core.error.Prioritizable;
import com.creactiviti.piper.core.task.PipelineTask;
import com.creactiviti.piper.core.task.SimplePipelineTask;
import com.creactiviti.piper.core.task.Task;
import com.creactiviti.piper.core.task.TaskExecution;
import com.creactiviti.piper.core.task.TaskStatus;
import com.creactiviti.piper.core.uuid.UUIDGenerator;


public class SimpleTaskExecution extends SimplePipelineTask implements TaskExecution {

  private SimpleTaskExecution () {
    this(Collections.EMPTY_MAP);
  }
  
  private SimpleTaskExecution (TaskExecution aSource) {
    this(aSource.asMap());
  }
  
  private SimpleTaskExecution (Map<String,Object> aSource) {
    super(aSource);
  }  
  
  private SimpleTaskExecution (Task aSource) {
    super(aSource);
  }
  
  @Override
  public String getId() {
    return getString(DSL.ID);
  }
  
  public void setId (String aId) {
    set(DSL.ID, aId);
  }
  
  @Override
  public String getParentId() {
    return getString(DSL.PARENT_ID);
  }
  
  public void setParentId (String aParentId) {
    set(DSL.PARENT_ID, aParentId);
  }
  
  @Override
  public String getJobId() {
    return getString(DSL.JOB_ID);
  }

  public void setJobId (String aJobId) {
    set(DSL.JOB_ID, aJobId);
  }

  @Override
  public TaskStatus getStatus() {
    String status = getString(DSL.STATUS);
    if(status == null) return null;
    return TaskStatus.valueOf(status);
  }
  
  @Override
  public Object getOutput() {
    return get(DSL.OUTPUT);
  }
  
  public void setOutput (Object aOutput) {
    set(DSL.OUTPUT, aOutput);
  }
  
  @Override
  public Error getError() {
    if(containsKey(DSL.ERROR)) {
      return new ErrorObject(getMap(DSL.ERROR));
    }
    return null;
  }
  
  public void setError (Error aError) {
    set(DSL.ERROR, aError);
  }
  
  public void setStatus (TaskStatus aStatus) {
    set(DSL.STATUS,aStatus);
  }
  
  @Override
  public Date getCreateTime() {
    return getDate(DSL.CREATE_TIME);
  }
  
  public void setCreateTime(Date aDate) { 
    set(DSL.CREATE_TIME, aDate);
  }
  
  @Override
  public Date getStartTime() {
    return getDate(DSL.START_TIME);
  }

  public void setStartTime(Date aDate) {
    set(DSL.START_TIME, aDate);
  }
  
  @Override
  public Date getEndTime() {
    return getDate(DSL.END_TIME);
  }
  
  public void setEndTime(Date aDate) {
    set(DSL.END_TIME, aDate);
  }

  @Override
  public long getExecutionTime() {
    if(getDate(DSL.EXECUTION_TIME)!=null) {
      return getDate(DSL.EXECUTION_TIME).getTime();
    }
    return 0;
  }
  
  public void setExecutionTime (long aExecutionTime) {
    set(DSL.EXECUTION_TIME, aExecutionTime);
  }
  
  @Override
  public int getRetry() {
    return getInteger(DSL.RETRY, 0);
  }
  
  @Override
  public int getRetryAttempts() {
    return getInteger(DSL.RETRY_ATTEMPTS, 0);
  }
  
  @Override
  public String getRetryDelay() {
    return getString(DSL.RETRY_DELAY,"1s");
  }
  
  @Override
  public long getRetryDelayMillis () {
    long delay = Duration.parse("PT" + getRetryDelay()).toMillis();
    int retryAttempts = getRetryAttempts();
    int retryDelayFactor = getRetryDelayFactor();
    return delay * retryAttempts * retryDelayFactor;
  }
  
  public void setRetryAttempts (int aRetryAttempts) {
    set(DSL.RETRY_ATTEMPTS, aRetryAttempts);
  }
  
  @Override
  public int getRetryDelayFactor() {
    return getInteger(DSL.RETRY_DELAY_FACTOR,2);
  }
  
  @Override
  public int getPriority() {
    return getInteger(DSL.PRIORTIY, Prioritizable.DEFAULT_PRIORITY);
  }
  
  public void setPriority (int aPriority) {
    set(DSL.PRIORTIY, aPriority);
  }
    
  /**
   * Creates a new {@link SimpleTaskExecution} instance 
   * from a {@link PipelineTask}.
   * 
   * @param aTask
   *         The {@link PipelineTask} to create this instance from.
   * @return {@link SimpleTaskExecution}
   */
  public static SimpleTaskExecution createFrom (PipelineTask aTask) {
    SimpleTaskExecution jobTask = new SimpleTaskExecution (aTask);
    jobTask.setCreateTime(new Date());
    jobTask.setId(UUIDGenerator.generate());
    jobTask.setStatus(TaskStatus.CREATED);
    return jobTask;
  }
    
  /**
   * Creates a new {@link SimpleTaskExecution} instance, using the 
   * given {@link TaskExecution} instance as a starting point. 
   * 
   * @param aJobTask
   *          The {@link TaskExecution} instance to use as a starting 
   *          point.
   * @return the new {@link SimpleTaskExecution}
   */
  public static SimpleTaskExecution createNewFrom (TaskExecution aJobTask) {
    SimpleTaskExecution mutableJobTask = new SimpleTaskExecution(aJobTask);
    mutableJobTask.setId(UUIDGenerator.generate());
    mutableJobTask.setCreateTime(new Date());
    mutableJobTask.setStatus(TaskStatus.CREATED);
    mutableJobTask.setError(null);
    return mutableJobTask;
  }

  /**
   * Creates a {@link SimpleTaskExecution} instance which 
   * is a copy of a {@link TaskExecution}.
   * 
   * @param aJobTask
   *          The {@link TaskExecution} instance to copy.
   * @return the new {@link SimpleTaskExecution}
   */
  public static SimpleTaskExecution createForUpdate (TaskExecution aJobTask) {
    return new SimpleTaskExecution(aJobTask);
  }
  
  /**
   * Creates an empty {@link SimpleTaskExecution} instance.
   * 
   * @return The new {@link SimpleTaskExecution}.
   */
  public static SimpleTaskExecution create () {
    return new SimpleTaskExecution();
  }
  
  /**
   * Creates a {@link SimpleTaskExecution} instance for the given 
   * Key-Value pair.
   * 
   * @return The new {@link SimpleTaskExecution}.
   */
  public static SimpleTaskExecution createFrom (String aKey, Object aValue) {
    return new SimpleTaskExecution(Collections.singletonMap(aKey, aValue));
  }
  
  /**
   * Creates a {@link SimpleTaskExecution} instance for the given Key-Value
   * map.
   * 
   * @return The new {@link SimpleTaskExecution}.
   */
  public static SimpleTaskExecution createFromMap (Map<String,Object> aSource) {
    return new SimpleTaskExecution(aSource);
  }
  
}