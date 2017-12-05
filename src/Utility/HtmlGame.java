package Utility;

public class HtmlGame
{
    public static String makeGamePanelHtml()
    {
        StringBuilder ret = new StringBuilder();

        ret.append("<h1>ゲームで使用中のパネル</h1>");
        ret.append("<table>");
        ret.append("    <tr>");
        ret.append("        <td><%=match.getMatchNo()%></td>");
        ret.append("        <%for (User u : match.getUserList()) {%>");
        ret.append("            <td><%=u.getKey()%></td>");
        ret.append("            <td><%=u.getName()%></td>");
        ret.append("        <% } %>");
        ret.append("        <td><%=match.getStartTime()%></td>");
        ret.append("        <td><%=match.getEndTime()%></td>");
        ret.append("        <td><%=match.getPanelList().size()%></td>");
        ret.append("    </tr>");
        ret.append("</table>");

        ret.append("<table>");
        ret.append("    <tr>");
        ret.append("        <%");
        ret.append("        ArrayList<Panel> panelList = match.getPanelList();");
        ret.append("        %>");
        ret.append("        <%for (int i = 0; i < panelList.size(); i++) {%>");
        ret.append("            <td>");
        ret.append("                <%if (!match.isEnableContinue() || panelList.get(i).isUsed()) {%>");
        ret.append("                    <%=panelList.get(i).getBaseWord()%>");
        ret.append("                <%} else {%>");
        ret.append("                    <a href=\"game?selectedPanel=<%=i%>\">");
        ret.append("                    <%=panelList.get(i).getBaseWord()%>");
        ret.append("                    </a>");
        ret.append("                <%}%>");
        ret.append("            </td>");
        ret.append("            <%if (i != 0 && i % 3 == 2) {%>");
        ret.append("                </tr>");
        ret.append("                <tr>");
        ret.append("            <%}%>");
        ret.append("        <%}%>");
        ret.append("    </tr>");
        ret.append("</table>");

        return ret.toString();
    }

    public static String makeUserSelectedHtml()
    {
        return null;
    }
}
