    public void writeExternal(ObjectOutput out) throws IOException {

        out.writeObject(writer);

        out.writeObject(logDirectory);

        out.writeObject(logCurrent);

        out.writeObject(logOld);

        out.writeObject(logIndexFile);

        out.writeObject(summary);

        out.writeObject(lastTestFileName);

        out.writeObject(lastTestStatus);

        out.writeObject(lastTestClassName);

        out.writeObject(startTestTime);

        out.writeObject(reportDir);

        out.writeObject(isTemp);

        out.writeObject(isZipLogDisable);

    }
