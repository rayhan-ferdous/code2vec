    public BaseContent(java.lang.Integer id, com.jeecms.cms.entity.main.CmsSite site, java.util.Date sortDate, java.lang.Byte topLevel, java.lang.Boolean hasTitleImg, java.lang.Boolean recommend, java.lang.Byte status, java.lang.Integer viewsDay, java.lang.Short commentsDay, java.lang.Short downloadsDay, java.lang.Short upsDay) {

        this.setId(id);

        this.setSite(site);

        this.setSortDate(sortDate);

        this.setTopLevel(topLevel);

        this.setHasTitleImg(hasTitleImg);

        this.setRecommend(recommend);

        this.setStatus(status);

        this.setViewsDay(viewsDay);

        this.setCommentsDay(commentsDay);

        this.setDownloadsDay(downloadsDay);

        this.setUpsDay(upsDay);

        initialize();

    }
