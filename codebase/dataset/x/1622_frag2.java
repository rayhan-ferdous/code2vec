    public void startImport(I_CmsReport report) throws Exception {

        try {

            m_report = report;

            m_report.println(Messages.get().container(Messages.RPT_HTML_IMPORT_BEGIN_0), I_CmsReport.FORMAT_HEADLINE);

            boolean isStream = !CmsStringUtil.isEmptyOrWhitespaceOnly(m_httpDir);

            File streamFolder = null;

            if (isStream) {

                streamFolder = unzipStream();

                m_inputDir = streamFolder.getAbsolutePath();

            }

            buildIndex(m_inputDir);

            buildParentPath();

            copyHtmlFiles(m_inputDir);

            copyOtherFiles(m_inputDir);

            createExternalLinks();

            if (isStream && streamFolder != null) {

                m_report.println(Messages.get().container(Messages.RPT_HTML_DELETE_0), I_CmsReport.FORMAT_NOTE);

                CmsFileUtil.purgeDirectory(streamFolder);

                File file = new File(m_httpDir);

                if (file.exists() && file.canWrite()) {

                    file.delete();

                }

            }

            m_report.println(Messages.get().container(Messages.RPT_HTML_IMPORT_END_0), I_CmsReport.FORMAT_HEADLINE);

        } catch (Exception e) {

            e.printStackTrace();

        }

    }
