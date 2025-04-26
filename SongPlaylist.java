import java.util.*;

public class SongPlaylist {

    static class Song {
        String title;
        String artist;
        int duration; 
        Song next;
        Song previous;

        public Song(String title, String artist, int duration) {
            this.title = title;
            this.artist = artist;
            this.duration = duration;
        }

        public String getFormattedDuration() {
            return String.format("%d:%02d", duration / 60, duration % 60);
        }

        @Override
        public String toString() {
            return title + " by " + artist + " (" + getFormattedDuration() + ")";
        }
    }

    static class Playlist {
        private Song head;
        private Song tail;
        private Song current;

        public void addSong(String title, String artist, int duration) {
            Song newSong = new Song(title, artist, duration);
            if (head == null) {
                head = tail = newSong;
            } else {
                tail.next = newSong;
                newSong.previous = tail;
                tail = newSong;
            }
        }

        public void start() {
            current = head;
        }

        public void next() {
            if (current != null && current.next != null) {
                current = current.next;
            } else {
                System.out.println("\n🎤 Concert finished. No more songs!\n");
                current = null;
            }
        }

        public void previous() {
            if (current != null && current.previous != null) {
                current = current.previous;
            } else {
                System.out.println("\n🎤 At the beginning of the concert.\n");
            }
        }

        public void jumpToSong(String songName) {
            Song temp = head;
            while (temp != null) {
                if (temp.title.equalsIgnoreCase(songName)) {
                    current = temp;
                    return;
                }
                temp = temp.next;
            }
            System.out.println("🎵 Song not found: " + songName);
        }

        public Song getCurrentSong() {
            return current;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Playlist playlist = new Playlist();

        playlist.addSong("SICKO MODE", "Travis Scott", 312);
        playlist.addSong("goosebumps", "Travis Scott", 245);
        playlist.addSong("STARGAZING", "Travis Scott", 270);
        playlist.addSong("HIGHEST IN THE ROOM", "Travis Scott", 181);
        playlist.addSong("Antidote", "Travis Scott", 244);
        playlist.addSong("BUTTERFLY EFFECT", "Travis Scott", 210);
        playlist.addSong("Mafia", "Travis Scott", 153);
        playlist.addSong("Franchise", "Travis Scott", 190);

        playlist.start();

        System.out.println("🎤 Welcome to Travis Scott's Concert Playlist!");

        while (playlist.getCurrentSong() != null) {
            System.out.println("\nNow playing: " + playlist.getCurrentSong());
            System.out.println("Press ENTER to play next, type 'back' to go to previous song, or type a song name to jump:");

            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                playlist.next();
            } else if (input.equalsIgnoreCase("back")) {
                playlist.previous();
            } else {
                playlist.jumpToSong(input);
            }

            try {
                Thread.sleep(1500); 
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("\n🎶 The concert has ended! Thanks for raging with Travis Scott!");
        scanner.close();
    }
}
