package rest;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import action.LoggedIn;
import action.Login;
import action.RestAction;
import action.User;
import dao.GreenflowDao;

@Path("")
public class RestService {

    @Inject
    private GreenflowDao greenflowDao;

    @Inject
    private RestAction restAction;

    @Inject
    private Login login;

    @GET
    @Path("/hello")
    public Response printMessage() {
        String result = restAction.quickTest();
        return Response.status(200).entity(result).build();
    }

    @GET
    @Path("/test")
    public Response test() {
        login.login();
        return Response.status(200).entity("").build();
    }

    @POST
    @Path("/add")
    public Response postUser() {
        System.out.println("hello restService");
        greenflowDao.addUser();
        return Response.status(200).entity("user added").build();
    }
}