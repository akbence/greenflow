package service.export;

import dao.transactions.TransactionDao;
import org.apache.commons.lang.StringUtils;
import service.authentication.LoggedInService;
import service.transaction.Transaction;

import javax.enterprise.inject.Model;
import javax.inject.Inject;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Random;

@Model
public class ExportService {

    private static final String WINDOWS = "windows";
    private static final String UNIX_LINUX = "linux or unix";

    @Inject
    TransactionDao transactionDao;

    @Inject
    LoggedInService loggedInService;


    public File export(String from, String to) throws Exception {
        File file;
        try {
            ArrayList<Transaction> transactions;
            if(from==null || to==null){
            transactions= transactionDao.getEntireTransactionHistory(loggedInService.getCurrentUserName());
            }else{
                DateTimeFormatter DATEFORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate fromDate = LocalDate.parse(from, DATEFORMATTER);
                LocalDate toDate = LocalDate.parse(to,DATEFORMATTER);
                transactions = transactionDao.getGivenPeriodTransactionHistory(loggedInService.getCurrentUserName(),fromDate,toDate);
            }
            file = new File(getTemporaryFileURI());
            file.createNewFile();
            return createCSV(file, transactions);

        } catch (Exception e) {
            throw e;
        }

    }

    private File createCSV(File file, ArrayList<Transaction> transactions) throws IOException {


        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))){
            // Header
            writer.write("\"Name\", \"Ammount\", \"Category\", \"Currency\", \"Date\", \"PaymentType\" ");
            writer.newLine();

            transactions.forEach(tr -> {

                //Data
                try {
                    writer.write("\"" + tr.getName() + "\"");
                    writer.write(", \"" + tr.getAmmount() + "\"");
                    writer.write(", \"" + tr.getCategory() + "\"");
                    writer.write(", \"" + tr.getCurrency() + "\"");
                    writer.write(", \"" + tr.getDate() + "\"");
                    writer.write(", \"" + tr.getPaymentType() + "\"");
                    writer.newLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        });

        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    private URI getTemporaryFileURI() throws Exception {
        Random randomnumber = new Random();
        switch (getOSType()) {
        case UNIX_LINUX:
            return new URI("file:/tmp/" + Math.abs(randomnumber.nextInt()) + ".csv");
        case WINDOWS:
            return new URI("file:///C:/Temp/"+ Math.abs(randomnumber.nextInt()) + ".csv");
        default:
            return new URI("file:/tmp/" + randomnumber.nextInt() + ".csv");
        }
    }

    private String getOSType() {
        String OS = System.getProperty("os.name").toLowerCase();
        if (OS.indexOf("win") >= 0) {
            return WINDOWS;
        }
        if ((OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0)) {
            return UNIX_LINUX;
        }
        return "NonDefined";
    }

}
