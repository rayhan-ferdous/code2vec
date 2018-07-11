    private boolean _jspx_meth_display_005ftable_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_html_005fform_005f0, PageContext _jspx_page_context) throws Throwable {

        PageContext pageContext = _jspx_page_context;

        JspWriter out = _jspx_page_context.getOut();

        org.displaytag.tags.el.ELTableTag _jspx_th_display_005ftable_005f0 = (org.displaytag.tags.el.ELTableTag) _005fjspx_005ftagPool_005fdisplay_005ftable_005fstyle_005fsort_005fsize_005frequestURI_005fpagesize_005fname_005fid_005fdecorator_005fclass_005fcellspacing_005fcellpadding.get(org.displaytag.tags.el.ELTableTag.class);

        _jspx_th_display_005ftable_005f0.setPageContext(_jspx_page_context);

        _jspx_th_display_005ftable_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_005fform_005f0);

        _jspx_th_display_005ftable_005f0.setClass("table");

        _jspx_th_display_005ftable_005f0.setStyle("width:100%;");

        _jspx_th_display_005ftable_005f0.setCellpadding("1");

        _jspx_th_display_005ftable_005f0.setCellspacing("1");

        _jspx_th_display_005ftable_005f0.setName("colecao");

        _jspx_th_display_005ftable_005f0.setSort("list");

        _jspx_th_display_005ftable_005f0.setSize("10");

        _jspx_th_display_005ftable_005f0.setRequestURI("cadastroPaciente.do");

        _jspx_th_display_005ftable_005f0.setPagesize("20");

        _jspx_th_display_005ftable_005f0.setDecorator("com.odontosis.view.decorator.CadastroPacienteDecorator");

        _jspx_th_display_005ftable_005f0.setUid("idDisplayTable");

        int _jspx_eval_display_005ftable_005f0 = _jspx_th_display_005ftable_005f0.doStartTag();

        if (_jspx_eval_display_005ftable_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {

            if (_jspx_eval_display_005ftable_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {

                out = _jspx_page_context.pushBody();

                _jspx_th_display_005ftable_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);

                _jspx_th_display_005ftable_005f0.doInitBody();

            }

            do {

                out.write("  \r\n");

                out.write("\t        \r\n");

                out.write("\t    \t\t\t\t\t\t\t\t\t\r\n");

                out.write("\t    ");

                if (_jspx_meth_display_005fcolumn_005f0(_jspx_th_display_005ftable_005f0, _jspx_page_context)) return true;

                out.write("\r\n");

                out.write("\t\t");

                if (_jspx_meth_display_005fcolumn_005f1(_jspx_th_display_005ftable_005f0, _jspx_page_context)) return true;

                out.write("\r\n");

                out.write("\t\t");

                if (_jspx_meth_display_005fcolumn_005f2(_jspx_th_display_005ftable_005f0, _jspx_page_context)) return true;

                out.write("\r\n");

                out.write("\t\t");

                if (_jspx_meth_display_005fcolumn_005f3(_jspx_th_display_005ftable_005f0, _jspx_page_context)) return true;

                out.write("\t\r\n");

                out.write("\t\t");

                if (_jspx_meth_display_005fcolumn_005f4(_jspx_th_display_005ftable_005f0, _jspx_page_context)) return true;

                out.write("\r\n");

                out.write("\t\t");

                if (_jspx_meth_display_005fcolumn_005f5(_jspx_th_display_005ftable_005f0, _jspx_page_context)) return true;

                out.write("\r\n");

                out.write("\t\t");

                if (_jspx_meth_display_005fcolumn_005f6(_jspx_th_display_005ftable_005f0, _jspx_page_context)) return true;

                out.write("\r\n");

                out.write("\t\t\r\n");

                out.write("\t    \r\n");

                out.write("\t");

                int evalDoAfterBody = _jspx_th_display_005ftable_005f0.doAfterBody();

                if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN) break;

            } while (true);

            if (_jspx_eval_display_005ftable_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {

                out = _jspx_page_context.popBody();

            }

        }

        if (_jspx_th_display_005ftable_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {

            _005fjspx_005ftagPool_005fdisplay_005ftable_005fstyle_005fsort_005fsize_005frequestURI_005fpagesize_005fname_005fid_005fdecorator_005fclass_005fcellspacing_005fcellpadding.reuse(_jspx_th_display_005ftable_005f0);

            return true;

        }

        _005fjspx_005ftagPool_005fdisplay_005ftable_005fstyle_005fsort_005fsize_005frequestURI_005fpagesize_005fname_005fid_005fdecorator_005fclass_005fcellspacing_005fcellpadding.reuse(_jspx_th_display_005ftable_005f0);

        return false;

    }
