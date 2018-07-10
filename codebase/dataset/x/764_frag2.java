                if (rc <= 0) break; else bout.write(buf, 0, rc);

            }

            return bout.toString();

        } catch (IOException e) {

            throw new ReCaptchaException("Cannot load URL: " + e.getMessage(), e);

        } finally {

            try {

                if (in != null) in.close();
