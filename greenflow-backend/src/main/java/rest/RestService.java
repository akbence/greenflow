package rest;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import rest.Input.*;
import rest.Response.BudgetResponse;
import rest.Response.CategoryResponse;
import rest.Response.LoginResponse;
import rest.Response.StatisticPieResponse;
import service.authentication.AuthService;
import service.authentication.Secured;
import service.budget.BudgetService;
import service.export.ExportService;
import service.statistics.StatisticService;
import service.transaction.CategoryService;
import service.transaction.Transaction;
import service.transaction.TransactionService;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


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

    @Inject
    private StatisticService statisticService;

    @Inject
    private BudgetService budgetService;

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
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postTransaction( TransactionInput transactionInput){
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
            ArrayList<CategoryResponse>categoryResponse= categoryService.getAll();
            return Response.status(200).entity(categoryResponse).build();
        } catch (Exception e){
            e.printStackTrace();
        }
        return Response.status(400).entity("testFail").build();
    }

    @DELETE
    @Secured
    @Path("/categories/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteCategory(@PathParam("id") int id){

        try{
            categoryService.delete(id);
            return Response.status(200).build();
        } catch (Exception e){
            e.printStackTrace();
        }
        return Response.status(400).entity("testFail").build();
    }

    @PUT
    @Secured
    @Path("/categories/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response modifyCategory(@PathParam("id") int id, CategoryInput categoryInput){
        Response postCategoryResponse = null;

        try{
            categoryService.modify(id,categoryInput);
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
        try{
            ArrayList<Transaction> ret = transactionService.getTransactions();
            return Response.status(200).entity(ret).build();
        } catch (Exception e){
            e.printStackTrace();
        }
        return Response.status(400).entity("testFail").build();

    }

    @PUT
    @Secured
    @Path("/transactions/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response modifyTransaction(@PathParam("id") int id,TransactionInput transactionInput){
        try{
            transactionService.modifyTransaction(id,transactionInput);
            return Response.status(201).build();
        } catch (Exception e){
            e.printStackTrace();
        }
        return Response.status(400).entity("testFail").build();

    }
    @DELETE
    @Secured
    @Path("/transactions/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteTransaction(@PathParam("id") int id){
        try{
            transactionService.delete(id);
            return Response.status(204).build();
        } catch (Exception e){
            e.printStackTrace();
        }
        return Response.status(400).entity("testFail").build();

    }

    @GET
    @Secured
    @Path("/statistics/pie/income/{year}/{month}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPieIncomeStatistics(@PathParam("year") int year, @PathParam("month") int month){
        System.out.println(year + "/"+ month);
        try{
            StatisticPieResponse ret = statisticService.getIncomePieStatistics(year, month);
            return Response.status(200).entity(ret).build();
        } catch (Exception e){
            e.printStackTrace();
        }
        return Response.status(400).entity("testFail").build();
    }

    @GET
    @Secured
    @Path("/statistics/pie/expense/{year}/{month}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPieExpenseStatistics(@PathParam("year") int year, @PathParam("month") int month){
        System.out.println(year + "/"+ month);
        try{
            StatisticPieResponse ret = statisticService.getExpensePieStatistics(year, month);
            return Response.status(200).entity(ret).build();
        } catch (Exception e){
            e.printStackTrace();
        }
        return Response.status(400).entity("testFail").build();
    }


    @POST
    @Secured
    @Path("/budget")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postBudget(BudgetInput budgetInput){
        try{
            budgetService.addBudget(budgetInput);
            return Response.status(200).build();
        } catch (Exception e){
            e.printStackTrace();
        }
        return Response.status(400).entity("testFail").build();
    }

    @GET
    @Secured
    @Path("/budget")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getAllBudget(){
        try{
            List<BudgetResponse> response= budgetService.getAllBudget();
            return Response.status(200).entity(response).build();
        } catch (Exception e){
            e.printStackTrace();
        }
        return Response.status(400).entity("testFail").build();
    }

    @PUT
    @Secured
    @Path("/budget/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response modifyBudget(@PathParam("id") int id, int limit){
        try{
            budgetService.modifyBudget(id,limit);
            return Response.status(200).build();
        } catch (Exception e){
            e.printStackTrace();
        }
        return Response.status(400).entity("testFail").build();
    }

    @DELETE
    @Secured
    @Path("/budget/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response modifyBudget(@PathParam("id") int id){
        try{
            budgetService.deleteBudget(id);
            return Response.status(200).build();
        } catch (Exception e){
            e.printStackTrace();
        }
        return Response.status(400).entity("testFail").build();
    }


}