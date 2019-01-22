package rest;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("")
public class RestService {

    @GET
    @Path("/hello")
    public Response printMessage() {
            String result = "Hello Backend";
            return Response.status(200).entity(result).build();
        }
    }