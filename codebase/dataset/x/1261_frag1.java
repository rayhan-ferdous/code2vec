            public void onError(ErrorEvent event) {

                String url = posterImage.getUrl();

                GWT.log("Could not load image: " + url);

                if (res instanceof GWTMediaFolder) {

                    posterImage.setUrl("images/128x128/folder_video2.png");

                } else {

                    posterImage.setUrl("images/128x128/video2.png");

                }

                posterImage.setTitle(url);

            }
