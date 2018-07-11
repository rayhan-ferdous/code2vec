    private boolean _jspx_meth_html_005flink_005f4(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f1, PageContext _jspx_page_context) throws Throwable {

        PageContext pageContext = _jspx_page_context;

        JspWriter out = _jspx_page_context.getOut();

        org.apache.struts.taglib.html.LinkTag _jspx_th_html_005flink_005f4 = (org.apache.struts.taglib.html.LinkTag) _005fjspx_005ftagPool_005fhtml_005flink_005fstyleId_005fstyleClass_005fonclick_005fhref.get(org.apache.struts.taglib.html.LinkTag.class);

        _jspx_th_html_005flink_005f4.setPageContext(_jspx_page_context);

        _jspx_th_html_005flink_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f1);

        _jspx_th_html_005flink_005f4.setStyleId("linkEditar");

        _jspx_th_html_005flink_005f4.setHref("#");

        _jspx_th_html_005flink_005f4.setOnclick("salvar()");

        _jspx_th_html_005flink_005f4.setStyleClass("botao");

        int _jspx_eval_html_005flink_005f4 = _jspx_th_html_005flink_005f4.doStartTag();

        if (_jspx_eval_html_005flink_005f4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {

            if (_jspx_eval_html_005flink_005f4 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {

                out = _jspx_page_context.pushBody();

                _jspx_th_html_005flink_005f4.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);

                _jspx_th_html_005flink_005f4.doInitBody();

            }

            do {

                out.write("Salvar");

                int evalDoAfterBody = _jspx_th_html_005flink_005f4.doAfterBody();

                if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN) break;

            } while (true);

            if (_jspx_eval_html_005flink_005f4 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {

                out = _jspx_page_context.popBody();

            }

        }

        if (_jspx_th_html_005flink_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {

            _005fjspx_005ftagPool_005fhtml_005flink_005fstyleId_005fstyleClass_005fonclick_005fhref.reuse(_jspx_th_html_005flink_005f4);

            return true;

        }

        _005fjspx_005ftagPool_005fhtml_005flink_005fstyleId_005fstyleClass_005fonclick_005fhref.reuse(_jspx_th_html_005flink_005f4);

        return false;

    }
