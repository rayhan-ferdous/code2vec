            String imageUrl = o.getPhotoUrl();

            if ((imageUrl != null) && !imageUrl.equals("")) {

                try {

                    if (!imageUrl.startsWith("http")) imageUrl = Configuration.getDomain() + imageUrl;

                    URL url = new URL(imageUrl);

                    URLConnection conn = url.openConnection();

                    conn.connect();

                    BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());

                    Bitmap bm = BitmapFactory.decodeStream(bis);

                    bis.close();

                    if (bm != null) imageView.setImageBitmap(bm);
