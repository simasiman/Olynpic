package Utility;

import java.util.ArrayList;

import modelPack.Match;
import modelPack.Panel;
import modelPack.User;
import modelPack.Word;

public class HtmlGame
{
    private static final int PANEL_HEIGHT = 80;
    private static final int PANEL_WIDTH = 120;

    private static final int PANEL_COL = 4;

    public static String makeGameReloadHtml(Match match, String key)
    {
        StringBuilder ret = new StringBuilder();

        int indentCount = 0;

        if (!match.isFinish())
        {
            ret.append(Utility.appendLineIndent(indentCount, "<a href=\"game\">更新</a>"));
        }
        else
        {
            ret.append(Utility.appendLineIndent(indentCount, "<a href=\"result\">結果画面へ</a>"));
        }

        return ret.toString();
    }

    public static String makeGameMessageHtml(Match match, String key)
    {
        StringBuilder ret = new StringBuilder();

        int indentCount = 0;

        if (match.isFinish())
        {
            // ゲーム結果の表示
            String result = "";

            int winLose = match.getUserWin(key);

            switch (winLose)
            {
                case User.WIN:
                    result = "勝ちました。";
                    break;

                case User.LOSE:
                    result = "負けました。";
                    break;

                case User.DRAW:
                    result = "引き分けです。";
                    break;
            }

            ret.append(Utility.appendLineIndent(indentCount, "<h1>※ゲームが終了しました</h1>"));
            ret.append(Utility.appendLineIndent(indentCount, "<h2>" + result + "</h2>"));
        }
        else if (match.isHisTurn(key))
        {
            // 自ターン時のメッセージを表示
            ret.append(Utility.appendLineIndent(indentCount, "<h1>※貴方のターンです</h1>"));
        }
        else
        {
            // 相手ターン時のメッセージを表示
            ret.append(Utility.appendLineIndent(indentCount, "<h1>※対戦相手のターンです</h1>"));
        }

        return ret.toString();
    }

    public static String makeGamePanelHtml(Match match, String key)
    {
        StringBuilder ret = new StringBuilder();

        int indentCount = 0;

        ret.append(Utility.appendLineIndent(indentCount, "<table>"));
        indentCount++;
        ret.append(Utility.appendLineIndent(indentCount, "<tr>"));
        indentCount++;

        ArrayList<Panel> panelList = match.getPanelList();
        for (int i = 0; i < panelList.size(); i++)
        {
            // ①現在自分のターンである
            // ②ゲームが終了していない
            // ③ゲームマッチが継続可能な状態である
            // ④対象のパネルは未選択である
            // 以上の条件を満たす場合のみ、パネルのリンクを有効にする

            Panel panel = panelList.get(i);
            String cssClass = "";
            String panelHtml = "";
            String panelImage = "<img src=\"img/panel/" + panel.getPicture() +
                    "\" height=\"" + PANEL_HEIGHT +
                    "\" width=\"" + PANEL_WIDTH + "\">";

            boolean isCanSelect = match.isHisTurn(key) && !match.isFinish() && match.isEnableContinue() && !panel.isUsed();
            boolean isCorrectSelect = !match.isFirstPick() && panel.isMatchWord(match.getNowWord()) > 0;
            boolean isUsed = !match.isFirstPick() && panel.isUsed();

            // CSS用のクラス名の定義
            if (isCanSelect)
            {
                cssClass += "canSelect ";
                if (isCorrectSelect)
                {
                    cssClass += "correct ";
                }
            }
            else if (isUsed)
            {
                cssClass += "selected ";
            }

            // 選択可能であれば<a>タグで囲う
            if (isCanSelect || isCorrectSelect)
            {
                panelHtml = "<a href=\"game?selectedPanel=" + i + "\">" + panelImage + "</a>";
            }
            else
            {
                panelHtml = panelImage;
            }

            // HTMLの書き込み
            ret.append(Utility.appendLineIndent(indentCount, "<td class=\"" + cssClass + "\">"));
            indentCount++;
            ret.append(Utility.appendLineIndent(indentCount, panelHtml));
            indentCount--;
            ret.append(Utility.appendLineIndent(indentCount, "</td>"));

            // 一定枚数ごとの改行
            if (i != 0 && i != panelList.size() - 1 && i % PANEL_COL == PANEL_COL - 1)
            {
                indentCount--;
                ret.append(Utility.appendLineIndent(indentCount, "</tr>"));
                ret.append(Utility.appendLineIndent(indentCount, "<tr>"));
                indentCount++;
            }
        }

        indentCount--;
        ret.append(Utility.appendLineIndent(indentCount, "</tr>"));
        indentCount--;
        ret.append(Utility.appendLineIndent(indentCount, "</table>"));

        if (indentCount != 0)
        {
            System.out.println("※indentCountの調整に誤りがあります。");
        }

        return ret.toString();
    }

    public static String makeUserSelectedHtml(Match match, String key)
    {
        StringBuilder ret = new StringBuilder();

        int indentCount = 0;

        // 選択指示の表示
        if (match.isFirstPick())
        {
            ret.append(Utility.appendLineIndent(indentCount, "<h3>最初に使用するパネルを選択してください</h3>"));
        }
        else
        {
            String wordRead = match.getNowWord().getWord();
            String wordHead = "「" + match.getNowWord().getWordHead() + "」";
            String wordTail = "「" + match.getNowWord().getWordTail() + "」";

            ret.append(Utility.appendLineIndent(indentCount, "<h3>" + wordHead + wordRead + wordTail + "</h3>"));
        }

        ret.append(Utility.appendLineIndent(indentCount, "<!-- ユーザ別に選択された単語の一覧の表示 -->"));
        ret.append(Utility.appendLineIndent(indentCount, "<h2>選択した単語</h2>"));

        for (User user : match.getUserList())
        {
            ret.append(Utility.appendLineIndent(indentCount, "<h3>" + user.getName() + "</h3>"));
            ret.append(Utility.appendLineIndent(indentCount, "<h4>" + user.getScore() + "</h4>"));
            ret.append(Utility.appendLineIndent(indentCount, "<ul>"));
            indentCount++;

            for (int j = 0; j < user.getSelectedPanel().size(); j++)
            {
                Panel p = user.getSelectedPanel().get(j);
                Word word = p.getSelectedWord();

                String scoreText = p.getBaseWord() + "(" + word.getWord() + ")[" + word.getBaseScore() + "+" + word.getBonusScore() + "]";

                ret.append(Utility.appendLineIndent(indentCount, "<li>" + scoreText));
            }

            indentCount--;
            ret.append(Utility.appendLineIndent(indentCount, "</ul>"));
        }

        return ret.toString();
    }
}
