package org.harvest.crawler.bean;

public class UrlInfo {

	private Integer pgm_id;
	private String website_id;
	private String former_website;
	private String pgm_play_url;
	private Integer quality;
	private Integer show_flag;
	private Integer grp_id;
	private String url_md5;
	private String _id;
	private Object episode;
	private String content_id;
	private String pgm_content_id;

	public Integer getPgm_id() {
		return pgm_id;
	}

	public void setPgm_id(Integer pgm_id) {
		this.pgm_id = pgm_id;
	}

	public String getWebsite_id() {
		return website_id;
	}

	public void setWebsite_id(String website_id) {
		this.website_id = website_id;
	}

	public String getFormer_website() {
		return former_website;
	}

	public void setFormer_website(String former_website) {
		this.former_website = former_website;
	}

	public String getPgm_play_url() {
		return pgm_play_url;
	}

	public void setPgm_play_url(String pgm_play_url) {
		this.pgm_play_url = pgm_play_url;
	}

	public Integer getQuality() {
		return quality;
	}

	public void setQuality(Integer quality) {
		this.quality = quality;
	}

	public Integer getShow_flag() {
		return show_flag;
	}

	public void setShow_flag(Integer show_flag) {
		this.show_flag = show_flag;
	}

	public Integer getGrp_id() {
		return grp_id;
	}

	public void setGrp_id(Integer grp_id) {
		this.grp_id = grp_id;
	}

	public String getUrl_md5() {
		return url_md5;
	}

	public void setUrl_md5(String url_md5) {
		this.url_md5 = url_md5;
	}

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public Object getEpisode() {
		return episode;
	}

	public void setEpisode(Object episode) {
		this.episode = episode;
	}

	public String getContent_id() {
		return content_id;
	}

	public void setContent_id(String content_id) {
		this.content_id = content_id;
	}

	public String getPgm_content_id() {
		return pgm_content_id;
	}

	public void setPgm_content_id(String pgm_content_id) {
		this.pgm_content_id = pgm_content_id;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null){
			return false;
		}
		if (!(obj instanceof UrlInfo)){
			return false;
		}
		UrlInfo urlInfo = (UrlInfo) obj;
		if (!urlInfo.getWebsite_id().equals(website_id)){
			return false;
		}
		return true;
	}
	
	public int hashCode() {
		assert false : "hashCode not designed";
		return 42; // any arbitrary constant will do
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("UrlInfo [pgm_id=").append(pgm_id).append(", website_id=").append(website_id).append(", pgm_play_url=").append(pgm_play_url
				).append(", quality=").append(quality).append(", show_flag=").append(show_flag).append(", grp_id=").append(grp_id).append(", url_md5=").append(url_md5
				).append(", _id=").append(_id).append(", episode=").append(episode).append(", content_id=").append(content_id).append(pgm_content_id +"]");
		return sb.toString();
	}

}
