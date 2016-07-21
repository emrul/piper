package com.creactiviti.piper.core.job;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.creactiviti.piper.core.task.JobTask;

public class SimpleJobRepository implements JobRepository {

  private final Map<String,Job> jobs = new HashMap<>();
  
  @Override
  public Job findOne (String aId) {
    return jobs.get(aId);
  }

  @Override
  public Job save(Job aJob) {
    return jobs.put(aJob.getId(), aJob);
  }
  
  @Override
  public Job findJobByTaskId(String aTaskId) {
    
    for(Job job : jobs.values()) {
      List<JobTask> tasks = job.getTasks();
      for(JobTask t : tasks) {
        if(t.getId().equals(aTaskId)) {
          return job;
        }
      }
    }
    
    return null;
  }

  @Override
  public JobTask nextTask(Job aJob) {
    return null;
  }

  @Override
  public Job updateTask(JobTask aJobTask, Job aJob) {
//    Assert.isTrue(tasks.containsKey(aTask.getId()),"Unkown task: " + aTask.getId());
//    tasks.put(aTask.getId(), aTask);
    return aJob;
  }

  @Override
  public Job updateStatus(Job aJob, JobStatus aStatus) {
    // TODO Auto-generated method stub
    return null;
  }

}
