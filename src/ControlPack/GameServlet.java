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
import modelPack.Panel;

@SuppressWarnings("serial")
public class GameServlet extends HttpServlet
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

        String urlPath = "/game.jsp";

        try
        {
            String key = (String) session.getAttribute("key");
            if (key == null || key.isEmpty())
            {
                // 正規ルートでのアクセスでないと判断し、トップに戻す
                req.setAttribute("match", null);
                resp.sendRedirect("top");

                return;
            }

            // 既にマッチング済のユーザであれば、試合に戻らせる
            Match m = MatchList.getMatch(key);
            if (m == null)
            {
                // 終了済みマッチングリストからもユーザを検索
                m = MatchList.getMatchFinished(key);
            }

            if (m == null)
            {
                // マッチング列に存在しないユーザの場合、トップ画面に戻る
                req.setAttribute("match", null);
                resp.sendRedirect("top");

                return;
            }

            if (!m.isStart())
            {
                // 開始されていない状態であれば、マッチング画面に戻る
                req.setAttribute("match", m);
                resp.sendRedirect("matching");

                return;
            }

            // TODO : デバッグモード有効状態
            if (req.getParameter("debugEnd") != null)
            {
                m.finishMatch();
            }

            // タイマーが意図せず停止している場合に備え、チェック処理を追加する
            m.timeOutCheck();
            if (!m.isFinish() && m.isHisTurn(key))
            {
                String paramSelectedPanel = (String) req.getParameter("selectedPanel");
                if (paramSelectedPanel != null && !paramSelectedPanel.isEmpty())
                {
                    // ゲーム継続時
                    try
                    {
                        int selected = Integer.parseInt(paramSelectedPanel);
                        Panel selectedPanel = m.getPanelList().get(selected);

                        if (m.isPanelRange(selected) && !selectedPanel.isUsed())
                        {
                            // 使われていないパネルが選択された場合
                            if (m.isFirstPick())
                            {
                                // 最初のパネル選択の場合
                                m.firstPick(key, selectedPanel);
                            }
                            else if (m.nextPick(key, selectedPanel))
                            {
                                // 最初以降のパネル選択の場合
                                // パネル選択可能の場合
                            }
                            else
                            {
                                // 最初以降のパネル選択の場合
                                // パネル選択不可能の場合
                            }
                        }
                    }
                    catch (IndexOutOfBoundsException e)
                    {
                        System.out.println("範囲外のパネルが指定されました。");
                    }
                    catch (NumberFormatException e)
                    {
                        System.out.println("パネルの指定で無効な数値が入力されました。");
                    }
                }

                if (!m.isEnableContinue())
                {
                    m.finishMatch();
                }
            }

            req.setAttribute("match", m);
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
