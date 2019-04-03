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
import service.transaction.Transaction;
import service.transaction.TransactionService;

import java.io.File;
import java.net.URI;
import java.nio.file.FileSystem;
import java.util.ArrayList;
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
        System.out.println("problem at REST");
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
    @Produces(MediaType.APPLICATION_JSON)
    public Response postCategory(CategoryInput categoryInput){
        Response postCategoryResponse = null;

        try{
            categoryService.post(categoryInput);
            return Response.status(200).build();
        } catch (Exception e){
            e.printStackTrace();
        }
        return Response.status(400).entity("testFail").build();
    }

    @GET
    @Secured
    @Path("/categories")
    @Produces (MediaType.APPLICATION_JSON)
    public Response getCategories(){
        try{
            ArrayList<String>categoryResponse= categoryService.getAll();
            return Response.status(200).entity(categoryResponse).build();
        } catch (Exception e){
            e.printStackTrace();
        }
        return Response.status(400).entity("testFail").build();
    }

    @DELETE
    @Secured
    @Path("/deleteCategory")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteCategory(CategoryInput categoryInput){

        try{
            categoryService.delete(categoryInput);
            return Response.status(200).build();
        } catch (Exception e){
            e.printStackTrace();
        }
        return Response.status(400).entity("testFail").build();
    }

    @PUT
    @Secured
    @Path("/modfiyCategory")
    @Produces(MediaType.APPLICATION_JSON)
    public Response modifyCategory(CategoryInput categoryInput){
        Response postCategoryResponse = null;

        try{
            categoryService.modify(categoryInput);
            return Response.status(201).build();
        } catch (Exception e){
            e.printStackTrace();
        }
        return Response.status(400).entity("testFail").build();
    }


    @GET
    @Secured
    @Path("/export")
    @Produces("txt/csv")
    public Response exportAsCSV(){
        Response postCategoryResponse = null;
        File file = null;
        try{

            file = exportService.export();

            Response.ResponseBuilder resp= Response.ok((Object)file);
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
        return Response.status(400).entity("export failed").build();

    }

    @GET
    @Secured
    @Path("/transactions")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTransactions(){
        Response getTransactionsResponse = null;

        try{
            ArrayList<Transaction> ret = transactionService.getTransactions();
            return Response.status(200).entity(ret).build();
        } catch (Exception e){
            e.printStackTrace();
        }
        return Response.status(400).entity("testFail").build();

    }


}