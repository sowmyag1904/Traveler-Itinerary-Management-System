import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class TransPromoEditDB extends HttpServlet{ 
    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
    static final String DB_URL="jdbc:mysql://localhost:3306/TIMS";

    //  Database credentials
    static final String USER = "root"; 
    static final String PASS = "sqldb";

    public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException{
        response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		String title = "Edit Transit Details";
      
		out.println(
         "<html>\n" +
         "<head>"+
		 "<link rel=\"stylesheet\" href=\"PromRecTable.css\">"+
		 "<title>"+title+"</title></head>\n" +
         "<body style = \"background-image:url('images/painting-1682416389136-9172.jpg'); background-repeat:no-repeat; background-size:cover; opacity:0.8\">\n");

		   
         try{
			// Register JDBC driver
			Class.forName(JDBC_DRIVER);
			// Open a connection
			Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);

            String tid = request.getParameter("transit_id");
			int prom_pri = Integer.parseInt(request.getParameter("prom_pri"));
            
            // Execute SQL query
            String sql = "UPDATE Transit SET prom_priority = "+prom_pri+ " WHERE transit_id= '" + tid + "'";
		    PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.execute(sql);
			stmt.close(); 

			Statement st = conn.createStatement();
			String sql1 = "SELECT * FROM Transit WHERE transit_id= '" + tid + "'";
			ResultSet rs = st.executeQuery(sql1);
            out.print("<table>");
			out.print("<tr><th>Transit ID</th>");
			out.print("<th>Transit Type</th>");
			out.print("<th>Source</th>");
			out.print("<th>Destination</th>");
			out.print("<th>Start Time</th>");
			out.print("<th>End Time</th>");
			out.print("<th>Cost</th>");
			out.print("<th>Promotion Priority</th>");
			out.print("</tr>");

			if(rs.next())
			{
			  rs.previous(); 
			  while(rs.next()){ 
				String transit_id = rs.getString("transit_id");
				String trans_type = rs.getString("trans_type");
				String source = rs.getString("source");
				String dest = rs.getString("dest");
				String start_time = rs.getString("start_time");
				String end_time = rs.getString("end_time");
				float cost = rs.getFloat("cost");
				int prom_priority = rs.getInt("prom_priority");
				
				//Display values
				out.print("<tr><td>" + transit_id + "</td>");
				out.print("<td>" + trans_type + "</td>");
				out.print("<td>" + source + "</td>");
				out.print("<td>" + dest + "</td>");
				out.print("<td>" + start_time + "</td>");
				out.print("<td>" + end_time + "</td>");
				out.print("<td>" + cost + "</td>");
				out.print("<td>" + prom_pri + "</td></tr>"); //prom_priority
			  }
			  out.println("</table>");
			}
			else{
			 out.print("</table>");
			 out.println("<p>No Records Found</p>");
			} 
            // Close all the connections
            rs.close();
			st.close();
            conn.close();
		} 
		catch(SQLException se) {
         //Handle errors for JDBC
            out.println("<p>Error</p>");
            se.printStackTrace();
		} 
        catch(Exception e){
            out.println("<p>Error</p>");
        }
      }
    
}

