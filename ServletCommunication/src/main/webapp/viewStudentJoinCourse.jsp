<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>View Students in Course</title>
</head>
<body>
    <h1>View Students in Course</h1>
    
    <table border="1">
        <tr>
            <th>Name</th>
            <th>Email</th>
        </tr>
        <%
            // This scriptlet is replaced with Java code executed at runtime
            // Retrieve and display student information from the servlet
            request.getRequestDispatcher("/viewStudentsInCourseServlet").include(request, response);
        %>
    </table>
</body>
</html>
