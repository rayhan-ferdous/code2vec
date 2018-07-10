            public Object construct() {

                finished = false;

                ActualTask conn = new ActualTask();

                for (int i = 0; i < files.size(); i++) {

                    currentFile = (InputFile) (files.get(i));

                    if (currentFile == null) continue;

                    try {

                        parameters.put("targetDirectory", parentWorkflow.getSessionId() + "/" + currentFile.getDirectory());

                        parameters.put("targetFile", currentFile.getName());

                        ret = conn.postDownloadRequest(currentFile.getLocalFile());

                    } catch (Exception e) {

                        error = true;

                        finished = true;

                    }

                    proccessProgressPercent = java.lang.Math.round((i + 1) * 100 / files.size());

                }

                finished = true;

                return ret;

            }
