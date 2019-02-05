package rest;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import rest.Input.UserAuthInput;
import rest.Response.LoginResponse;
import service.authentication.LoggedInService;
import service.authentication.AuthService;
import service.RestAction;
import dao.GreenflowDao;

@Path("")
@ApplicationScoped
public class RestService {

    @Inject
    private GreenflowDao greenflowDao;

    @Inject
    private RestAction restAction;

    @Inject
    private LoggedInService loggedInService;

    @Inject
    private AuthService authService;

    @GET
    @Path("/hello")
    public Response printMessage() {
        String result = restAction.quickTest();
        return Response.status(200).entity(result).build();
    }

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response register(UserAuthInput userAuthInput) {
        try {
            authService.registerUser(userAuthInput);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.status(200).entity("").build();
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(UserAuthInput userAuthInput){
        LoginResponse loginResponse=null;
        try {
            loginResponse=authService.loginUser(userAuthInput);
            return Response.status(200).entity(loginResponse).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.status(400).entity("").build();
    }

    @POST
    @Path("/add")
    public Response postUser() {
        System.out.println("hello restService");
        greenflowDao.addUser();
        return Response.status(200).entity("user added").build();
    }
}