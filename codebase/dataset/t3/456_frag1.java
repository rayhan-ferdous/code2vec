    private boolean _jspx_meth_html_005fform_005f0(PageContext _jspx_page_context) throws Throwable {

        PageContext pageContext = _jspx_page_context;

        JspWriter out = _jspx_page_context.getOut();

        org.apache.struts.taglib.html.FormTag _jspx_th_html_005fform_005f0 = (org.apache.struts.taglib.html.FormTag) _005fjspx_005ftagPool_005fhtml_005fform_005faction.get(org.apache.struts.taglib.html.FormTag.class);

        _jspx_th_html_005fform_005f0.setPageContext(_jspx_page_context);

        _jspx_th_html_005fform_005f0.setParent(null);

        _jspx_th_html_005fform_005f0.setAction("/cadastroUsuario");

        int _jspx_eval_html_005fform_005f0 = _jspx_th_html_005fform_005f0.doStartTag();

        if (_jspx_eval_html_005fform_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {

            do {

                out.write('\r');

                out.write('\n');

                out.write('	');

                if (_jspx_meth_html_005fhidden_005f0(_jspx_th_html_005fform_005f0, _jspx_page_context)) return true;

                out.write('\r');

                out.write('\n');

                out.write('	');

                if (_jspx_meth_html_005fhidden_005f1(_jspx_th_html_005fform_005f0, _jspx_page_context)) return true;

                out.write("\r\n");

                out.write("\r\n");

                out.write("\t\r\n");

                out.write("\t<fieldset><legend>Filtrar por:</legend>\r\n");

                out.write("\t<p class=\"nota\">Os campos marcados com \" * \" são obrigatórios</p>\r\n");

                out.write("\r\n");

                out.write("\r\n");

                out.write("\t\r\n");

                out.write("\t<label for=\"nome\">Nome:</label> ");

                if (_jspx_meth_html_005ftext_005f0(_jspx_th_html_005fform_005f0, _jspx_page_context)) return true;

                out.write("\r\n");

                out.write("\t<hr />\r\n");

                out.write("\t<label for=\"login\">Login:</label> ");

                if (_jspx_meth_html_005ftext_005f1(_jspx_th_html_005fform_005f0, _jspx_page_context)) return true;

                out.write("\r\n");

                out.write("\r\n");

                out.write("\t<hr />\r\n");

                out.write("\t<label for=\"grupo\">Grupo:</label> ");

                if (_jspx_meth_html_005fselect_005f0(_jspx_th_html_005fform_005f0, _jspx_page_context)) return true;

                out.write("\r\n");

                out.write("\r\n");

                out.write("\t<p class=\"comandos\">");

                if (_jspx_meth_html_005flink_005f1(_jspx_th_html_005fform_005f0, _jspx_page_context)) return true;

                out.write("</p>\r\n");

                out.write("\r\n");

                out.write("\t</fieldset>\r\n");

                out.write("\r\n");

                out.write("\r\n");

                out.write("\t<h3>Lista de Usuários encontrados:</h3>\r\n");

                out.write("\r\n");

                out.write("\t<p class=\"comandosgerais\">");

                if (_jspx_meth_html_005flink_005f2(_jspx_th_html_005fform_005f0, _jspx_page_context)) return true;

                out.write("</p>\r\n");

                out.write("\r\n");

                out.write("\t<table width=\"70%\" class=\"posicaoTabela\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\r\n");

                out.write("\t\t");

                if (_jspx_meth_display_005ftable_005f0(_jspx_th_html_005fform_005f0, _jspx_page_context)) return true;

                out.write("\r\n");

                out.write("\r\n");

                out.write("\t</table>\r\n");

                out.write("\r\n");

                out.write("\t");

                int evalDoAfterBody = _jspx_th_html_005fform_005f0.doAfterBody();

                if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN) break;

            } while (true);

        }

        if (_jspx_th_html_005fform_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {

            _005fjspx_005ftagPool_005fhtml_005fform_005faction.reuse(_jspx_th_html_005fform_005f0);

            return true;

        }

        _005fjspx_005ftagPool_005fhtml_005fform_005faction.reuse(_jspx_th_html_005fform_005f0);

        return false;

    }
