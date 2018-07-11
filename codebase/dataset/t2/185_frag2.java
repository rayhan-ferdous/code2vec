        try {

            TInfoLevel_QueryMap query = new TInfoLevel_QueryMap();

            List ls = query.findType(vo.getInfo_type());

            request.setAttribute("infoType", ls);

        } catch (HibernateException e) {

            logger.error("doEdit(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)", e);

            e.printStackTrace();

        }

        if (null != act && "edit1".equalsIgnoreCase(act)) {
