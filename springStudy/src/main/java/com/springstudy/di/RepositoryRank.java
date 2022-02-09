package com.springstudy.di;

import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GHIssueState;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

import java.io.IOException;

public class RepositoryRank {
    /* Test를 위해 PSA DI를 추가해준 부분 Start */
    interface GithubService {
        GitHub connect() throws IOException;
    }

    class DefaultGitHubService implements GithubService {
        @Override
        public GitHub connect() throws IOException {
            return GitHub.connect();
        }
    }

    private DefaultGitHubService gitHubService;

    public RepositoryRank( DefaultGitHubService gitHubService ) {
        this.gitHubService = gitHubService;
    }

    /* Test를 위해 PSA DI를 추가해준 부분 End */

    public int getPoint( String repositoryName) throws IOException {

        //GitHub gitHub = GitHub.connect();  // TEST시 외부 라이브러리를 사용하기 때문에 문제가 됨
        GitHub gitHub = gitHubService.connect();
        GHRepository repository = gitHub.getRepository(repositoryName);

        int points = 0;
        if (repository.hasIssues()) {
            points += 1;
        }

        if (repository.getReadme() != null) {
            points += 1;
        }

        if (repository.getPullRequests(GHIssueState.CLOSED).size() > 0) {
            points += 1;
        }

        points += repository.getStargazersCount();
        points += repository.getForksCount();

        return points;
    }

    public static void main( String[] args ) throws IOException{
        RepositoryRank spring = new RepositoryRank();
        int point = spring.getPoint("pms131/techStudy");
        System.out.println("point = " + point);
    }
}
