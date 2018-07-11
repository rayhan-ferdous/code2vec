        if (artifact.isSnapshot() && PomUtils.getFile(artifact, m_resolver, m_localRepo)) {

            return pluginVersion;

        }

        VersionRange range = getArchetypeVersionRange();

        try {

            getLog().info("Selecting latest archetype release within version range " + range);

            return PomUtils.getReleaseVersion(artifact, m_source, m_remoteRepos, m_localRepo, range);
