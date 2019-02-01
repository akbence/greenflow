package rest;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import inputs.UserInput;
import service.authentication.Login;
import service.RestAction;
import dao.GreenflowDao;
import service.authentication.RegisterService;

@Path("")
public class RestService {

    @Inject
    private GreenflowDao greenflowDao;

    @Inject
    private RestAction restAction;

    @Inject
    private Login login;

    @Inject
    private RegisterService registerService;

    @GET
    @Path("/hello")
    public Response printMessage() {
        String result = restAction.quickTest();
        return Response.status(200).entity(result).build();
    }

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response register(UserInput userInput) {
        try {
            registerService.registerUser(userInput);
        } catch (Exception e) {
            e.printStackTrace();
        }
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