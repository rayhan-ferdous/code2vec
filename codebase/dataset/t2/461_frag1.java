    private boolean _jspx_meth_display_005fcolumn_005f3(javax.servlet.jsp.tagext.JspTag _jspx_th_display_005ftable_005f0, PageContext _jspx_page_context) throws Throwable {

        PageContext pageContext = _jspx_page_context;

        JspWriter out = _jspx_page_context.getOut();

        org.displaytag.tags.el.ELColumnTag _jspx_th_display_005fcolumn_005f3 = (org.displaytag.tags.el.ELColumnTag) _005fjspx_005ftagPool_005fdisplay_005fcolumn_005ftitle_005fstyle_005fsortable_005fproperty_005fnobody.get(org.displaytag.tags.el.ELColumnTag.class);

        _jspx_th_display_005fcolumn_005f3.setPageContext(_jspx_page_context);

        _jspx_th_display_005fcolumn_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_display_005ftable_005f0);

        _jspx_th_display_005fcolumn_005f3.setStyle("height:20; width: 52%;   text-align:left;");

        _jspx_th_display_005fcolumn_005f3.setProperty("paciente");

        _jspx_th_display_005fcolumn_005f3.setTitle("<div align='center'>Nome do Paciente</div>");

        _jspx_th_display_005fcolumn_005f3.setSortable("true");

        int _jspx_eval_display_005fcolumn_005f3 = _jspx_th_display_005fcolumn_005f3.doStartTag();

        if (_jspx_th_display_005fcolumn_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {

            _005fjspx_005ftagPool_005fdisplay_005fcolumn_005ftitle_005fstyle_005fsortable_005fproperty_005fnobody.reuse(_jspx_th_display_005fcolumn_005f3);

            return true;

        }

        _005fjspx_005ftagPool_005fdisplay_005fcolumn_005ftitle_005fstyle_005fsortable_005fproperty_005fnobody.reuse(_jspx_th_display_005fcolumn_005f3);

        return false;

    }
