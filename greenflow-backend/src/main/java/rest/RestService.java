package rest;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import rest.Input.CategoryInput;
import rest.Input.ExportInput;
import rest.Input.TransactionInput;
import rest.Input.UserAuthInput;
import rest.Response.LoginResponse;
import service.authentication.LoggedInService;
import service.authentication.AuthService;
import service.export.ExportService;
import service.transaction.CategoryService;
import service.transaction.TransactionService;


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

    @POST
    @Path("/export")
    public Response exportAsCSV(ExportInput exportInput){
        Response postCategoryResponse = null;

        try{
            exportService.export(exportInput);
            return Response.status(200).entity("postCategory success").build();
        } catch (Exception e){
            e.printStackTrace();
        }
        return Response.status(400).entity("testFail").build();

    }


}