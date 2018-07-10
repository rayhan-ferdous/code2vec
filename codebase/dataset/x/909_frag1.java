        public void update() {

            try {

                if (zfile != null) {

                    zfile.close();

                }

                zfile = new ZipFile(this);

            } catch (Exception tryCloseX) {

            }

            super.update();

        }
