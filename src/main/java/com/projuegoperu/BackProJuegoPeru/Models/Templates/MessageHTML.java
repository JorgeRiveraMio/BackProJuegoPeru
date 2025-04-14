package com.projuegoperu.BackProJuegoPeru.Models.Templates;

public class MessageHTML {
    public static final String TEMPLATE_PRUEBA = "<html lang=\"en\">\r\n" +
            "<head>\r\n" +
            "    <meta charset=\"UTF-8\">\r\n" +
            "    <style>\r\n" +
            "        .bg{\r\n" +
            "            width: 100%; background: white;\r\n" +
            "        }\r\n" +
            "        .cont{\r\n" +
            "            width: 600px; margin: 0 auto; background: white;\r\n" +
            "        }\r\n" +
            "        .boxCont{\r\n" +
            "            width: 70%; margin: 0 auto;\r\n" +
            "        }\r\n" +
            "        .boxCont p {\r\n" +
            "            font-size: 18px; color: #231f20;\r\n" +
            "        }\r\n" +
            "        .boxCode{\r\n" +
            "            text-align: center; border-radius: 10px; background-color: #7788e7; font-size: 16px; padding: auto;\r\n" +
            "            display: flex; justify-content: center;\r\n" + // Usamos flex para alinearlos horizontalmente
            "        }\r\n" +
            "        .boxCode div {\r\n" +  // Asegura que cada número esté en su propio div
            "            width: 40px; height: 40px; font-size: 32px; display: inline-block; border-radius: 5px; text-align: center;\r\n" +
            "            color: #fff; background-color: #7788e7; line-height: 40px;\r\n" +
            "        }\r\n" +
            "        .boxCont .footer{\r\n" +
            "            font-size: 12px; color: #949595; text-align: center;\r\n" +
            "        }\r\n" +
            "        .cont .fo{\r\n" +
            "            width: 100%; height: 10px; margin: 20px 0 0 0;\r\n" +
            "            background: linear-gradient(90deg, #4155c4, #1b04a1);\r\n" +
            "        }\r\n" +
            "    </style>\r\n" +
            "</head>\r\n" +
            "<body>\r\n" +
            "    <div class=\"bg\">\r\n" +
            "        <div class=\"cont\">\r\n" +
            "            <div class=\"boxCont\">\r\n" +
            "                <p> <strong>Estimado usuario</strong>,</p>\r\n" +
            "                <p>Su código de verificación para el inicio de sesión es: </p>\r\n" +
            "                <div class=\"boxCode\">\r\n" +
            "                    <div>{0}</div>\r\n" +
            "                    <div>{1}</div>\r\n" +
            "                    <div>{2}</div>\r\n" +
            "                    <div>{3}</div>\r\n" +
            "                    <div>{4}</div>\r\n" +
            "                    <div>{5}</div>\r\n" +
            "                </div>\r\n" +
            "                <p>Este código es de un solo uso.</p>\r\n" +
            "                <br>\r\n" +
            "                <p class=\"footer\">Este mensaje de correo electrónico se ha enviado desde una \r\n" +
            "                    dirección exclusivamente para envíos. No responda a este mensaje</p>\r\n" +
            "            </div>\r\n" +
            "        </div>\r\n" +
            "    </div>\r\n" +
            "</body>\r\n" +
            "</html>";
}
