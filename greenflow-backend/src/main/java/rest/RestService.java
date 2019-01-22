package rest;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import dao.GreenflowDao;

@Path("")
public class RestService {

    @Inject
    private GreenflowDao greenflowDao;

    @GET
    @Path("/hello")
    public Response printMessage() {
        String result = "Hello Backend";
        return Response.status(200).entity(result).build();
    }

    @POST
    @Path("/add")
    public Response postUser() {
        System.out.println("hello restService");
        greenflowDao.addUser();
        return Response.status(200).entity("user added").build();
    }
}