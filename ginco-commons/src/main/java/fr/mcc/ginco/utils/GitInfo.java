package fr.mcc.ginco.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Custom bean that represent the Git State of the build.
 *
 */
@Service("gitInfoService")
public class GitInfo {

	@Value("${git.commit.id}") private String commitId;
	@Value("${git.branch}") private String gitBranch;
	@Value("${git.build.time}") private String gitBuildTime;
	@Value("${git.commit.user.name}") private String gitCommitUserName;

	public String getGitBuildTime() {
		return gitBuildTime;
	}

	public void setGitBuildTime(String gitBuildTime) {
		this.gitBuildTime = gitBuildTime;
	}

	public String getGitCommitUserName() {
		return gitCommitUserName;
	}

	public void setGitCommitUserName(String gitCommitUserName) {
		this.gitCommitUserName = gitCommitUserName;
	}

	public String getGitBranch() {
		return gitBranch;
	}

	public void setGitBranch(String gitBranch) {
		this.gitBranch = gitBranch;
	}

	public String getCommitId() {
		return commitId;
	}

	public void setCommitId(String commitId) {
		this.commitId = commitId;
	}
}
