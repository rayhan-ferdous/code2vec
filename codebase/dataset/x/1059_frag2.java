    public BaseCmsAcquisition(java.lang.Integer id, com.jeecms.cms.entity.main.CmsUser user, com.jeecms.cms.entity.main.ContentType type, com.jeecms.cms.entity.main.CmsSite site, com.jeecms.cms.entity.main.Channel channel, java.lang.String name, java.lang.Integer status, java.lang.Integer currNum, java.lang.Integer currItem, java.lang.Integer totalItem, java.lang.Integer pauseTime, java.lang.String pageEncoding, java.lang.Integer queue) {

        this.setId(id);

        this.setUser(user);

        this.setType(type);

        this.setSite(site);

        this.setChannel(channel);

        this.setName(name);

        this.setStatus(status);

        this.setCurrNum(currNum);

        this.setCurrItem(currItem);

        this.setTotalItem(totalItem);

        this.setPauseTime(pauseTime);

        this.setPageEncoding(pageEncoding);

        this.setQueue(queue);

        initialize();

    }
