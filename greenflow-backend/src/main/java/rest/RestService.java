package rest;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.math.RandomUtils;
import rest.Input.CategoryInput;
import rest.Input.ExportInput;
import rest.Input.TransactionInput;
import rest.Input.UserAuthInput;
import rest.Response.LoginResponse;
import service.authentication.LoggedInService;
import service.authentication.AuthService;
import service.authentication.Secured;
import service.export.ExportService;
import service.transaction.CategoryService;
import service.transaction.TransactionService;

import java.io.File;
import java.net.URI;
import java.nio.file.FileSystem;
import java.util.Random;
import java.util.RandomAccess;


@Path("")
@ApplicationScoped
public class RestService {

    @Inject
    private AuthService authService;

    @Inject
    private TransactionService transactionService;

    @Inject
    private CategoryService categoryService;

    @Inject
    private ExportService exportService;

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
    @Secured
    @Path("/postTransaction")
    public Response postTransaction(TransactionInput transactionInput){
        Response postTransactionResponse = null;

        ///TEST
        System.out.println(transactionInput.getCategory());
        System.out.println(transactionInput.getUsername());
        System.out.println(transactionInput.getCurrency());


        try{
            transactionService.post(transactionInput);
            return Response.status(200).entity("postTransaction success").build();
        } catch (Exception e){
            e.printStackTrace();
        }
        return Response.status(400).entity("testFail").build();
    }

    @POST
    @Secured
    @Path("/postCategory")
    public Response postCategory(CategoryInput categoryInput){
        Response postCategoryResponse = null;

        try{
            categoryService.post(categoryInput);
            return Response.status(200).entity("postCategory success").build();
        } catch (Exception e){
            e.printStackTrace();
        }
        return Response.status(400).entity("testFail").build();
    }

    @GET
    //@Secured
    @Path("/export")
    @Produces("txt/csv")
    public Response exportAsCSV(){
        Response postCategoryResponse = null;
        File file = null;
        try{
           // exportService.export();
            Random randomnumber = new Random();
            file = new File(new URI("file:/tmp/"+  "865599642"/*randomnumber.nextInt()*/+".csv"));

            file.createNewFile();
            System.out.println(file.getAbsolutePath());

            Response.ResponseBuilder resp= Response.ok((Object)file);
            //return Response.status(200)
            resp.header("Content-Disposition","attachment; filename=\"test_file.csv\"");
            return  resp.build();
        } catch (Exception e){
            e.printStackTrace();
        }
        finally {
            if(file!=null){
                //file.delete();
            }
        }
        return Response.status(400).entity("testFail").build();

    }


}