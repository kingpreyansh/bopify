package com.example.b07_project;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.example.b07_project.layout.LoginActivity;
import com.example.b07_project.models.Auth;
import com.example.b07_project.models.Store;
import com.example.b07_project.presenters.LoginPresenter;


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(MockitoJUnitRunner.class)
public class AuthTest {

    @Mock
    LoginActivity mockView;

    @Mock
    Auth mockModel;

    @Mock
    Contract.View view;

    LoginPresenter presenter;

    @Before
    public void setUp(){
        presenter = new LoginPresenter(mockModel, mockView);
    }

    // Testing the interaction between the presenter and the model / view

    @Test
    public void testPresenter() throws Exception {
        String email = "";
        String password = "pwd";

        Mockito.doNothing().when(mockModel).login(email, password, mockView, presenter);

        presenter.recieveData(mockView, email, password);

        Mockito.verify(mockModel, Mockito.times(1)).login(email, password, mockView, presenter);
    }

    @Test
    public void presenterSuccess() throws Exception {

        String email = "storetegh@gmail.com"; // Store
        String password = "tegh123";
        String id = "1";
        Store store = new Store("Tegh", email, id);

        Mockito.doNothing().when(mockModel).login(email, password, mockView, presenter);
        presenter.recieveData(mockView, email, password);
        presenter.successDecider(true, mockView, store);
        Mockito.verify(mockView, Mockito.times(1)).handleGetUser(store);
    }

    @Test
    public void presenterFail() throws Exception {
        String email = "storetegh@gmail.com"; // Store
        String password = "tegh123";
        String id = "1";
        Store store = new Store("Tegh", email, id);

        Mockito.doNothing().when(mockModel).login(email, password, mockView, presenter);
        presenter.recieveData(mockView, email, password);
        presenter.successDecider(false, mockView, store);
        Mockito.verify(mockView, Mockito.times(1)).handleFailure();
    }

    @Test
    public void testHandleFinishLoginSuccess() {
        String email = "storetegh@gmail.com"; // Store
        String password = "tegh123";
        String id = "1";
        Store store = new Store("Tegh", email, id);

        Mockito.doNothing().when(mockModel).login(email, password, mockView, presenter);
        presenter.recieveData(mockView, email, password);
        presenter.successDecider(true, mockView, store);
        presenter.handleFinishLogin(true, "");
        Mockito.verify(mockView, Mockito.times(1)).handleGetUser(store);
    }

    @Test
    public void testHandleFinishLoginFail() {
        String toastMsg = "User does not exist";
        presenter.handleFinishLogin(false, toastMsg);
        Mockito.verify(mockView, Mockito.times(1)).handleFinishLoading(toastMsg);
    }

    @Test
    public void testHandleLogin() {
        presenter.handleLogin();
    }
}