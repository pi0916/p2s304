/*
 * LibraryModel.java
 * Author:
 * Created on:
 */



import javax.swing.*;
import javax.xml.transform.Result;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class LibraryModel {

    // For use in creating dialogs and making them modal
    private JFrame dialogParent;
    //Initialization
    private final String url = "jdbc:postgresql://localhost:5432/300387709_jdbc";
    private Connection conManager;
    private String generalErrorMessage = "\nThere are no matching record according to the information you supplied." +
                                          "\nPlease check your input and try again";
    private String p16Spaces = "                ";
    private String divider = "\n==========================================================\n";


    public LibraryModel(JFrame parent, String userid, String password) {
	dialogParent = parent;
	try {
	    //set up the connections,initialize the statement object
        conManager = DriverManager.getConnection(url,userid,password);

    }catch (java.sql.SQLException e) {
	    e.printStackTrace();
	    }
    }

    public String bookLookup(int isbn) {

        StringBuilder qResult =  new StringBuilder();
        String banner = "Please check the  query result of book look up is below:\n";
        qResult.append(banner);
        try {
            Statement stmt = conManager.createStatement();
            ResultSet rs=null;
            rs = stmt.executeQuery("SELECT * FROM book WHERE isbn= "+isbn+";");
            //Get the info from books first
            if (rs.next()&& !rs.getString(1).equals("0")) {
                qResult.append("\nISBN: ").append(rs.getString(1));
                qResult.append("\nTitle: ") .append(rs.getString(2));
                qResult.append("\nEdition NUmber: ").append(rs.getString(3));
                qResult.append("\nNumbers of copies: ").append(rs.getString(4));
                qResult.append("\nNumber of copies left: ").append(rs.getString(5));
                qResult.append("\nAuthors: ");
                //Then retrieve the Author's information
                rs = stmt.executeQuery("SELECT name,surname,authorseqno" +
                                         " FROM book_author NATURAL JOIN author" +
                                         " WHERE isbn =" +isbn+
                                         " ORDER BY authorseqno ASC;");
                while (rs.next()){
                    qResult.append(rs.getString(1));
                    qResult.append(rs.getString(2));
                    qResult.append("Author Seq No.: ").append(rs.getString(3)).append("\n");;
                    qResult.append(p16Spaces);
                }
            }
            else{
                qResult.append(generalErrorMessage);
            }
        }catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getSQLState());
            System.out.println(e.getErrorCode());
        }
	return qResult.toString();
    }

    public String showCatalogue() {

        StringBuilder qResult =  new StringBuilder();
        String banner = "Please check the information of all the books (Catalogue) below:\n";
        qResult.append(banner);
        try {
            Statement stmt = conManager.createStatement();
            ResultSet rs =null;
            rs = stmt.executeQuery("SELECT * FROM book ;");
            System.out.println(rs.next());
            if(rs.next()) {
                do{
                    //skip the default tuple
                    if(rs.getString(1).equals("0")){continue;}
                    qResult.append(divider);
                    qResult.append("\nISBN: ").append(rs.getString(1));
                    qResult.append("\nTitle: ") .append(rs.getString(2));
                    qResult.append("\nEdition Number: ").append(rs.getString(3));
                    qResult.append("\nNumbers of copies: ").append(rs.getString(4));
                    qResult.append("\nNumber of copies left: ").append(rs.getString(5));
                } while(rs.next());
            }
            else{
                qResult.append(generalErrorMessage);
            }
        }catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getSQLState());
            System.out.println(e.getErrorCode());
        }
        return qResult.toString();
    }

    public String showLoanedBooks() {
        StringBuilder qResult =  new StringBuilder();
        String banner = "Please check the information of loaned books below:\n";
        qResult.append(banner);
        try {
            Statement stmt = conManager.createStatement();
            ResultSet rs=null;
            rs = stmt.executeQuery("SELECT isbn,title,customerid,f_name,l_name,duedate"+
                                        " FROM (book natural join cust_book) natural join customer;");
            if(rs.next()) {
                do{
                    /*no need to skip default tuple in this query as neither default book or customer should not
                    been inserted into this table*/
                    qResult.append("\nISBN: ").append(rs.getString(1));
                    qResult.append("\nTitle: ").append(rs.getString(2));
                    qResult.append("\nCustomer  ID : ").append(rs.getString(3));
                    qResult.append("\nCustomer Name: ").append(rs.getString(4))
                            .append("   ").append(rs.getString(5));
                    qResult.append("\nDue Date: ").append(rs.getString(6));
                } while(rs.next());
            }
            else{
                qResult.append(generalErrorMessage);
            }
        }catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getSQLState());
            System.out.println(e.getErrorCode());
        }
        return qResult.toString();
    }

    public String showAuthor(int authorID) {
        StringBuilder qResult =  new StringBuilder();
        String banner = "Please check the Author's information of your query below:\n";
        qResult.append(banner);
        try {
            Statement stmt = conManager.createStatement();
            ResultSet rs=null;
            rs = stmt.executeQuery("SELECT * FROM author WHERE authorid ="+authorID+";");

            if(rs.next()&& !rs.getString(1).equals("0")) {
                qResult.append("\nAuthor  ID: ").append(rs.getInt(1));
                qResult.append("\nFirst Name: ").append(rs.getString(2));
                qResult.append("\nLast  Name: ").append(rs.getString(3));
            }
            else{
                qResult.append(generalErrorMessage);
            }

        }catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getSQLState());
            System.out.println(e.getErrorCode());
        }
        return qResult.toString();
    }

    public String showAllAuthors() {
        StringBuilder qResult =  new StringBuilder();
        String banner = "Please check all the Author's information below:\n";
        qResult.append(banner);
        try {
            Statement stmt = conManager.createStatement();
            ResultSet rs=null;
            rs = stmt.executeQuery("SELECT * FROM author ;");
            if(rs.next()) {
                do{
                    //skip the default tuple
                    if(rs.getString(1).equals("0")){continue;}
                    qResult.append(divider);
                    qResult.append("\nAuthor  ID: ").append(rs.getInt(1));
                    qResult.append("\nFirst Name: ").append(rs.getString(2));
                    qResult.append("\nLast  Name: ").append(rs.getString(3));
                } while(rs.next());
            }
            else{
                qResult.append(generalErrorMessage);
            }
        }catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getSQLState());
            System.out.println(e.getErrorCode());
        }
        return qResult.toString();
    }

    public String showCustomer(int customerID) {
        StringBuilder qResult =  new StringBuilder();
        String banner = "Please check the Customers' information of your query below:\n";
        qResult.append(banner);
        try {
            Statement stmt = conManager.createStatement();
            ResultSet rs=null;
            rs = stmt.executeQuery("SELECT * FROM customer WHERE customerid ="+customerID+";");
            if(rs.next() && !rs.getString(1).equals("0")) {
                qResult.append("\nCustomer ID:  ").append(rs.getInt(1));
                qResult.append("\nFirst  Name:  ") .append(rs.getString(2));
                qResult.append("\nLast   Name:  ").append(rs.getString(3));
                qResult.append("\ncity:  ").append(rs.getString(4));
            }
            else{
                qResult.append(generalErrorMessage);
            }

        }catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getSQLState());
            System.out.println(e.getErrorCode());
        }
        return qResult.toString();
    }

    public String showAllCustomers() {
        StringBuilder qResult =  new StringBuilder();
        String banner = "Please check all the Customers' information below:\n";
        qResult.append(banner);
        try {
            Statement stmt = conManager.createStatement();
            ResultSet rs=null;
            rs = stmt.executeQuery("SELECT * FROM customer ;");
            if(rs.next()){
                do{
                    //skip the default tuple
                    if(rs.getString(1).equals("0")){continue;}
                    qResult.append(divider);
                    qResult.append("\nCustomer ID:  ").append(rs.getInt(1));
                    qResult.append("\nFirst  Name:  ") .append(rs.getString(2));
                    qResult.append("\nLast   Name:  ").append(rs.getString(3));
                    qResult.append("\ncity:  ").append(rs.getString(4));
                } while(rs.next());
            }
            else {
                qResult.append(generalErrorMessage);
            }
        }catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getSQLState());
            System.out.println(e.getErrorCode());
        }
        return qResult.toString();
    }

    public String borrowBook(int isbn, int customerID, int day, int month, int year) {
        //to fix the error in given UI
        int correctMonth = month+1;
        Statement preCheckBook = null;
        Statement preCheckCustomer=null;

        ResultSet pChkBook =null;
        ResultSet pChkCustomer=null;
        Savepoint checkpoint=null;
        Calendar today = Calendar.getInstance();
        Calendar choseDate = Calendar.getInstance();
        choseDate.set(year,correctMonth,day);
        StringBuilder result = new StringBuilder();
        String banner = "Performing the borrow of Book-ISBN "+isbn+" for Customer ";

        String bookQuery = "SELECT * " +
                            "FROM book " +
                              "WHERE isbn="+isbn+";";

        String customerQuery = "SELECT * " +
                                 "FROM customer " +
                                     "WHERE customerid ="+customerID+";";
        try{
            //shutdown auto commit for transaction
            conManager.setAutoCommit(false);
            //set transaction level to avoid Dirty Reads, Non-Repeatable Reads,and Phantom Reads
            conManager.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            //generate roll back checkpoint for roll back
            checkpoint = conManager.setSavepoint();
            preCheckBook = conManager.createStatement();
            preCheckCustomer = conManager.createStatement();

            String bookErrorMsg = "\nNo book have ISBN equals to"+isbn+", please check your input.";
            String customerErrorMsg="\nNo customer have customerID equals"+customerID+", please check your input.";

            pChkBook = preCheckBook.executeQuery(bookQuery);
            pChkCustomer= preCheckCustomer.executeQuery(customerQuery);

            //check if given ISBN is valid
            if (!pChkBook.next()){
                 result.append(bookErrorMsg);
                 return  result.toString();
             }
             else{
                //check if it is the default book, default book shouldn't able to be borrowed
                 if(pChkBook.getString(1).equals("0")){
                     result.append(bookErrorMsg);
                     return result.toString();
                 }
             }
             //check if given customer id is valid
            if (!pChkCustomer.next()){
                result.append(customerErrorMsg);
                return  result.toString();
            }
            else{
                //check if it is default customer,default customer should't able to borrow
                if(pChkCustomer.getString(1).equals("0")){
                    result.append(customerErrorMsg);
                    return result.toString();
                }
            }
            //due date should ahead today to be valid
            if(choseDate.before(today)){
                result.append("Due date must ahead today,please verify your choice");
                return result.toString();
            }
            //rectrive all the useful information of the seclected book and customer
            String ISBN =pChkBook.getString("isbn");
            int bookLeft = pChkBook.getInt("numleft");
            String title = pChkBook.getString("title");
            String cfname = pChkCustomer.getString("f_name");
            String lname = pChkCustomer.getString("l_name");
            int CID = pChkCustomer.getInt("customerid");
            //check the left copies before updating
            if(bookLeft < 1){
                result.append("There is no left copy for ").append(title).append(" ISBN").append(isbn);
                return result.toString();
            }
            //check if the customer has borrowed this book before(in hand),one customer is not allow to borrow duplicate copies of same book at a time
            Statement ifborrowed = conManager.createStatement();
            ResultSet ifBefore = ifborrowed.executeQuery("SELECT * FROM cust_book WHERE customerid ="+CID+" AND isbn= "+ISBN+";");
            if(ifBefore.next()){
                result.append("\nBorrow failed as you have already borrowed one copy of this book");
                return result.toString();
            }

            String updateBooks = "UPDATE book set numleft = "+(bookLeft-1)+" WHERE isbn ="+isbn+";";
            String dueDate =  year + "-" + correctMonth + "-" + day ;
            String insertBorrow = "Insert into cust_book VALUES("+isbn+ ","+ "'" + year + "-" + correctMonth + "-" + day + "',"+CID+");";

            Statement upBooks = conManager.createStatement();
            Statement inBorrow = conManager.createStatement();
            //final confimation
            String confirmation = "Borrow summary:\nID: "+CID+"     First Name:"+cfname+" Last Name: "+lname+"\n"
                                    +"Book Title: "+ title+" ISBN: "+ISBN+"\nDue Date: "+dueDate;
            int choice = JOptionPane.YES_NO_OPTION;
            int chosen = JOptionPane.showConfirmDialog(null,confirmation);
            if(chosen!=JOptionPane.YES_OPTION){
                result.append("Borrow canceled,Thanks for using");
                return  result.toString();
            }

            //perform the transaction
            upBooks.execute(updateBooks);
            inBorrow.execute(insertBorrow);
            conManager.commit();
            //sucessfully borrowed , print out all the information
            result.append("Successfully Borrowed, Please check the record below\n")
                    .append("Customer Name: ").append(cfname).append(lname)
                    .append("\nCustomerID: ").append(CID);
            result.append("\nBook Title: ").append(title).append(" ISBN: ").append(ISBN);
            result.append("\nDue Date: ").append(dueDate);

        }catch (SQLException e){
            e.printStackTrace();
           if(conManager!=null){
               try{
                   conManager.rollback(checkpoint);
               }catch (SQLException e1){e1.printStackTrace();}
           }
        }
        finally {
            try {
                if(preCheckBook!=null) preCheckBook.close();
                if(pChkCustomer!=null) pChkCustomer.close();
                conManager.setAutoCommit(true);
            }catch (SQLException e){e.printStackTrace();}
        }
	return result.toString();
    }

    public String returnBook(int isbn, int customerID) {
	return "Return Book Stub";
    }

    public void closeDBConnection() {
        try{
            conManager.close();
        }catch (java.sql.SQLException e){e.printStackTrace();}
    }
    
    public String deleteCus(int customerID) {
    	return "Delete Customer";
    }
    
    public String deleteAuthor(int authorID) {
    	return "Delete Author";
    }
    
    public String deleteBook(int isbn) {
    	return "Delete Book";
    }
}