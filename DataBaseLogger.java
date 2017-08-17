import java.sql.*;
public class DataBaseLogger
{
private DataBaseLogger()
{
}
private static String inputFileName;
public static void setName(String fileName)
{
inputFileName=fileName;
System.out.println(inputFileName);
}

public static void addFile()
{
System.out.println(inputFileName);
try
{
Class.forName("com.mysql.jdbc.Driver");
Connection c;
c=DriverManager.getConnection("jdbc:mysql://localhost:3306/FileLogDB","student","student");
PreparedStatement ps;
ps=c.prepareStatement("insert into FileLog(file_name) values(?)");
ps.setString(1,inputFileName);
ps.executeUpdate();
ps.close();
c.close();
}
catch(Exception e)
{
System.out.println(e);
}
}


}