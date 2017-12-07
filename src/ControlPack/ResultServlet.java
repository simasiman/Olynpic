package ControlPack;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import modelPack.FinishedMatchList;
import modelPack.Match;

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

        try
        {
            String key = (String) req.getParameter("key");
            String name = (String) req.getParameter("name");

            if (key == null || name == null)
            {
                key = (String) session.getAttribute("key");
                name = (String) session.getAttribute("name");
            }

            Match match = FinishedMatchList.getMatch(key);

            req.setAttribute("matchResult", match);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        ServletContext context = getServletContext();
        RequestDispatcher rd = context.getRequestDispatcher("/result.jsp");
        rd.forward(req, resp);
    }
}
