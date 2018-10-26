package razerdp.friendcircle.app.mvp.model.event;


public class DownloadEvent {
    private String url;
    private int state;
    private int progress;

    public DownloadEvent(String url, int state, int progress) {
        this.url = url;
        this.state = state;
        this.progress = progress;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
}
