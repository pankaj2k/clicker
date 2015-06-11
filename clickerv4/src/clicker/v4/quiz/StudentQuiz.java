package clicker.v4.quiz;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import clicker.v4.databaseconn.DatabaseConnection;

/**
 * Servlet implementation class StudentQuiz
 */
public class StudentQuiz extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public StudentQuiz() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		String courseId = (String) session.getAttribute("couseId");
		String quizName = (String) session.getAttribute("quizName");
		String instrId = (String) session.getAttribute("instrId");
		PrintWriter out = response.getWriter();
		out.println("<html>\n<head></head>\n<body>\n");
		int total = 0;

		Map<String, String> ansMap = new HashMap<String, String>();
		Map<String, String> guessMap = new HashMap<String, String>();

		DatabaseConnection dbconn = new DatabaseConnection();
		Connection con = dbconn.createDatabaseConnection();
		String questionsql = "select QuestionID,OptionID from options "
				+ "where OptionCorrectness=1 and QuestionID in "
				+ "(select QuestionID from question where questionid in"
				+ "(select questionid from quizquestion where "
				+ "quizid=(select quizid from quiz "
				+ String.format(
						"where quizname='%s' and instrid='%s' and courseid='%s')))",
						quizName, instrId, courseId);
		try {
			PreparedStatement pstmt = con.prepareStatement(questionsql);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				ansMap.put(rs.getString("QuestionID"), rs.getString("OptionID"));
				//out.println(rs.getString("QuestionID")+" "+rs.getString("OptionID")+"<br>");
			}
		} catch (Exception exe) {
			System.err.println("Error fetching ques,ans pairs");
			exe.printStackTrace();
		}

		Map<String, String[]> paramMap = request.getParameterMap();
		for (String key : paramMap.keySet()) {
			//out.println(key+" "+paramMap.get(key)[0]+"<br>");
			if (key.equals("totalQues")) {
				try {
					total = Integer.parseInt(paramMap.get(key)[0]);
				} catch (Exception exe) {
					System.err.println("Error casting total questions value");
				}
			} else if (key.matches("\\d+")) {
				String[] pair = paramMap.get(key)[0].split(":");
				guessMap.put(pair[0], pair[1]);
			}
		}

		int correct = 0;
		for (String key : guessMap.keySet())
			if (ansMap.get(key).equals(guessMap.get(key)))
				correct++;

		
		out.println("<h3>Your score is " + (100.0 * correct / total)+"</h3>");	//guessMap.size()
		out.println("<a href='student.jsp'>Back</a>");
		out.println("</body>");
	}
}
