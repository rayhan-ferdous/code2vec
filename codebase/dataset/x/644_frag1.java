    void loadImage() {

        this.jLabelImage.setIcon(new ImageIcon(imageList.get(imageIndex)));

        if (boxes != null) {

            boxes.deselectAll();

        }

        this.jLabelImage.repaint();

        this.jLabelPageNbr.setText(String.format("Page: %d of %d", imageIndex + 1, imageList.size()));

        setButton();

        tableSelectAction = true;

        resetReadout();

        tableSelectAction = false;

    }
