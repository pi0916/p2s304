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
    private Statement stmt;
    private String errorMessage = "\nThere are no matching record according to the information you supplied." +
                            "\nPlease check your input and try again";
    private String p16Spaces = "                ";



    public LibraryModel(JFrame parent, String userid, String password) {
	dialogParent = parent;
	try {
	    //set up the connections
        conManager = DriverManager.getConnection(url,userid,password);

    }catch (java.sql.SQLException e) {e.printStackTrace();}
    }

    public String bookLookup(int isbn) {

        ResultSet rs;
        StringBuilder qResult =  new StringBuilder();
        String banner = "Please check the  query result of book look up is below:\n";
        qResult.append(banner);
        try {
            stmt  = conManager.createStatement();
            rs = stmt.executeQuery("SELECT * FROM book WHERE isbn= "+isbn+";");
            //Get the info from books first
            if (rs.next()) {
                qResult.append("\n  ISBN:  ").append(rs.getInt(1));
                qResult.append("\n  Title:  ") .append(rs.getString(2));
                qResult.append("\n  Edition No. :  ").append(rs.getString(3));
                qResult.append("\n  Numbers of copies:  ").append(rs.getString(4));
                qResult.append("\n  Number of copies left:  ").append(rs.getString(5));
                qResult.append("\n  Authors: ");
                //Then retrive the
                rs = stmt.executeQuery("SELECT name,surname,authorseqno" +
                                         " FROM book_author NATURAL JOIN author" +
                                         " WHERE isbn =" +isbn+
                                         " ORDER BY authorseqno ASC;");

                while (rs.next()){
                    qResult.append(rs.getString("name"));
                    qResult.append(rs.getString("surname"));
                    qResult.append("Author Seq No.: ").append(rs.getString("authorseqno")).append("\n");;
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
	return "Show Catalogue Stub";
    }

    public String showLoanedBooks() {
	return "Show Loaned Books Stub";
    }

    public String showAuthor(int authorID) {
	return "Show Author Stub";
    }

    public String showAllAuthors() {
	return "Show All Authors Stub";
    }

    public String showCustomer(int customerID) {
	return "Show Customer Stub";
    }

    public String showAllCustomers() {
	return "Show All Customers Stub";
    }

    public String borrowBook(int isbn, int customerID,
			     int day, int month, int year) {
	return "Borrow Book Stub";
    }

    public String returnBook(int isbn, int customerid) {
	return "Return Book Stub";
    }

    public void closeDBConnection() {
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