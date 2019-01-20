import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/hello")
public class MessageRestService {

    @GET
    @Path("/")
    public Response printMessage() {
        public Response printMessage () {
            String result = "Hello Backend";
            return Response.status(200).entity(result).build();
        }
    }