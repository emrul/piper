
package com.creactiviti.piper.core.pipeline;

import java.util.List;
import java.util.stream.Collectors;

import com.creactiviti.piper.core.git.GitOperations;
import com.creactiviti.piper.core.git.JGitTemplate;

public class GitPipelineRepository extends YamlPipelineRepository  {

  private String url;
  private String[] searchPaths;
  private GitOperations git = new JGitTemplate();

  
  @Override
  public List<Pipeline> findAll () {
    synchronized(this) {
      List<IdentifiableResource> resources = git.getHeadFiles(url, searchPaths);
      List<Pipeline> pipelines = resources.stream()
                                          .map(r -> parsePipeline(r))
                                          .collect(Collectors.toList());
      return pipelines;
    }
  }

  @Override
  public Pipeline findOne (String aId) {
    synchronized(this) {
      IdentifiableResource resource = git.getFile(url, aId);
      return parsePipeline(resource);
    }
  }

  public void setUrl(String aUrl) {
    url = aUrl;
  }

  public void setSearchPaths(String[] aSearchPaths) {
    searchPaths = aSearchPaths;
  }
  
  public void setGitOperations(GitOperations aGit) {
    git = aGit;
  }
  

}
