    private void compareFile(File fileA, File fileB, boolean skipIdentical, String basePath) {

        String filename = null;

        if (fileB != null) filename = fileB.getPath(); else if (fileA != null) filename = fileA.getPath();

        if (filename != null && basePath != null && filename.startsWith(basePath)) {

            filename = filename.substring(basePath.length());

            if (filename.startsWith(File.separator)) filename = filename.substring(1);

        }

        updateProgress(filename);

        if (!isFile(fileA)) fileA = null;

        if (!isFile(fileB)) fileB = null;

        examineFiles(fileA, fileB);

        if (skipIdentical && filesAreIdentical) return;

        StringBuffer fileTable;

        String label, htmlName;

        if (fileA == null) {

            fileTable = addedTable;

            label = "Added";

        } else if (fileB == null) {

            fileTable = deletedTable;

            label = "Deleted";

        } else {

            fileTable = modifiedTable;

            label = "Modified";

        }

        htmlName = HTMLUtils.escapeEntities(filename);

        if (filesAreBinary) {

            fileTable.append("<tr><td nowrap>").append(htmlName);

            if (fileTable == modifiedTable) fileTable.append("</td><td></td><td></td><td></td><td>");

            fileTable.append("</td><td></td><td>Binary</td></tr>\n");

            return;

        }

        String contentsA = getContents(fileA);

        String contentsB = getContents(fileB);

        PSPDiff diff = new PSPDiff(webServer, contentsA, contentsB, filename, null);

        if (skipIdentical && (diff.getDeleted() + diff.getAdded() + diff.getModified() == 0)) return;

        base += diff.getBase();

        deleted += diff.getDeleted();

        added += diff.getAdded();

        modified += diff.getModified();

        total += diff.getTotal();

        fileTable.append("<tr><td nowrap><a href='#file").append(counter).append("'>").append(htmlName).append("</a>");

        if (fileTable == deletedTable) fileTable.append("</td><td>").append(diff.getBase()); else if (fileTable == modifiedTable) fileTable.append("</td><td>").append(diff.getBase()).append("</td><td>").append(diff.getDeleted()).append("</td><td>").append(diff.getModified()).append("</td><td>").append(diff.getAdded());

        fileTable.append("</td><td>").append(diff.getTotal()).append("</td><td>").append(AbstractLanguageFilter.getFilterName(diff.getFilter())).append("</td></tr>\n");

        redlinesOut.print("<hr><DIV onMouseOver=\"window.defaultStatus='");

        redlinesOut.print(EscapeString.escape(htmlName, '\\', "\""));

        redlinesOut.print("'\"><h1>");

        redlinesOut.print(label);

        redlinesOut.print(": <a name='file");

        redlinesOut.print(counter++);

        redlinesOut.print("'>");

        redlinesOut.print(htmlName);

        redlinesOut.print("</a></h1>");

        diff.displayHTMLRedlines(redlinesOut);

        redlinesOut.print("</DIV>\n\n\n");

        diff.dispose();

    }
