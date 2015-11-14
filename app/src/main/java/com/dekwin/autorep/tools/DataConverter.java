package com.dekwin.autorep.tools;

import android.os.Environment;

import com.dekwin.autorep.db.DatabaseHelper;
import com.dekwin.autorep.entities.Contract;
import com.dekwin.autorep.entities.Work;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by dekst on 11.11.2015.
 */
public class DataConverter {

    private static String filePath = Environment.getExternalStorageDirectory().toString() + "/AutoReport.html";

    public static void writeToHTML(Contract contract) {
        String html;
        String workList = "";

        int contractId = contract.getId();
        String responsible = contract.getResponsible().toString();
        String organization = contract.getOrganization().getName();
        String initialDate = contract.getInitialDate().get(Calendar.DAY_OF_MONTH) + "." + contract.getInitialDate().get(Calendar.MONTH) + "." + contract.getInitialDate().get(Calendar.YEAR);
        String finalDate = contract.getFinalDate().get(Calendar.DAY_OF_MONTH) + "." + contract.getFinalDate().get(Calendar.MONTH) + "." + contract.getFinalDate().get(Calendar.YEAR);
        float origPrice = contract.getOriginalPrice();
        ArrayList<Work> works = DatabaseHelper.selectWorksByContractId(DatabaseHelper.WORKS_COLUMN_NAME + " ASC", contract.getId() + "");


        workList += "<table>";

        for (Work work : works) {
            workList += "<tr><td>";
            workList += work.getName();
            workList += "</td></tr>";
        }

        workList += "</table>";
        html = "<!DOCTYPE>\n" +
                "<html>\n" +
                "<title>\n" +
                "    Контракт № " + contractId + ".\n" +
                "</title>\n" +
                "\n" +
                "<meta charset=\"UTF-8\"/>" +
                "<style>\n" +
                "    p{\n" +
                "        text-indent: 3.5em;\n" +
                "    }\n" +
                "    span{\n" +
                "        text-decoration: underline ;\n" +
                "    }\n" +
                "    td{\n" +
                "        font-family:  Georgia, 'Times New Roman', Times, serif;\n" +
                "        font-size: 14pt;\n" +
                "        font-style: italic;\n" +
                "    }\n" +
                "\n" +
                "    body {\n" +
                "        background: rgb(204,204,204);\n" +
                "    }\n" +
                "    page[size=\"A4\"] {\n" +
                "        text-align: justify;\n" +
                "        font-family:  Georgia, 'Times New Roman', Times, serif;\n" +
                "        font-size: 14pt;\n" +
                "        background: white;\n" +
                "        width: 21cm;\n" +
                "        height: 29.7cm;\n" +
                "        display: block;\n" +
                "        margin: 0 auto;\n" +
                "        margin-bottom: 0.5cm;\n" +
                "        padding: 2cm;\n" +
                "        box-shadow: 0 0 0.5cm rgba(0,0,0,0.5);\n" +
                "    }\n" +
                "    @media print {\n" +
                "        body, page[size=\"A4\"] {\n" +
                "            margin: 0;\n" +
                "            box-shadow: 0;\n" +
                "        }\n" +
                "    }\n" +
                "</style>\n" +
                "\n" +
                "<page size=\"A4\">\n" +
                "<h4 align=\"center\">КОНТРАКТ № " + contractId + "</h4>\n" +
                "<p>Организация(фирма) <span>\"Авторемонтная мастерская К\"</span>,<br>\n" +
                "    именуемая в дальнейшем \"Исполнитель\", в лице своего полномочного представителя <span>" + responsible + "</span>,\n" +
                "    с одной стороны, и организация(фирма) <span>\"" + organization + "\"</span>,<br>\n" +
                "    именуемая в дальнейшем \"Заказчик\", с другой стороны,\n" +
                "    заключили настоящий контракт о нижеследующем.<br>\n" +
                "   <p>В срок от " + initialDate + " до " + finalDate + " выполнить приведенный ниже список авторемонтных работ:<br>\n" +
                "    " + workList + "<br>\n" +
                "    На общую сумму " + origPrice + " у.е.</p>\n" +
                "</p>\n" +
                "</page>\n" +
                "</html>";


        filePath = Environment.getExternalStorageDirectory().toString() + "/AutoReport_" + contract.getId() + ".html";
        useFileWriter(html);


    }


    public static void useFileWriter(String content) {
        Writer writer = null;

        try {

            writer = new FileWriter(filePath);
            writer.write(content);

        } catch (IOException e) {


            e.printStackTrace();

        } finally {

            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {


                    e.printStackTrace();
                }
            }

        }
    }




}
