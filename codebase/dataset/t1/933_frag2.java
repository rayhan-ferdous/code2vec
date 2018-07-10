    }



    /**

	 * Sorts files lexicographically by name.

	 * 

	 * @param files the array of Files to be sorted

	 */

    static void sortFiles(File[] files) {

        sortBlock(files, 0, files.length - 1, new File[files.length]);

    }



    private static void sortBlock(File[] files, int start, int end, File[] mergeTemp) {

        final int length = end - start + 1;

        if (length < 8) {

            for (int i = end; i > start; --i) {

                for (int j = end; j > start; --j) {

                    if (compareFiles(files[j - 1], files[j]) > 0) {

                        final File temp = files[j];

                        files[j] = files[j - 1];

                        files[j - 1] = temp;

                    }

                }

            }

            return;

        }

        final int mid = (start + end) / 2;

        sortBlock(files, start, mid, mergeTemp);

        sortBlock(files, mid + 1, end, mergeTemp);

        int x = start;

        int y = mid + 1;

        for (int i = 0; i < length; ++i) {

            if ((x > mid) || ((y <= end) && compareFiles(files[x], files[y]) > 0)) {

                mergeTemp[i] = files[y++];

            } else {

                mergeTemp[i] = files[x++];

            }

        }

        for (int i = 0; i < length; ++i) files[i + start] = mergeTemp[i];

    }



    private static int compareFiles(File a, File b) {

        int compare = a.getName().compareToIgnoreCase(b.getName());

        if (compare == 0) compare = a.getName().compareTo(b.getName());

        return compare;

    }



    /**

	 * Stops the worker and waits for it to terminate.

	 */

    void workerStop() {

        if (workerThread == null) return;

        synchronized (workerLock) {

            workerCancelled = true;

            workerStopped = true;

            workerLock.notifyAll();

        }

        while (workerThread != null) {

            if (!display.readAndDispatch()) display.sleep();

        }

    }



    /**

	 * Notifies the worker that it should update itself with new data.

	 * Cancels any previous operation and begins a new one.

	 * 

	 * @param dir the new base directory for the table, null is ignored

	 * @param force if true causes a refresh even if the data is the same

	 */

    void workerUpdate(File dir, boolean force) {

        if (dir == null) return;

        if ((!force) && (workerNextDir != null) && (workerNextDir.equals(dir))) return;

        synchronized (workerLock) {

            workerNextDir = dir;

            workerStopped = false;

            workerCancelled = true;

            workerLock.notifyAll();

        }

        if (workerThread == null) {

            workerThread = new Thread(workerRunnable);

            workerThread.start();

        }

    }



    /**

	 * Manages the worker's thread

	 */

    private final Runnable workerRunnable = new Runnable() {



        public void run() {

            while (!workerStopped) {

                synchronized (workerLock) {

                    workerCancelled = false;

                    workerStateDir = workerNextDir;

                }

                workerExecute();

                synchronized (workerLock) {

                    try {

                        if ((!workerCancelled) && (workerStateDir == workerNextDir)) workerLock.wait();

                    } catch (InterruptedException e) {

                    }

                }

            }

            workerThread = null;

            display.wake();

        }

    };



    /**

	 * Updates the table's contents

	 */

    private void workerExecute() {

        File[] dirList;

        display.syncExec(new Runnable() {



            public void run() {

                tableContentsOfLabel.setText(FileViewer.getResourceString("details.ContentsOf.text", new Object[] { workerStateDir.getPath() }));

                table.removeAll();

                table.setData(TABLEDATA_DIR, workerStateDir);

            }

        });

        dirList = getDirectoryList(workerStateDir);

        for (int i = 0; (!workerCancelled) && (i < dirList.length); i++) {

            workerAddFileDetails(dirList[i]);

        }

    }



    /**

	 * Adds a file's detail information to the directory list

	 */

    private void workerAddFileDetails(final File file) {

        final String nameString = file.getName();

        final String dateString = dateFormat.format(new Date(file.lastModified()));

        final String sizeString;

        final String typeString;

        final Image iconImage;

        if (file.isDirectory()) {

            typeString = getResourceString("filetype.Folder");

            sizeString = "";

            iconImage = iconCache.stockImages[iconCache.iconClosedFolder];

        } else {

            sizeString = getResourceString("filesize.KB", new Object[] { new Long((file.length() + 512) / 1024) });

            int dot = nameString.lastIndexOf('.');

            if (dot != -1) {

                String extension = nameString.substring(dot);

                Program program = Program.findProgram(extension);

                if (program != null) {

                    typeString = program.getName();

                    iconImage = iconCache.getIconFromProgram(program);

                } else {

                    typeString = getResourceString("filetype.Unknown", new Object[] { extension.toUpperCase() });

                    iconImage = iconCache.stockImages[iconCache.iconFile];

                }

            } else {

                typeString = getResourceString("filetype.None");

                iconImage = iconCache.stockImages[iconCache.iconFile];

            }

        }

        final String[] strings = new String[] { nameString, sizeString, typeString, dateString };

        display.syncExec(new Runnable() {



            public void run() {

                if (shell.isDisposed()) return;

                TableItem tableItem = new TableItem(table, 0);

                tableItem.setText(strings);

                tableItem.setImage(iconImage);

                tableItem.setData(TABLEITEMDATA_FILE, file);

            }

        });

    }



    /**

	 * Instances of this class manage a progress dialog for file operations.

	 */

    class ProgressDialog {



        public static final int COPY = 0;



        public static final int DELETE = 1;



        public static final int MOVE = 2;



        Shell shell;



        Label messageLabel, detailLabel;



        ProgressBar progressBar;



        Button cancelButton;



        boolean isCancelled = false;



        final String operationKeyName[] = { "Copy", "Delete", "Move" };



        /**

		 * Creates a progress dialog but does not open it immediately.

		 * 

		 * @param parent the parent Shell

		 * @param style one of COPY, MOVE

		 */

        public ProgressDialog(Shell parent, int style) {

            shell = new Shell(parent, SWT.BORDER | SWT.TITLE | SWT.APPLICATION_MODAL);

            GridLayout gridLayout = new GridLayout();

            shell.setLayout(gridLayout);

            shell.setText(getResourceString("progressDialog." + operationKeyName[style] + ".title"));

            shell.addShellListener(new ShellAdapter() {



                public void shellClosed(ShellEvent e) {

                    isCancelled = true;

                }

            });

            messageLabel = new Label(shell, SWT.HORIZONTAL);

            messageLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_FILL));

            messageLabel.setText(getResourceString("progressDialog." + operationKeyName[style] + ".description"));

            progressBar = new ProgressBar(shell, SWT.HORIZONTAL | SWT.WRAP);

            progressBar.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_FILL));

            progressBar.setMinimum(0);

            progressBar.setMaximum(0);

            detailLabel = new Label(shell, SWT.HORIZONTAL);

            GridData gridData = new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING);

            gridData.widthHint = 400;

            detailLabel.setLayoutData(gridData);

            cancelButton = new Button(shell, SWT.PUSH);

            cancelButton.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END | GridData.VERTICAL_ALIGN_FILL));

            cancelButton.setText(getResourceString("progressDialog.cancelButton.text"));

            cancelButton.addSelectionListener(new SelectionAdapter() {



                public void widgetSelected(SelectionEvent e) {

                    isCancelled = true;

                    cancelButton.setEnabled(false);

                }

            });

        }



        /**

		 * Sets the detail text to show the filename along with a string

		 * representing the operation being performed on that file.

		 * 

		 * @param file the file to be detailed

		 * @param operation one of COPY, DELETE

		 */

        public void setDetailFile(File file, int operation) {

            detailLabel.setText(getResourceString("progressDialog." + operationKeyName[operation] + ".operation", new Object[] { file }));

        }



        /**

		 * Returns true if the Cancel button was been clicked.

		 * 

		 * @return true if the Cancel button was clicked.

		 */

        public boolean isCancelled() {

            return isCancelled;

        }



        /**

		 * Sets the total number of work units to be performed.

		 * 

		 * @param work the total number of work units

		 */

        public void setTotalWorkUnits(int work) {

            progressBar.setMaximum(work);

        }



        /**

		 * Adds to the total number of work units to be performed.

		 * 

		 * @param work the number of work units to add

		 */

        public void addWorkUnits(int work) {

            setTotalWorkUnits(progressBar.getMaximum() + work);

        }



        /**

		 * Sets the progress of completion of the total work units.

		 * 

		 * @param work the total number of work units completed

		 */

        public void setProgress(int work) {

            progressBar.setSelection(work);

            while (display.readAndDispatch()) {

            }

        }



        /**

		 * Adds to the progress of completion of the total work units.

		 * 

		 * @param work the number of work units completed to add

		 */

        public void addProgress(int work) {

            setProgress(progressBar.getSelection() + work);

        }



        /**

		 * Opens the dialog.

		 */

        public void open() {

            shell.pack();

            final Shell parentShell = (Shell) shell.getParent();

            Rectangle rect = parentShell.getBounds();

            Rectangle bounds = shell.getBounds();

            bounds.x = rect.x + (rect.width - bounds.width) / 2;

            bounds.y = rect.y + (rect.height - bounds.height) / 2;

            shell.setBounds(bounds);

            shell.open();

        }



        /**

		 * Closes the dialog and disposes its resources.

		 */

        public void close() {

            shell.close();

            shell.dispose();

            shell = null;

            messageLabel = null;

            detailLabel = null;

            progressBar = null;

            cancelButton = null;

        }

    }

}
