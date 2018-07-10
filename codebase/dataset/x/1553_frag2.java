    public void showTray() {

        SystemTray tray = SystemTray.getSystemTray();

        if (SystemTray.isSupported()) {

            Image image = createImage(imagePath);

            trayIcon = createTrayIcon(image, menu());

            try {

                tray.add(trayIcon);

            } catch (AWTException e) {

                JOptionPane.showMessageDialog(null, properties.getProperty("icon.not.added"));

            }

        }

    }
