package com.example.unittestdemoproject.example5;

import com.example.unittestdemoproject.example5.authtoken.AuthTokenCache;
import com.example.unittestdemoproject.example5.eventbus.EventBusPoster;
import com.example.unittestdemoproject.example5.eventbus.LoggedInEvent;
import com.example.unittestdemoproject.example5.networking.LoginHttpEndpointSync;
import com.example.unittestdemoproject.example5.networking.NetworkErrorException;

import org.junit.Before;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class LoginUseCaseSyncTest {
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String AUTH_TOKEN = "authToken";
    public static final String NON_INITIALIZED_AUTH_TOKEN = "noAuthToken";

    LoginHttpEndpointSyncTd mLoginHttpEndpointSyncTd;
    AuthTokenCacheTd mAuthTokenCacheTd;
    EventBusPosterTd mEventBusPosterTd;

    LoginUseCaseSync loginUseCaseSync;

    @Before
    public void setup() throws Exception {
        mLoginHttpEndpointSyncTd = new LoginHttpEndpointSyncTd();
        mAuthTokenCacheTd = new AuthTokenCacheTd();
        mEventBusPosterTd = new EventBusPosterTd();
        loginUseCaseSync = new LoginUseCaseSync(mLoginHttpEndpointSyncTd, mAuthTokenCacheTd, mEventBusPosterTd);
    }

    @Test
    public void loginSync_success_usernameAndPasswordPassedToEndpoint() throws Exception {
        loginUseCaseSync.loginSync(USERNAME, PASSWORD);
        assertThat(mLoginHttpEndpointSyncTd.mUsername).isEqualTo(USERNAME);
        assertThat(mLoginHttpEndpointSyncTd.mPassword).isEqualTo(PASSWORD);

    }

    @Test
    public void loginSync_success_authTokenCached() throws Exception {
        loginUseCaseSync.loginSync(USERNAME, PASSWORD);
        assertThat(mAuthTokenCacheTd.getAuthToken()).isEqualTo(AUTH_TOKEN);
    }

    @Test
    public void loginSync_generalError_authTokenNotCached() throws Exception {
        mLoginHttpEndpointSyncTd.mIsGeneralError = true;
        loginUseCaseSync.loginSync(USERNAME, PASSWORD);
        assertThat(mAuthTokenCacheTd.getAuthToken()).isEqualTo(NON_INITIALIZED_AUTH_TOKEN);
    }

    @Test
    public void loginSync_authError_authTokenNotCached() throws Exception {
        mLoginHttpEndpointSyncTd.mIsAuthError = true;
        loginUseCaseSync.loginSync(USERNAME, PASSWORD);
        assertThat(mAuthTokenCacheTd.getAuthToken()).isEqualTo(NON_INITIALIZED_AUTH_TOKEN);
    }

    @Test
    public void loginSync_serverError_authTokenNotCached() throws Exception {
        mLoginHttpEndpointSyncTd.mIsServerError = true;
        loginUseCaseSync.loginSync(USERNAME, PASSWORD);
        assertThat(mAuthTokenCacheTd.getAuthToken()).isEqualTo(NON_INITIALIZED_AUTH_TOKEN);
    }

    @Test
    public void loginSync_success_loggedInEventPosted() throws Exception {
        loginUseCaseSync.loginSync(USERNAME, PASSWORD);
        assertThat(mEventBusPosterTd.mEvent).isInstanceOf(LoggedInEvent.class);
    }

    @Test
    public void loginSync_generalError_noInteractionWithEventBusPoster() throws Exception {
        mLoginHttpEndpointSyncTd.mIsGeneralError = true;
        loginUseCaseSync.loginSync(USERNAME, PASSWORD);
        assertThat(mEventBusPosterTd.mInteractionsCount).isEqualTo(0);
    }

    @Test
    public void loginSync_authError_noInteractionWithEventBusPoster() throws Exception {
        mLoginHttpEndpointSyncTd.mIsAuthError = true;
        loginUseCaseSync.loginSync(USERNAME, PASSWORD);
        assertThat(mEventBusPosterTd.mInteractionsCount).isEqualTo(0);
    }

    @Test
    public void loginSync_serverError_noInteractionWithEventBusPoster() throws Exception {
        mLoginHttpEndpointSyncTd.mIsServerError = true;
        loginUseCaseSync.loginSync(USERNAME, PASSWORD);
        assertThat(mEventBusPosterTd.mInteractionsCount).isEqualTo(0);
    }

    @Test
    public void loginSync_success_successReturned() throws Exception {
        LoginUseCaseSync.UseCaseResult result = loginUseCaseSync.loginSync(USERNAME, PASSWORD);
        assertThat(result).isEqualTo(LoginUseCaseSync.UseCaseResult.SUCCESS);
    }

    @Test
    public void loginSync_serverError_failureReturned() throws Exception {
        mLoginHttpEndpointSyncTd.mIsServerError = true;
        LoginUseCaseSync.UseCaseResult result = loginUseCaseSync.loginSync(USERNAME, PASSWORD);
        assertThat(result).isEqualTo(LoginUseCaseSync.UseCaseResult.FAILURE);
    }

    @Test
    public void loginSync_authError_failureReturned() throws Exception {
        mLoginHttpEndpointSyncTd.mIsAuthError = true;
        LoginUseCaseSync.UseCaseResult result = loginUseCaseSync.loginSync(USERNAME, PASSWORD);
        assertThat(result).isEqualTo(LoginUseCaseSync.UseCaseResult.FAILURE);
    }

    @Test
    public void loginSync_generalError_failureReturned() throws Exception {
        mLoginHttpEndpointSyncTd.mIsGeneralError = true;
        LoginUseCaseSync.UseCaseResult result = loginUseCaseSync.loginSync(USERNAME, PASSWORD);
        assertThat(result).isEqualTo(LoginUseCaseSync.UseCaseResult.FAILURE);
    }

    @Test
    public void loginSync_networkError_networkErrorReturned() throws Exception {
        mLoginHttpEndpointSyncTd.mIsNetworkError = true;
        LoginUseCaseSync.UseCaseResult result = loginUseCaseSync.loginSync(USERNAME, PASSWORD);
        assertThat(result).isEqualTo(LoginUseCaseSync.UseCaseResult.NETWORK_ERROR);
    }

    // ---------------------------------------------------------------------------------------------
    // Helper classes

    private static class LoginHttpEndpointSyncTd implements LoginHttpEndpointSync {
        public String mUsername = "";
        private String mPassword = "";
        public boolean mIsGeneralError;
        public boolean mIsAuthError;
        public boolean mIsServerError;
        public boolean mIsNetworkError;

        @Override
        public EndpointResult loginSync(String username, String password) throws NetworkErrorException {
            mUsername = username;
            mPassword = password;
            if (mIsGeneralError) {
                return new EndpointResult(EndpointResultStatus.GENERAL_ERROR, "");
            } else if (mIsAuthError) {
                return new EndpointResult(EndpointResultStatus.AUTH_ERROR, "");
            }  else if (mIsServerError) {
                return new EndpointResult(EndpointResultStatus.SERVER_ERROR, "");
            } else if (mIsNetworkError) {
                throw new NetworkErrorException();
            } else {
                return new EndpointResult(EndpointResultStatus.SUCCESS, AUTH_TOKEN);
            }
        }
    }

    private static class AuthTokenCacheTd implements AuthTokenCache {

        String mAuthToken = NON_INITIALIZED_AUTH_TOKEN;

        @Override
        public void cacheAuthToken(String authToken) {
            mAuthToken = authToken;
        }

        @Override
        public String getAuthToken() {
            return mAuthToken;
        }
    }

    private static class EventBusPosterTd implements EventBusPoster {
        public Object mEvent;
        public int mInteractionsCount;

        @Override
        public void postEvent(Object event) {
            mInteractionsCount++;
            mEvent = event;
        }
    }
}
