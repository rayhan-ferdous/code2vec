            Collections.reverse(imageList);

            String fullDNAPath = PathUtils.GetFullDNAPath(dataCollectionId);

            DNAContentPresent = (new File(fullDNAPath + Constants.DNA_FILES_INDEX_FILE)).exists();

            String fullDNARankingPath = PathUtils.GetFullDNARankingPath(dataCollectionId);

            DNARankingProjectFilePresent = (new File(fullDNARankingPath + rankingProjectFileName).exists());
