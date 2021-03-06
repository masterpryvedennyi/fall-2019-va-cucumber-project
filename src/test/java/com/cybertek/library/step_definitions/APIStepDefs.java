package com.cybertek.library.step_definitions;

import com.cybertek.library.pages.LoginPage;
import com.cybertek.library.utilities.AuthenticationUtility;
import com.cybertek.library.utilities.LibraryUserUtility;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class APIStepDefs {
    Map<String, Object> student;
    @Given("new student is added using the add_user endpoint")
    public void new_student_is_added_using_the_add_user_endpoint() {
        // set base url --> let's in Hooks class
        // get a token
        String librarianToken = AuthenticationUtility.getLibrarianToken();
        System.out.println("librarianToken = " + librarianToken);
        // create new user information
        student = LibraryUserUtility.createUser(3);
        System.out.println("student = " + student);
        // create using using the add_user
        Response response = given().
                header("x-library-token", librarianToken).
                formParams(student).
                log().all().
        when().
                post("add_user").
                prettyPeek();
        response.then().statusCode(200);
    }

    @When("I login as the new user created using add_user endpoint")
    public void i_login_as_the_new_user_created_using_add_user_endpoint() {
        String email = student.get("email").toString();
        String password = student.get("password").toString();
        LoginPage loginPage = new LoginPage();
        loginPage.login(email, password);
    }
}
