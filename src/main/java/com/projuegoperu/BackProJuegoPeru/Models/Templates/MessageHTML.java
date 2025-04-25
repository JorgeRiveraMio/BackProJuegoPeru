package com.projuegoperu.BackProJuegoPeru.Models.Templates;

public class MessageHTML {
    public static final String TEMPLATE_PRUEBA = "<html lang=\"en\">\r\n" +
            "<head>\r\n" +
            "    <meta charset=\"UTF-8\">\r\n" +
            "    <style>\r\n" +
            "        .bg {\r\n" +
            "            width: 100%; background: white;\r\n" +
            "        }\r\n" +
            "        .cont {\r\n" +
            "            width: 600px; margin: 0 auto; background: white;\r\n" +
            "        }\r\n" +
            "        .boxCont {\r\n" +
            "            width: 70%; margin: 0 auto;\r\n" +
            "        }\r\n" +
            "        .boxCont p {\r\n" +
            "            font-size: 18px; color: #231f20;\r\n" +
            "        }\r\n" +
            "        .code-block {\r\n" +
            "            font-family: monospace;\r\n" +
            "            background-color: #f5f5f5;\r\n" +
            "            padding: 12px;\r\n" +
            "            border-radius: 8px;\r\n" +
            "            font-size: 24px;\r\n" +
            "            color: #000;\r\n" +
            "            text-align: center;\r\n" +
            "            max-width: 300px;\r\n" +
            "            margin: 0 auto;\r\n" +
            "        }\r\n" +
            "        .boxCont .footer {\r\n" +
            "            font-size: 12px; color: #949595; text-align: center;\r\n" +
            "        }\r\n" +
            "        .cont .fo {\r\n" +
            "            width: 100%; height: 10px; margin: 20px 0 0 0;\r\n" +
            "            background: linear-gradient(90deg, #4155c4, #1b04a1);\r\n" +
            "        }\r\n" +
            "    </style>\r\n" +
            "</head>\r\n" +
            "<body>\r\n" +
            "    <div class=\"bg\">\r\n" +
            "        <div class=\"cont\">\r\n" +
            "            <div class=\"boxCont\">\r\n" +
            "                <p><strong>Estimado usuario</strong>,</p>\r\n" +
            "                <p>Su código de verificación para el inicio de sesión es:</p>\r\n" +
            "                <pre class=\"code-block\">{0}{1}{2}{3}{4}{5}</pre>\r\n" +
            "                <p style=\"text-align: center; font-size: 13px; color: #666;\">\r\n" +
            "                    Puedes copiar este código directamente si lo necesitas.\r\n" +
            "                </p>\r\n" +
            "                <br>\r\n" +
            "                <p class=\"footer\">\r\n" +
            "                    Este mensaje de correo electrónico se ha enviado desde una\r\n" +
            "                    dirección exclusivamente para envíos. No responda a este mensaje.\r\n" +
            "                </p>\r\n" +
            "            </div>\r\n" +
            "        </div>\r\n" +
            "    </div>\r\n" +
            "</body>\r\n" +
            "</html>";
}