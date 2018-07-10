        Intent intent = new Intent(Intent.ACTION_VIEW);

        intent.setData(Uri.parse("market://details?id=com.hamdyghanem.holyquranimg" + AC.CurrentImageType));

        startActivity(intent);

    }



    public void downloadTafser(View view) {

        downloadTafserNow();

    }



    public void downloadDatabase(View view) {

        downloadDatabaseNow();
