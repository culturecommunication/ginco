/**
 * Copyright or © or Copr. Ministère Français chargé de la Culture
 * et de la Communication (2013)
 * <p/>
 * contact.gincoculture_at_gouv.fr
 * <p/>
 * This software is a computer program whose purpose is to provide a thesaurus
 * management solution.
 * <p/>
 * This software is governed by the CeCILL license under French law and
 * abiding by the rules of distribution of free software. You can use,
 * modify and/ or redistribute the software under the terms of the CeCILL
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info".
 * <p/>
 * As a counterpart to the access to the source code and rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty and the software's author, the holder of the
 * economic rights, and the successive licensors have only limited liability.
 * <p/>
 * In this respect, the user's attention is drawn to the risks associated
 * with loading, using, modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean that it is complicated to manipulate, and that also
 * therefore means that it is reserved for developers and experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systemsand/or
 * data to be ensured and, more generally, to use and operate it in the
 * same conditions as regards security.
 * <p/>
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL license and that you accept its terms.
 */
package fr.mcc.ginco.tests.services;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;

import fr.mcc.ginco.utils.GitInfo;


public class GitInfoServiceTest {	
	
	GitInfo gitInfo = new GitInfo();
	
	@Value("${git.commit.id}") private String commitId;
	@Value("${git.branch}") private String gitBranch;
	@Value("${git.build.time}") private String buildTime;
	@Value("${git.commit.user.name}") private String commitUsername;
	@Value("${mvn.project.version}") private String mvnVersion;

	 /**
	 * Test getting Git Commit Id from properties
	 */
	@Test
	 public final void getGitCommitId(){
		 String expectedResponse= commitId ;
		 String actualResponse = gitInfo.getCommitId();
		 Assert.assertEquals("Error while getting git build time !", expectedResponse, actualResponse);
	 }
	
	/**
	 * Test getting Git Branch from properties
	 */
	@Test
	 public final void getGitBranch(){
		 String expectedResponse= gitBranch ;
		 String actualResponse = gitInfo.getGitBranch();
		 Assert.assertEquals("Error while getting git build time !", expectedResponse, actualResponse);
	 }
	
	/**
	 * Test getting Git build time from properties
	 */
	@Test
	 public final void getGitBuildTime(){
		 String expectedResponse= buildTime ;
		 String actualResponse = gitInfo.getGitBuildTime();
		 Assert.assertEquals("Error while getting git build time !", expectedResponse, actualResponse);
	 }
	
	/**
	 * Test getting Git Commit Username from properties
	 */
	@Test
	 public final void getGitCommitUsername(){
		 String expectedResponse= commitUsername ;
		 String actualResponse = gitInfo.getGitCommitUserName();
		 Assert.assertEquals("Error while getting git build time !", expectedResponse, actualResponse);
	 }
	
	/**
	 * Test getting Version from properties
	 */
	@Test
	 public final void getMavenVersion(){
		 String expectedResponse= mvnVersion ;
		 String actualResponse = gitInfo.getMavenVersion();
		 Assert.assertEquals("Error while getting version !", expectedResponse, actualResponse);
	 }
	
	
}