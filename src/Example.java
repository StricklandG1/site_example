import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.annotation.Resource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;


/**
 * Servlet implementation class Example
 */
@WebServlet(name = "Example", urlPatterns = "/Example")
public class Example extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Resource(name = "jdbc/testdb")
	private DataSource dataSource;
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		
		try
		{
			// Get the parameter from the ajax post message
			String id = request.getParameter("id");
			System.out.println("ID: " + id);
			HttpSession session = request.getSession();
			Context initCtx = new InitialContext();
			
			Context envCtx = (Context) initCtx.lookup("java:comp/env");
			if (envCtx == null)
				out.println("envCtx is NULL");
			DataSource ds = (DataSource) envCtx.lookup("jdbc/example");
			
			if (ds == null)
				out.println("ds is NULL");
			
			Connection dbcon = ds.getConnection(); // connect to db from
			if (dbcon == null)
				out.println("dbcon is NULL");
			
			
			String query = "SELECT * FROM example_set WHERE id=?"; // query string
			PreparedStatement stmt = dbcon.prepareStatement(query); // SQL statment to be executed
			stmt.setString(1, id); // Setting string using predetermined statement to avoid SQL injections
			
			ResultSet rs = stmt.executeQuery(); // execute query and store in a ResultSet
			
			if (rs.next()) // if result set is not empty ...
			{
				System.out.println("Data: " + rs.getString("data"));
				JsonObject responseObj = new JsonObject(); // Create json Object to pass back to frontend
				responseObj.addProperty("data", rs.getString("data")); // rs.getString("data") - get the value from the "data"
				
				response.getWriter().write(responseObj.toString()); //send the json object to front end as string

			}
			response.setStatus(200); // custom status code to make sure it at least made it this far
			
			// Good practice to close active connections
			rs.close();
			stmt.close();
			dbcon.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("errorMessage", e.getMessage());
			
			response.setStatus(500);
			
		}
	}
}
