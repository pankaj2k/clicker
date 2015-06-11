<%--@author rajavel, Clicker Team, IDL Lab - IIT Bombay  
This jsp file is act as helper for quiz conduction
--%>

<%@page import="clicker.v4.quiz.Newquizhelper"%>
<%--<%@page import="clicker.v4.global.Global"%>
<%@page import="clicker.v4.quiz.QuizHelper"%>
<%@page import="clicker.v4.wrappers.Quiz"%>--%>
<%
	String courseId = (String) request.getParameter("courseId");
	String quizName = (String) request.getParameter("quizName");
	String instrId = (String) request.getParameter("instrId");
	
	String submit=(String)request.getParameter("subbut");
	
	if(submit==null || submit.equalsIgnoreCase("")){		
		Newquizhelper quizHelper = new Newquizhelper();
		String quizdata = quizHelper.getQuizList(courseId, quizName, instrId);
		out.print(quizdata);
	}
	session.setAttribute("couseId", courseId);
	session.setAttribute("quizName", quizName);
	session.setAttribute("instrId",instrId);	
%>