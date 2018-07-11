    public void exportEnded() {

        btnBrowse.setEnabled(true);

        btnClose.setEnabled(true);

        btnExport.setEnabled(true);

        btnScanEncoding.setEnabled(true);

        txtCellSeparator.setEnabled(true);

        txtFileName.setEnabled(true);

        txtRowSeparator.setEnabled(true);

        chkCellSeparator.setEnabled(true);

        chkExportToClipboard.setEnabled(true);

        chkExportToFile.setEnabled(true);

        chkIncludeHeaders.setEnabled(true);

        chkNewLineForEachCell.setEnabled(true);

        chkNewLineForEachRow.setEnabled(true);

        chkRowSeparator.setEnabled(true);

        cmbEncoding.setEnabled(true);

        btnCancelCurrentJob.setEnabled(false);

        if (chkCloseWhenFinished.isSelected()) {

            btnCloseActionPerformed(null);

        }

    }
