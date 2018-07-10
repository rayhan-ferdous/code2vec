            for (int j = 0; j < w; j++) {

                pixel[i][j] = pixels[j][i];

            }

        }

        return pixel;

    }



    /**

	 * 剪切指定图像

	 * 

	 * @param image

	 * @param objectWidth

	 * @param objectHeight

	 * @param x1

	 * @param y1

	 * @param x2

	 * @param y2

	 * @return

	 */

    public static BufferedImage drawClipImage(final Image image, int objectWidth, int objectHeight, int x1, int y1, int x2, int y2) {

        BufferedImage buffer = GraphicsUtils.createImage(objectWidth, objectHeight, true);

        Graphics g = buffer.getGraphics();

        Graphics2D graphics2D = (Graphics2D) g;
