package fr.mcc.ginco.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Custom bean that represent the Git State of the build.
 *
 */
@Service("gitInfoService")
public class GitInfo {

	private @Value("${git.commit.id}") String commitId;
	private @Value("${git.branch}") String gitBranch;
	private @Value("${git.build.time}") String gitBuildTime;
	private @Value("${git.commit.user.name}") String gitCommitUserName;

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
