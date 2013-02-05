package fr.mcc.ginco.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Custom bean that represent the Git State of the build.
 *
 */
@Service("gitInfoService")
public class GitInfo {

	private @Value("${git.commit.id.describe}") String commitIdDescribe;
	private @Value("${git.branch}") String gitBranch;

	public String getGitBranch() {
		return gitBranch;
	}

	public void setGitBranch(String gitBranch) {
		this.gitBranch = gitBranch;
	}

	public String getCommitIdDescribe() {
		return commitIdDescribe;
	}

	public void setCommitIdDescribe(String commitIdDescribe) {
		this.commitIdDescribe = commitIdDescribe;
	}
}
