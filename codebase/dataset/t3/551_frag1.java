    private boolean _jspx_meth_c_005fwhen_005f1(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fchoose_005f1, PageContext _jspx_page_context, int[] _jspx_push_body_count_c_005fforEach_005f0) throws Throwable {

        PageContext pageContext = _jspx_page_context;

        JspWriter out = _jspx_page_context.getOut();

        org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f1 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);

        _jspx_th_c_005fwhen_005f1.setPageContext(_jspx_page_context);

        _jspx_th_c_005fwhen_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f1);

        _jspx_th_c_005fwhen_005f1.setTest(((java.lang.Boolean) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${user.active}", java.lang.Boolean.class, (PageContext) _jspx_page_context, null, false)).booleanValue());

        int _jspx_eval_c_005fwhen_005f1 = _jspx_th_c_005fwhen_005f1.doStartTag();

        if (_jspx_eval_c_005fwhen_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {

            do {

                out.write("\r\n");

                out.write("\t\t\t\t\t\t\t\t<div class=\"controlButton\"><a\r\n");

                out.write("\t\t\t\t\t\t\t\t\thref=\"index.html?c=user&task=deactivate&login=");

                out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${user.login}", java.lang.String.class, (PageContext) _jspx_page_context, null, false));

                out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${redirect}", java.lang.String.class, (PageContext) _jspx_page_context, null, false));

                out.write("\"\r\n");

                out.write("\t\t\t\t\t\t\t\t\tid=\"activate\" class=\"button_small\"> <span>Deaktivieren</span>\r\n");

                out.write("\t\t\t\t\t\t\t\t</a></div>\r\n");

                out.write("\t\t\t\t\t\t\t");

                int evalDoAfterBody = _jspx_th_c_005fwhen_005f1.doAfterBody();

                if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN) break;

            } while (true);

        }

        if (_jspx_th_c_005fwhen_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {

            _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f1);

            return true;

        }

        _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f1);

        return false;

    }
