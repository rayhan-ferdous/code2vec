            fos.write(baf.toByteArray());

            fos.close();

            Log.d("ImageManager", "download ready in" + ((System.currentTimeMillis() - startTime) / 1000) + " sec");

            return "";

        } catch (IOException e) {
