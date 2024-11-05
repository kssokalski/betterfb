package com.example;


import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.*;


@Path("/api") //base path to handle the registration process
public class UserController {

    @Inject
    private UserService userService; //injecting the UserService class to use it in the controller

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)    
    public Response register(UserDto userDto){
        String response = userService.register(userDto.getLogin(), userDto.getPassword(), userDto.getEmail());
        return Response.ok(response).build();
    }

    public static class UserDto {

        private String login;
        private String password;
        private String email;

        public String getLogin() {
            return login;
        }
        public String getPassword() {
            return password;
        }
        public String getEmail() {
            return email;
        }

        public void setLogin(String login) {
            this.login = login;
        }
        public void setPassword(String password) {  
            this.password = password;
        }
        public void setEmail(String email) {
            this.email = email;
        }

    }
}
