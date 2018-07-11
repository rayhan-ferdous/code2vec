        public void dragSetData(DragSourceEvent event) {

            try {

                if (TextTransfer.getInstance().isSupportedType(event.dataType)) {

                    StringWriter out = new StringWriter();

                    PrintWriter pw = new PrintWriter(out);

                    for (Object it : ((IStructuredSelection) _viewer.getSelection()).toArray()) {

                        pw.println(((IDocumentItem) it).getResource().getContent());

                    }

                    event.data = out.toString();

                } else if (FileTransfer.getInstance().isSupportedType(event.dataType)) {

                    if (_tmpFiles == null) {

                        _tmpFiles = createTempFiles();

                    }

                    event.data = _tmpFiles;

                }

            } catch (Exception e) {

                File sessionDir = _tmpSessionDir;

                reset();

                deleteRecursive(sessionDir);

                throw new RuntimeException(e);

            }

        }
