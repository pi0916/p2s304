/*
 * LibraryModel.java
 * Author:
 * Created on:
 */



import javax.swing.*;
import java.sql.*;

public class LibraryModel {

    // For use in creating dialogs and making them modal
    private JFrame dialogParent;
    //Initialization
    private final String url = "jdbc:postgresql://localhost:5432/300387709_jdbc";
    private Connection conManager;
    private String errorMessage = "\nThere are no matching record according to the information you supplied." +
                            "\nPlease check your input and try again";
    private String p16Spaces = "                ";
    private String divider = "\n==========================================================\n";
    private Statement stmt;
    private ResultSet rs;



    public LibraryModel(JFrame parent, String userid, String password) {
	dialogParent = parent;
	try {
	    //set up the connections,initialize the statement object
        conManager = DriverManager.getConnection(url,userid,password);
        stmt = conManager.createStatement();
    }catch (java.sql.SQLException e) {e.printStackTrace();}
    }

    public String bookLookup(int isbn) {

        StringBuilder qResult =  new StringBuilder();
        String banner = "Please check the  query result of book look up is below:\n";
        qResult.append(banner);
        try {
            rs = stmt.executeQuery("SELECT * FROM book WHERE isbn= "+isbn+";");
            //Get the info from books first
            if (rs.next()) {
                qResult.append("\nISBN: ").append(rs.getInt(1));
                qResult.append("\nTitle: ") .append(rs.getString(2));
                qResult.append("\nEdition NUmber: ").append(rs.getString(3));
                qResult.append("\nNumbers of copies: ").append(rs.getString(4));
                qResult.append("\nNumber of copies left: ").append(rs.getString(5));
                qResult.append("\nAuthors: ");
                //Then retrieve the Author's infomation
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
                qResult.append(errorMessage);
            }
        }catch (SQLException e) {e.printStackTrace();}

	return qResult.toString();
    }

    public String showCatalogue() {

        StringBuilder qResult =  new StringBuilder();
        String banner = "Please check the information of all the books (Catalogue) below:\n";
        qResult.append(banner);
        try {
            rs = stmt.executeQuery("SELECT * FROM book ;");
            if(!rs.next()) {
                qResult.append(errorMessage);
                return qResult.toString();
            }
            while(rs.next()) {
                qResult.append(divider);
                qResult.append("\nISBN: ").append(rs.getInt(1));
                qResult.append("\nTitle: ") .append(rs.getString(2));
                qResult.append("\nEdition Number: ").append(rs.getString(3));
                qResult.append("\nNumbers of copies: ").append(rs.getString(4));
                qResult.append("\nNumber of copies left: ").append(rs.getString(5));
            }
        }catch (SQLException e) {e.printStackTrace();}
        return qResult.toString();
    }

    public String showLoanedBooks() {
        StringBuilder qResult =  new StringBuilder();
        String banner = "Please check the information of loaned books below:\n";
        qResult.append(banner);
        try {
            rs = stmt.executeQuery("SELECT isbn,title,customerid,f_name,l_name,duedate"+
                                        " FROM (book natural join cust_book) natural join customer;");
            if(!rs.next()) {
                qResult.append(errorMessage);
                return qResult.toString();
            }
            while(rs.next()) {
                qResult.append("\nISBN: ").append(rs.getInt(1));
                qResult.append("\nTitle: ").append(rs.getString(2));
                qResult.append("\nCustomer  ID : ").append(rs.getString(3));
                qResult.append("\nCustomer Name: ").append(rs.getString(4))
                                                     .append("   ").append(rs.getString(5));
                qResult.append("\nDue Date: ").append(rs.getString(6));
            }
        }catch (SQLException e) {e.printStackTrace();}
        return qResult.toString();
    }

    public String showAuthor(int authorID) {
        StringBuilder qResult =  new StringBuilder();
        String banner = "Please check the Author's information of your query below:\n";
        qResult.append(banner);
        try {
            rs = stmt.executeQuery("SELECT * FROM author WHERE authorid ="+authorID+";");

            if(rs.next()) {
                qResult.append("\nAuthor  ID: ").append(rs.getInt(1));
                qResult.append("\nFirst Name: ").append(rs.getString(2));
                qResult.append("\nLast  Name: ").append(rs.getString(3));
            }
            else{
                qResult.append(errorMessage);
            }

        }catch (SQLException e) {e.printStackTrace();}
        return qResult.toString();
    }

    public String showAllAuthors() {
        StringBuilder qResult =  new StringBuilder();
        String banner = "Please check all the Author's information below:\n";
        qResult.append(banner);
        try {
            rs = stmt.executeQuery("SELECT * FROM author ;");
            if(!rs.next()) {
                qResult.append(errorMessage);
                return qResult.toString();
            }
            while(rs.next()) {
                qResult.append(divider);
                qResult.append("\nAuthor  ID: ").append(rs.getInt(1));
                qResult.append("\nFirst Name: ").append(rs.getString(2));
                qResult.append("\nLast  Name: ").append(rs.getString(3));
            }
        }catch (SQLException e) {e.printStackTrace();}
        return qResult.toString();
    }

    public String showCustomer(int customerID) {
        StringBuilder qResult =  new StringBuilder();
        String banner = "Please check the Customers' information of your query below:\n";
        qResult.append(banner);
        try {
            rs = stmt.executeQuery("SELECT * FROM customer WHERE customerid ="+customerID+";");

            if(rs.next()) {
                qResult.append("\nCustomer ID:  ").append(rs.getInt(1));
                qResult.append("\nFirst  Name:  ") .append(rs.getString(2));
                qResult.append("\nLast   Name:  ").append(rs.getString(3));
                qResult.append("\ncity:  ").append(rs.getString(4));
            }
            else{
                qResult.append(errorMessage);
            }

        }catch (SQLException e) {e.printStackTrace();}
        return qResult.toString();
    }

    public String showAllCustomers() {
        StringBuilder qResult =  new StringBuilder();
        String banner = "Please check all the Customers' information below:\n";
        qResult.append(banner);
        try {
            rs = stmt.executeQuery("SELECT * FROM customer ;");

            if(!rs.next()) {
                qResult.append(errorMessage);
                return qResult.toString();
            }
            while(rs.next()) {
                qResult.append(divider);
                qResult.append("\nCustomer ID:  ").append(rs.getInt(1));
                qResult.append("\nFirst  Name:  ") .append(rs.getString(2));
                qResult.append("\nLast   Name:  ").append(rs.getString(3));
                qResult.append("\ncity:  ").append(rs.getString(4));
            }
        }catch (SQLException e) {e.printStackTrace();}
        return qResult.toString();
    }

    public String borrowBook(int isbn, int customerID,
			     int day, int month, int year) {
	return "Borrow Book Stub";
    }

    public String returnBook(int isbn, int customerid) {
	return "Return Book Stub";
    }

    public void closeDBConnection() {
        try{
            rs.close();
            stmt.close();
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