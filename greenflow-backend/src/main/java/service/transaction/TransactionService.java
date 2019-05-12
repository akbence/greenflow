package service.transaction;

import converters.TransactionConverter;
import dao.UserDao;
import dao.transactions.CategoryDao;
import dao.transactions.TransactionDao;
import enums.Currency;
import enums.PaymentType;
import net.sourceforge.tess4j.Tesseract;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import rest.Input.TransactionInput;
import service.authentication.LoggedInService;
import service.budget.BudgetService;
import service.events.EventService;

import javax.enterprise.inject.Model;
import javax.inject.Inject;
import javax.json.*;
import javax.net.ssl.HttpsURLConnection;
import javax.ws.rs.core.MediaType;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Model
public class TransactionService {

    @Inject
    private LoggedInService loggedInService;

    @Inject
    private TransactionDao transactionDao;

    @Inject UserDao userDao;

    @Inject
    private CategoryDao categoryDao;

    @Inject
    private BudgetService budgetService;


    @Inject
    private TransactionConverter transactionConverter;

    public void post(TransactionInput transactionInput) throws Exception {
        Transaction transaction= transactionConverter.restInputToService(transactionInput,loggedInService.getCurrentUserName());
        transactionDao.post(transaction);
        //Whenever a transaction expense posted, the BudgetService checks, if it overextends any limit.
        budgetService.checkLimitExtension(transaction);
    }



    public ArrayList<Transaction> getTransactions() throws Exception {
        return transactionDao.getEntireTransactionHistory(loggedInService.getCurrentUserName());
    }

    public void delete(int id) throws Exception {
        String username=loggedInService.getCurrentUserName();
        transactionDao.delete(id,username);
    }

    public void modifyTransaction(int id, TransactionInput transactionInput) throws Exception {
        String username=loggedInService.getCurrentUserName();
        Transaction transaction = transactionConverter.restInputToService(transactionInput,loggedInService.getCurrentUserName());
        transaction.setId(id);

        int category_id= categoryDao.getID(transaction.getCategory(),userDao.getId(username));
        transactionDao.modify(transaction,category_id);
    }

    public Transaction predictByReceipt(String picture) throws Exception {
        ArrayList<String> lines=getOCRLines(picture);
        lines.forEach(s -> System.out.println(s));
        Transaction transaction=calcPrediction(lines);
        if(transaction!=null){
            return transaction;
        }
        return null;
    }

    private Transaction calcPrediction(ArrayList<String> lines) {
        //Only for SPAR receipts
        boolean isSparReceipt=false;
        for (String line : lines) {
            if(line.toLowerCase().contains("spar")){
                isSparReceipt = true;
            }
        }
        if(isSparReceipt){
            int amount =0;
            String paymentType=null;
            for (String line : lines) {
                String tabSeperated[] =line.split("\\\\t");
                if(tabSeperated.length==2){
                    if(hammingDist("forint",tabSeperated[0].toLowerCase().trim())<3){
                        String temp=tabSeperated[1].toLowerCase().replace("ft","").replaceAll("\\s","");
                        amount+=Integer.parseInt(temp);
                        paymentType="CASH";
                    }
                    if(hammingDist("visszajaro",tabSeperated[0].toLowerCase().trim())<3){
                        String temp=tabSeperated[1].toLowerCase().replace("ft","").replaceAll("\\s","");
                        amount-=Integer.parseInt(temp);
                        break;
                    }
                    if(hammingDist("bankkartya",tabSeperated[0].toLowerCase().trim())<3){
                        String temp=tabSeperated[1].toLowerCase().replace("ft","").replaceAll("\\s","");
                        amount+=Integer.parseInt(temp);
                        paymentType="CARD";
                        break;
                    }
                }
            }
            if(amount!=0 && (paymentType != null)){
                Transaction transaction = new Transaction();
                transaction.setAmmount(amount);
                transaction.setPaymentType(PaymentType.valueOf(paymentType));
                transaction.setCurrency(Currency.HUF);
                transaction.setExpense(true);
                return transaction;
            }
        }

        return null;
    }

    int hammingDist(String main , String compare)
    {
        int i = 0, count = 0;
        while (i < main.length() && i<compare.length())
        {
            if (main.charAt(i) != compare.charAt(i))
                count++;
            i++;
        }
        if(main.length()>compare.length()){
            count+= main.length()-compare.length();
        }
        return count;
    }

    private ArrayList<String> getOCRLines(String picture) throws IOException {
        HttpClient client = new DefaultHttpClient();
        String url ="https://api.ocr.space/parse/image";
        HttpPost post = new HttpPost(url);
        UrlEncodedFormEntity entity = setOCRRequest(picture, post);
        post.setEntity(entity);
        HttpResponse response = client.execute(post);
        String json = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8.toString());
        JsonReader reader = Json.createReader(new StringReader(json));
        JsonString parsedTextResults = reader.readObject()
                .getJsonArray("ParsedResults")
                .getJsonObject(0)
                .getJsonString("ParsedText");
        return new ArrayList<>(Arrays.asList(parsedTextResults.toString().split("\\\\t\\\\r\\\\n")));
    }

    private UrlEncodedFormEntity setOCRRequest(String picture, HttpPost post) throws UnsupportedEncodingException {
        ResourceBundle rb = ResourceBundle.getBundle("ocrcredentials");
        post.setHeader("apikey", rb.getString("key"));
        post.setHeader("content-type", MediaType.APPLICATION_FORM_URLENCODED);
        List<NameValuePair> form = new ArrayList<>();
        form.add(new BasicNameValuePair("language", "hun"));
        form.add(new BasicNameValuePair("base64Image", "data:image/jpg;base64,"+picture));
        form.add(new BasicNameValuePair("filetype","JPG"));
        form.add(new BasicNameValuePair("isTable","true"));
        form.add(new BasicNameValuePair("scale","true"));
        return new UrlEncodedFormEntity(form);
    }

    /*OLD with Tesseract
    public void oldpredictByReceipt(String picture) throws Exception {

        try {
            File imageFile= new File("C:\\\\Temp\\test.jpg");
            Tesseract instance = new Tesseract();
            System.out.println("abs path: "+ imageFile.getAbsolutePath());
            instance.setLanguage("hun");
            File tessDataFolder = new File("C:\\\\Temp\\tessdata");
            System.out.println(tessDataFolder.getAbsolutePath());
            instance.setDatapath(tessDataFolder.getAbsolutePath());



            res=instance.doOCR(imageFile);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }*/
}
