        public void run() {

            closeLog();

            if (setLimits(timeLimit, outputLimit, 6, uid, gid) < 0) {

                halt(JudgeReply.JUDGE_INTERNAL_ERROR);

            }

            mainMethod.setAccessible(true);

            System.setSecurityManager(sandboxSecurityManager);

            SandboxSecurityManager.targetThread = this;

            try {

                mainMethod.invoke(null, targetArguments);

                System.out.close();

                SandboxSecurityManager.targetThread = null;

                updateConsumptions();

            } catch (InvocationTargetException e) {

                SandboxSecurityManager.targetThread = null;

                Throwable targetException = e.getTargetException();

                logError(printError(targetException));

                if (targetException instanceof OutOfMemoryError) {

                    memoryConsumption = memoryLimit + 1;

                    halt(JudgeReply.MEMORY_LIMIT_EXCEEDED);

                } else {

                    halt(JudgeReply.RUNTIME_ERROR);

                }

            } catch (Exception e) {

                SandboxSecurityManager.targetThread = null;

                logError(printError(e));

                halt(JudgeReply.JUDGE_INTERNAL_ERROR);

            }

        }
