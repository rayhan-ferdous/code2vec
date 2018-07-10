    private void lockTransaksiForm() {

        resetTransaksiForm();

        namaField.setEnabled(false);

        menuField.setEnabled(false);

        jumlahField.setEnabled(false);

        beratField.setEnabled(false);

        hargaField.setEnabled(false);

        discountField.setEnabled(false);

        cetakInvoiceButton.setEnabled(false);

        cetakChecklistButton.setEnabled(false);

        postingInvoiceButton.setEnabled(false);

        pembayaranButton.setEnabled(false);

        promoCheckBox.setSelected(false);

        promoCheckBox.setEnabled(false);

    }
