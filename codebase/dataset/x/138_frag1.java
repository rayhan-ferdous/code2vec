        public void run() {

            try {

                while (true) {

                    if (photosQueue.photosToLoad.size() == 0) {

                        synchronized (photosQueue.photosToLoad) {

                            photosQueue.photosToLoad.wait();

                        }

                    }

                    if (photosQueue.photosToLoad.size() != 0) {

                        PhotoToLoad photoToLoad;

                        synchronized (photosQueue.photosToLoad) {

                            photoToLoad = photosQueue.photosToLoad.pop();

                        }

                        Bitmap bmp = getBitmap(photoToLoad.url);

                        cache.put(photoToLoad.url, bmp);

                        if (photoToLoad.imageView != null) {

                            if (((String) photoToLoad.imageView.getTag()).equals(photoToLoad.url)) {

                                BitmapDisplayer bd = new BitmapDisplayer(bmp, photoToLoad.imageView);

                                Activity a = (Activity) photoToLoad.imageView.getContext();

                                a.runOnUiThread(bd);

                            }

                        }

                    }

                    if (Thread.interrupted()) {

                        break;

                    }

                }

            } catch (InterruptedException e) {

            }

        }
