package com.example.unittestdemoproject.example7;

import com.example.unittestdemoproject.example7.authtoken.AuthTokenCache;
import com.example.unittestdemoproject.example7.eventbus.EventBusPoster;
import com.example.unittestdemoproject.example7.eventbus.LoggedInEvent;
import com.example.unittestdemoproject.example7.networking.LoginHttpEndpointSync;
import com.example.unittestdemoproject.example7.networking.NetworkErrorException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class LoginUseCaseSyncMockitoTest {

    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String AUTH_TOKEN = "authToken";

    LoginHttpEndpointSync mLoginHttpEndpointSyncMock;
    AuthTokenCache mAuthTokenCacheMock;
    EventBusPoster mEventBusPosterMock;

    LoginUseCaseSyncMockito loginUseCaseSyncMockito;

    @Before
    public void setup() throws Exception {
        mLoginHttpEndpointSyncMock = mock(LoginHttpEndpointSync.class);
        mAuthTokenCacheMock = mock(AuthTokenCache.class);
        mEventBusPosterMock = mock(EventBusPoster.class);
        loginUseCaseSyncMockito = new LoginUseCaseSyncMockito(mLoginHttpEndpointSyncMock, mAuthTokenCacheMock, mEventBusPosterMock);
        success();
    }

    @Test
    public void loginSync_success_usernameAndPasswordPassedToEndpoint() throws Exception {
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        loginUseCaseSyncMockito.loginSync(USERNAME, PASSWORD);
        verify(mLoginHttpEndpointSyncMock, times(1)).loginSync(ac.capture(), ac.capture());
        List<String> captures = ac.getAllValues();
        assertThat(captures.get(0)).isEqualTo(USERNAME);
        assertThat(captures.get(1)).isEqualTo(PASSWORD);
    }

    @Test
    public void loginSync_success_authTokenCached() throws Exception {
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        loginUseCaseSyncMockito.loginSync(USERNAME, PASSWORD);
        verify(mAuthTokenCacheMock).cacheAuthToken(ac.capture());

        assertThat(ac.getValue()).isEqualTo(AUTH_TOKEN);
    }

    @Test
    public void loginSync_generalError_authTokenNotCached() throws Exception {
        generalError();
        loginUseCaseSyncMockito.loginSync(USERNAME, PASSWORD);
        verifyNoMoreInteractions(mAuthTokenCacheMock);
    }

    @Test
    public void loginSync_authError_authTokenNotCached() throws Exception {
        authError();
        loginUseCaseSyncMockito.loginSync(USERNAME, PASSWORD);
        verifyNoMoreInteractions(mAuthTokenCacheMock);
    }

    @Test
    public void loginSync_serverError_authTokenNotCached() throws Exception {
        serverError();
        loginUseCaseSyncMockito.loginSync(USERNAME, PASSWORD);
        verifyNoMoreInteractions(mAuthTokenCacheMock);
    }

    @Test
    public void loginSync_success_loggedInEventPosted() throws Exception {
        ArgumentCaptor<Object> ac = ArgumentCaptor.forClass(Object.class);
        loginUseCaseSyncMockito.loginSync(USERNAME, PASSWORD);
        verify(mEventBusPosterMock).postEvent(ac.capture());
        assertThat(ac.getValue()).isInstanceOf(LoggedInEvent.class);
    }

    @Test
    public void loginSync_generalError_noInteractionWithEventBusPoster() throws Exception {
        generalError();
        loginUseCaseSyncMockito.loginSync(USERNAME, PASSWORD);
        verifyNoMoreInteractions(mEventBusPosterMock);
    }

    @Test
    public void loginSync_authError_noInteractionWithEventBusPoster() throws Exception {
        authError();
        loginUseCaseSyncMockito.loginSync(USERNAME, PASSWORD);
        verifyNoMoreInteractions(mEventBusPosterMock);
    }

    @Test
    public void loginSync_serverError_noInteractionWithEventBusPoster() throws Exception {
        serverError();
        loginUseCaseSyncMockito.loginSync(USERNAME, PASSWORD);
        verifyNoMoreInteractions(mEventBusPosterMock);
    }

    @Test
    public void loginSync_success_successReturned() throws Exception {
        LoginUseCaseSyncMockito.UseCaseResult result = loginUseCaseSyncMockito.loginSync(USERNAME, PASSWORD);
        assertThat(result).isEqualTo(LoginUseCaseSyncMockito.UseCaseResult.SUCCESS);
    }

    @Test
    public void loginSync_serverError_failureReturned() throws Exception {
        serverError();
        LoginUseCaseSyncMockito.UseCaseResult result = loginUseCaseSyncMockito.loginSync(USERNAME, PASSWORD);
        assertThat(result).isEqualTo(LoginUseCaseSyncMockito.UseCaseResult.FAILURE);
    }

    @Test
    public void loginSync_authError_failureReturned() throws Exception {
        authError();
        LoginUseCaseSyncMockito.UseCaseResult result = loginUseCaseSyncMockito.loginSync(USERNAME, PASSWORD);
        assertThat(result).isEqualTo(LoginUseCaseSyncMockito.UseCaseResult.FAILURE);
    }

    @Test
    public void loginSync_generalError_failureReturned() throws Exception {
        generalError();
        LoginUseCaseSyncMockito.UseCaseResult result = loginUseCaseSyncMockito.loginSync(USERNAME, PASSWORD);
        assertThat(result).isEqualTo(LoginUseCaseSyncMockito.UseCaseResult.FAILURE);
    }

    @Test
    public void loginSync_networkError_networkErrorReturned() throws Exception {
        networkError();
        LoginUseCaseSyncMockito.UseCaseResult result = loginUseCaseSyncMockito.loginSync(USERNAME, PASSWORD);
        assertThat(result).isEqualTo(LoginUseCaseSyncMockito.UseCaseResult.NETWORK_ERROR);
    }

    private void networkError() throws Exception {
        doThrow(new NetworkErrorException())
                .when(mLoginHttpEndpointSyncMock).loginSync(any(String.class), any(String.class));
    }

    private void success() throws NetworkErrorException {
        when(mLoginHttpEndpointSyncMock.loginSync(any(String.class), any(String.class)))
                .thenReturn(new LoginHttpEndpointSync.EndpointResult(LoginHttpEndpointSync.EndpointResultStatus.SUCCESS, AUTH_TOKEN));
    }

    private void generalError() throws Exception {
        when(mLoginHttpEndpointSyncMock.loginSync(any(String.class), any(String.class)))
                .thenReturn(new LoginHttpEndpointSync.EndpointResult(LoginHttpEndpointSync.EndpointResultStatus.GENERAL_ERROR, ""));
    }

    private void authError() throws Exception {
        when(mLoginHttpEndpointSyncMock.loginSync(any(String.class), any(String.class)))
                .thenReturn(new LoginHttpEndpointSync.EndpointResult(LoginHttpEndpointSync.EndpointResultStatus.AUTH_ERROR, ""));
    }

    private void serverError() throws Exception {
        when(mLoginHttpEndpointSyncMock.loginSync(any(String.class), any(String.class)))
                .thenReturn(new LoginHttpEndpointSync.EndpointResult(LoginHttpEndpointSync.EndpointResultStatus.SERVER_ERROR, ""));
    }
}
