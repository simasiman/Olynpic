package ControlPack;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import modelPack.Match;
import modelPack.MatchList;
import modelPack.User;

/**
 * URL:resultに対するPost時の処理
 */
@SuppressWarnings("serial")
public class ResultServlet extends HttpServlet
{
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        doPost(req, resp);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        req.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession();

        String urlPath = "/result.jsp";

        try
        {
            String key = (String) session.getAttribute("key");

            // 終了済のマッチングを検索
            Match match = MatchList.getMatchFinished(key);
            if (match == null)
            {
                // 正規ルートでのアクセスでないと判断し、トップに戻す
                req.setAttribute("match", null);
                resp.sendRedirect("top");

                return;
            }

            // 対象マッチングのユーザ閲覧フラグをONにする
            User user = match.getUser(key);
            user.setResultWatch(true);

            req.setAttribute("matchResult", match);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        ServletContext context = getServletContext();
        RequestDispatcher rd = context.getRequestDispatcher(urlPath);
        rd.forward(req, resp);
    }
}
