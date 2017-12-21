package ControlPack;

import java.io.IOException;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import Utility.Utility;
import modelPack.Panel;
import modelPack.SiritoriDao;
import modelPack.Word;

/**
 * URL:topに対するPost時の処理
 */
@SuppressWarnings("serial")
@WebServlet("/regist")
@MultipartConfig(location = "/tmp", maxFileSize = 1048576)
public class RegistServlet extends HttpServlet
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

        String urlPath = "/panelRegistration.jsp";
        String message = "";

        if (req.getParameter("isUpload") != null)
        {
            try
            {
                // パネル用画像のアップロード
                Part part = req.getPart("panelImage");
                String name = this.getFileName(part);
                if (!fileNameCheck(name))
                {
                    throw new Exception("画像ファイルを指定してください。");
                }
                String folderPath = getServletContext().getRealPath("/img/userUpload") + "/";
                String fileName = createNewFileName(name);
                part.write(folderPath + fileName);

                // データベースへの登録を行う
                Panel panel = new Panel();
                panel.setBaseWord(req.getParameter("panelName"));
                panel.setPicture(fileName);

                for (int i = 1; i <= 8; i++)
                {
                    if (req.getParameter("Disp" + i) != null && !req.getParameter("Disp" + i).isEmpty() &&
                            req.getParameter("Read" + i) != null && !req.getParameter("Read" + i).isEmpty())
                    {
                        String disp = req.getParameter("Disp" + i);
                        String read = Utility.convertHirakana(req.getParameter("Read" + i));

                        Word word = new Word(0, disp, read, "", "");
                        panel.addWordList(word);
                    }
                }

                if (panel.getWordList().size() >= 8)
                {
                    (new SiritoriDao()).insertUserPanel(panel);
                    message = "パネルの登録に成功しました。";

                    Utility.outputLog(req, "Panel Inserted : " + req.getParameter("panelName"));
                }
            }
            catch (SQLException e)
            {
                e.printStackTrace();
                message = "！！パネルの登録に失敗しました。！！";
            }
            catch (IOException e)
            {
                e.printStackTrace();
                message = "！！画像のアップロードに失敗しました。！！";
            }
            catch (Exception e)
            {
                e.printStackTrace();
                message = "！！原因不明のエラーが発生しました！！";
            }
        }

        req.setAttribute("message", message);

        ServletContext context = getServletContext();
        RequestDispatcher rd = context.getRequestDispatcher(urlPath);
        rd.forward(req, resp);
    }

    private String getFileName(Part part)
    {
        String name = null;
        for (String dispotion : part.getHeader("Content-Disposition").split(";"))
        {
            if (dispotion.trim().startsWith("filename"))
            {
                name = dispotion.substring(dispotion.indexOf("=") + 1).replace("\"", "").trim();
                name = name.substring(name.lastIndexOf("\\") + 1);
                break;
            }
        }
        return name;
    }

    private boolean fileNameCheck(String fileName)
    {
        String regex = "(?i:.*\\.(bmp|gif|jpg|jpeg|png))";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(fileName);
        return matcher.matches();
    }

    private String createNewFileName(String name)
    {
        // 拡張子の検索
        String extension = name.substring(name.lastIndexOf("."), name.length());

        return Utility.getDefaultKey() + extension;
    }

}
