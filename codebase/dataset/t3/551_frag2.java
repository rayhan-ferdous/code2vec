    private boolean _jspx_meth_c_005fif_005f1(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fwhen_005f2, PageContext _jspx_page_context) throws Throwable {

        PageContext pageContext = _jspx_page_context;

        JspWriter out = _jspx_page_context.getOut();

        org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f1 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);

        _jspx_th_c_005fif_005f1.setPageContext(_jspx_page_context);

        _jspx_th_c_005fif_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f2);

        _jspx_th_c_005fif_005f1.setTest(((java.lang.Boolean) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${task eq 'create' or task eq 'edit'}", java.lang.Boolean.class, (PageContext) _jspx_page_context, null, false)).booleanValue());

        int _jspx_eval_c_005fif_005f1 = _jspx_th_c_005fif_005f1.doStartTag();

        if (_jspx_eval_c_005fif_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {

            do {

                out.write("\r\n");

                out.write("\t<div class=\"type-text\"><label for=\"password\">Passwort:<sup>*</sup></label>\r\n");

                out.write("\t<input id=\"password\" name=\"password\" type=\"password\" size=\"30\" value=\"\" /></div>\r\n");

                out.write("\t<div class=\"type-text\"><label for=\"password\">Passwort:<sup>*</sup></label>\r\n");

                out.write("\t<input id=\"password2\" name=\"password2\" type=\"password\" size=\"30\"\r\n");

                out.write("\t\tvalue=\"\" /></div>\r\n");

                int evalDoAfterBody = _jspx_th_c_005fif_005f1.doAfterBody();

                if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN) break;

            } while (true);

        }

        if (_jspx_th_c_005fif_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {

            _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f1);

            return true;

        }

        _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f1);

        return false;

    }
