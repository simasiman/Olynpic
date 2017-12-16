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

/**
 * URL:topに対するPost時の処理
 */
@SuppressWarnings("serial")
public class TopServlet extends HttpServlet
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

        String urlPath = "/top.jsp";

        try
        {
            String key = (String) session.getAttribute("key");

            // 既にマッチングが作成され、未確定の場合、それを削除する
            if (key != null && !key.isEmpty())
            {
                Match m = MatchList.getMatch(key);
                if (m != null)
                {
                    if (!m.isStart())
                    {
                        MatchList.remove(m);
                        req.setAttribute("match", null);
                    }
                }
            }
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
