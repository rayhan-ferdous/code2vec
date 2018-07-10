    public void actualizarCarpeta(Component componente) {

        CarpetaTematica carpeta = (CarpetaTematica) componente.getAttribute("carpeta");

        carpeta.setNombre(((Textbox) componente.getRoot().getFellow("txt_nombre_recurso")).getText());

        SessionUtil.getFacadeOperacionesRecursosCassiaCore().actualizarCarpeta(carpeta);

        try {

            ZKUtil.actualizarArbolCarpetas(componente);

            ((Window) componente.getRoot()).setVisible(false);

            Messagebox.show(Labels.getLabel("msg_carpeta_actualizada"), Labels.getLabel("title_editar_recurso"), Messagebox.OK, Messagebox.QUESTION);

        } catch (InterruptedException e) {

        }

    }
