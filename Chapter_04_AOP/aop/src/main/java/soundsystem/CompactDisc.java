package soundsystem;

import java.util.List;

/**
 * Created by Shubo on 7/30/2015.
 */
public class CompactDisc {

    private String title;
    private String artist;
    private List<String> tracks;

    public CompactDisc() {

    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public List<String> getTracks() {
        return tracks;
    }

    public void setTracks(List<String> tracks) {
        this.tracks = tracks;
    }


    public void playTrack(int i) {
        System.out.println(tracks.get(i));
    }
}
