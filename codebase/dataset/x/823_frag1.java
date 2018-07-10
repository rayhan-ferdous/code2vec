            byte[] buf = new byte[5000];

            int n = 0;

            while ((n = bis.read(buf)) != -1) baos.write(buf, 0, n);

            bis.close();

        } catch (IOException e) {
