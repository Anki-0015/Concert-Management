import java.util.Scanner;

public class SongPlaylist {
    
    // Node class for the doubly linked list
    static class Song {
        private String title;
        private String artist;
        private int duration; // in seconds
        private Song previous;
        private Song next;
        
        public Song(String title, String artist, int duration) {
            this.title = title;
            this.artist = artist;
            this.duration = duration;
            this.previous = null;
            this.next = null;
        }
        
        public String getTitle() {
            return title;
        }
        
        public String getArtist() {
            return artist;
        }
        
        public String getDurationFormatted() {
            int minutes = duration / 60;
            int seconds = duration % 60;
            return String.format("%d:%02d", minutes, seconds);
        }
        
        @Override
        public String toString() {
            return title + " by " + artist + " (" + getDurationFormatted() + ")";
        }
    }
    
    // Playlist class using doubly linked list
    static class Playlist {
        private Song head;
        private Song tail;
        private Song currentSong;
        private int size;
        
        public Playlist() {
            this.head = null;
            this.tail = null;
            this.currentSong = null;
            this.size = 0;
        }
        
        // Add a song to the end of the playlist
        public void addSong(String title, String artist, int duration) {
            Song newSong = new Song(title, artist, duration);
            
            if (head == null) {
                head = newSong;
                tail = newSong;
                currentSong = newSong; // Set as current if it's the first song
            } else {
                tail.next = newSong;
                newSong.previous = tail;
                tail = newSong;
            }
            
            size++;
        }
        
        // Play the next song in the playlist
        public Song nextSong() {
            if (currentSong != null && currentSong.next != null) {
                currentSong = currentSong.next;
                return currentSong;
            } else {
                System.out.println("End of playlist reached.");
                return currentSong;
            }
        }
        
        // Play the previous song in the playlist
        public Song previousSong() {
            if (currentSong != null && currentSong.previous != null) {
                currentSong = currentSong.previous;
                return currentSong;
            } else {
                System.out.println("Beginning of playlist reached.");
                return currentSong;
            }
        }
        
        // Jump to a specific song by index (1-based for user convenience)
        public Song jumpToSong(int index) {
            if (index < 1 || index > size) {
                System.out.println("Invalid song number. Please choose between 1 and " + size);
                return currentSong;
            }
            
            Song temp = head;
            for (int i = 1; i < index; i++) {
                temp = temp.next;
            }
            
            currentSong = temp;
            return currentSong;
        }
        
        // Jump to a specific song by name
        public Song jumpToSongByName(String songName) {
            Song temp = head;
            boolean found = false;
            
            while (temp != null) {
                if (temp.getTitle().equalsIgnoreCase(songName)) {
                    currentSong = temp;
                    found = true;
                    break;
                }
                temp = temp.next;
            }
            
            if (!found) {
                System.out.println("Song not found: " + songName);
            }
            
            return currentSong;
        }
        
        // Display all songs in the playlist with their indices
        public void displayPlaylist() {
            Song temp = head;
            int index = 1;
            
            System.out.println("\n===== PLAYLIST =====");
            while (temp != null) {
                System.out.print(index + ". " + temp);
                if (temp == currentSong) {
                    System.out.print(" ◄ NOW PLAYING");
                }
                System.out.println();
                temp = temp.next;
                index++;
            }
            System.out.println("==================\n");
        }
        
        // Get current song
        public Song getCurrentSong() {
            return currentSong;
        }
        
        // Get playlist size
        public int getSize() {
            return size;
        }
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Playlist playlist = new Playlist();
        
        // Adding 8 songs by The Weeknd to the playlist
        playlist.addSong("Blinding Lights", "The Weeknd", 200);
        playlist.addSong("Starboy", "The Weeknd", 230);
        playlist.addSong("The Hills", "The Weeknd", 242);
        playlist.addSong("Save Your Tears", "The Weeknd", 215);
        playlist.addSong("Can't Feel My Face", "The Weeknd", 213);
        playlist.addSong("After Hours", "The Weeknd", 361);
        playlist.addSong("Heartless", "The Weeknd", 198);
        playlist.addSong("In Your Eyes", "The Weeknd", 216);
        
        boolean running = true;
        
        System.out.println("Welcome to The Weeknd Playlist!");
        playlist.displayPlaylist();
        
        while (running) {
            System.out.println("Now playing: " + playlist.getCurrentSong());
            System.out.println("Enter a song name to change songs, press ENTER to play next song, or type 'exit' to quit:");
            String input = scanner.nextLine().trim();
            
            if (input.isEmpty()) {
                Song next = playlist.nextSong();
                if (next == playlist.getCurrentSong()) {
                    // If we're at the end of the playlist and can't go next, start from beginning
                    playlist.jumpToSong(1);
                    System.out.println("Reached the end of playlist. Starting from beginning.");
                }
            } else if (input.equalsIgnoreCase("exit")) {
                running = false;
                System.out.println("Thank you for using the Music Player. Goodbye!");
            } else {
                // Attempt to find and play the song by name
                Song current = playlist.getCurrentSong();
                playlist.jumpToSongByName(input);
                if (current == playlist.getCurrentSong()) {
                    System.out.println("Song not found: " + input);
                }
            }
            
            // Add a short delay between songs (simulating playback)
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        scanner.close();
    }
}