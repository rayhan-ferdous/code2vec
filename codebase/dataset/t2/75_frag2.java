        DeleteFileUtils.deleteFile(this.deepDir);

        this.result = this.deepDir.exists();

        assertFalse("", this.result);

        this.result = testFile5.exists();

        assertFalse("", this.result);

        this.result = testFile4.exists();

        assertTrue("", this.result);

        this.result = this.testDir.exists();

        assertTrue("", this.result);

        DeleteFileUtils.deleteFile(this.testDir);

        this.result = testFile4.exists();

        assertFalse("", this.result);

        this.result = this.testDir.exists();

        assertFalse("", this.result);

    }
