package contorollers.reports;

import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Report;
import models.validators.ReportValidator;
import utils.DBUtil;

/**
 * Servlet implementation class ReportsUpdateServlet
 */
@WebServlet("/reports/update")
public class ReportsUpdateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReportsUpdateServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    String _token = (String)request.getParameter("_token");
	    if(_token != null && _token.equals(request.getSession().getId())){
	        EntityManager em = DBUtil.createEntityManager();

	        //該当するIDのデータを一件探す
	        Report r = em.find(Report.class, (Integer)(request.getSession().getAttribute("report_id")));

	        //各フィールドにデータをセット
	        r.setReport_date(Date.valueOf(request.getParameter("report_date")));
	        r.setTitle(request.getParameter("title"));
	        r.setContent(request.getParameter("content"));
	        r.setUpdated_at(new Timestamp(System.currentTimeMillis()));

	        //バリデーションを実行しエラーがある場合は編集画面へ戻す
	        List<String> errors = ReportValidator.validate(r);
	        if(errors.size() > 0){
	            request.setAttribute("report", r);
	            request.setAttribute("_token", request.getSession().getId());
	            request.setAttribute("errors", errors);

	            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/reports/edit.jsp");
                rd.forward(request, response);
                //エラーがない場合はデータベースの更新を行う
                }else{
                    em.getTransaction().begin();
                    em.persist(r);
                    em.getTransaction().commit();
                    em.close();
                    //更新完了のflushメッセージを登録
                    request.getSession().setAttribute("flush", "更新が完了しました。");

                    request.getSession().removeAttribute("report_id");

                    response.sendRedirect(request.getContextPath() + "/reports/index");
                }



	    	}
	}
}
