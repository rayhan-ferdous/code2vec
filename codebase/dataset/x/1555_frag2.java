                @Override

                public IProblemReporter get(Object key) {

                    IProblemReporter problemReporter = super.get(key);

                    if (problemReporter == null) {

                        IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();

                        IPath path = (IPath) key;

                        IResource resource = root.getFile(path);

                        problemReporter = new BuildProblemReporter(problemFactory, resource);

                        put(path, problemReporter);

                    }

                    return problemReporter;

                }
