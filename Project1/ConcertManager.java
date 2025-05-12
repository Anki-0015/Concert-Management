package Project1;

import java.text.ParseException;
import java.text.SimpleDateFormat;


import java.util.*;

// Concert class
class Concert implements Comparable<Concert> {
    int concertId;
    private String title;
    private String date;
    private Venue venue;
    private ArrayList<Performer> performers;

    // Constructor to initialize a Concert with its ID, title, date, and venue
    public Concert(int concertId, String title, String date, Venue venue) {
        if (!isValidDate(date)) {
            throw new IllegalArgumentException("Invalid date format. Expected format: yyyy-MM-dd");
        }
        this.concertId = concertId;
        this.title = title;
        this.date = date;
        this.venue = venue;
        this.performers = new ArrayList<>();
    }

    //checking if format of date is correct
    private boolean isValidDate(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false); // strict parsing
        try {
            sdf.parse(dateStr);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    // Adds a performer to the list of performers for this concert
    public void addPerformer(Performer performer) {
        performers.add(performer);
    }

    // Returns the concert ID
    public int getConcertId() {
        return concertId;
    }

    // Returns the concert title
    public String getTitle() {
        return title;
    }


    // Displays concert details including venue and performers
    public void displayConcert() {
        System.out.println("Concert ID: " + concertId + ", Title: " + title + ", Date: " + date);
        System.out.println("Venue: " + venue.getName() + ", Location: " + venue.getLocation());
        System.out.print("Performers: ");
        for (Performer p : performers) {
            System.out.print(p.getName() + " ");
        }
        System.out.println("\n-------------------------");
    }

    // Compares two concerts by their ID (for sorting purposes)
    @Override
    public int compareTo(Concert other) {
        return Integer.compare(this.concertId, other.concertId);
    }

    // Returns the concert date
    public String getDate() {
        return date;
    }

    // Returns the venue of the concert
    public Venue getVenue() {
        return venue;
    }

}

// Venue class
class Venue {
    int venueId;
    private String name;
    private String location;
    int capacity;

    // Constructor to initialize a venue with its ID, name, location, and capacity
    public Venue(int venueId, String name, String location, int capacity) {
        this.venueId = venueId;
        this.name = name;
        this.location = location;
        this.capacity = capacity;
    }

    // Returns the name of the venue
    public String getName() {
        return name;
    }

    // Returns the location of the venue
    public String getLocation() {
        return location;
    }
}

// Performer class
class Performer {
    private int performerId;
    private String name;
    private String genre;


    // Constructor to initialize performer with ID, name, and genre
    public Performer(int performerId, String name, String genre) {
        this.performerId = performerId;
        this.name = name;
        this.genre = genre;
    }

    // Returns the name of the performer
    public String getName() {
        return name;
    }
}

// Ticket class
class Ticket {
    private Concert concert;
    private int ticketId;
    private double price;
    private int userId;
    private String userName;

    // Constructor to create a ticket with concert, ticket ID, and price
    public Ticket(Concert concert, int ticketId, double price) {
        this.concert = concert;
        this.ticketId = ticketId;
        this.price = price;
    }

    // Overloaded constructor for ticket with user information
    public Ticket(Concert concert, int ticketId, int price, int userId, String userName) {
        this.concert = concert;
        this.ticketId = ticketId;
        this.price = price;
        this.userId = userId;
        this.userName = userName;
    }

    // Overloaded constructor for ticket with user information
    public int getTicketId() {
        return ticketId;
    }

    // Overloaded constructor for ticket with user information
    public double getPrice() {
        return price;
    }

    // Overloaded constructor for ticket with user information
    public Concert getConcert() {
        return concert;
    }

    // Convert ticket to a single line for saving in a file
    public String toFileString() {
        Venue v = concert.getVenue();
        return "TicketId:" + ticketId +
                ",UserId:" + userId +
                ",ConcertId:" + concert.getConcertId() +
                ",UserName:" + userName +
                ",ConcertTitle:" + concert.getTitle() +
                ",Date:" + concert.getDate() +
                ",VenueId:" + v.venueId +
                ",VenueName:" + v.getName() +
                ",VenueLocation:" + v.getLocation() +
                ",VenueCapacity:" + v.capacity;
    }

    // Reconstruct ticket from file line
    public static Ticket fromFileString(String line) {
        String[] parts = line.split(",");

        int ticketId = Integer.parseInt(parts[0].split(":")[1]);
        int userId = Integer.parseInt(parts[1].split(":")[1]);
        int concertId = Integer.parseInt(parts[2].split(":")[1]);
        String userName = parts[3].split(":")[1];
        String concertTitle = parts[4].split(":")[1];
        String date = parts[5].split(":")[1];

        int venueId = Integer.parseInt(parts[6].split(":")[1]);
        String venueName = parts[7].split(":")[1];
        String venueLocation = parts[8].split(":")[1];
        int venueCapacity = Integer.parseInt(parts[9].split(":")[1]);

        Venue venue = new Venue(venueId, venueName, venueLocation, venueCapacity);
        Concert concert = new Concert(concertId, concertTitle, date, venue);

        return new Ticket(concert, ticketId, 100, userId, userName);


    }

    // Overloaded constructor that initializes all ticket fields
    public Ticket(Concert concert, int ticketId, double price, int userId, String userName) {
        this.concert = concert;
        this.ticketId = ticketId;
        this.price = price;
        this.userId = userId;
        this.userName = userName;
    }


    // Nicely formats ticket details for display
    @Override
    public String toString() {
        return "----- Ticket Details -----\n" +
                "Ticket ID: " + ticketId + "\n" +
                "User: " + userName + " (ID: " + userId + ")\n" +
                "Concert: " + concert.getTitle() + " (ID: " + concert.getConcertId() + ")\n" +
                "Date: " + concert.getDate() + "\n" +
                "Venue: " + concert.getVenue().getName() + ", " + concert.getVenue().getLocation() + "\n" +
                "Price: $" + price + "\n---------------------------";
    }
}


// ConcertManager class
class ConcertManager {
    ArrayList<Concert> concerts = new ArrayList<>();
    HashMap<Integer, User> users = new HashMap<>();
    public static Stack<Ticket> recentTickets = new Stack<>();
    public UserBST userTree = new UserBST();


    // Nicely formats ticket details for display
    public HashMap<Integer, User> getUsers() {
        return users;
    }

    // Adds a new user to both the HashMap and the UserBST
    public void addUser(User user) {
        users.put(user.userId, user);
        userTree.insert(user); // Insert into BST too
    }

    // Adds a new user to both the HashMap and the UserBST
    public void replaceUser(User user) {
        if(searchUserById(user.userId)==null) {
            users.put(user.userId, user);

        }

    }

    // Adds a new user to both the HashMap and the UserBST
    public User searchUserById(int id) {
        return userTree.search(id);
    }

//    public void addUser(User user) {
//        users.put(user.userId, user);
//    }

    // Adds a new user to both the HashMap and the UserBST
    public void displayAllUsers() {
        for (User user : users.values()) {
            user.displayInfo();
        }
    }

    // Adds a new user to both the HashMap and the UserBST
    public void addConcert(Concert concert) {
        concerts.add(concert);
        Collections.sort(concerts);
    }

    // Displays all concerts using their display method
    public void displayAllConcerts() {
        for (Concert c : concerts) {
            c.displayConcert();
        }
    }

    // Searches a concert by its title (case-insensitive)
    public Concert searchConcertByTitle(String title) {
        for (Concert c : concerts) {
            if (c.getTitle().equalsIgnoreCase(title)) {
                return c;
            }
        }
        return null;
    }

    // Searches for a concert by ID using binary search (concerts list must be sorted)
    public Concert searchConcertById(int id) {
        int low = 0, high = concerts.size() - 1;
        while (low <= high) {
            int mid = (low + high) / 2;
            Concert midConcert = concerts.get(mid);
            if (midConcert.getConcertId() == id) return midConcert;
            else if (midConcert.getConcertId() < id) low = mid + 1;
            else high = mid - 1;
        }
        return null;
    }

    //using stack to view recently booked ticket
    public void displayRecentBookings() {
        System.out.println("\n--- Recently Booked Tickets ---");
        if (recentTickets.isEmpty()) {
            System.out.println("No tickets have been booked yet.");
            return;
        }

        System.out.println(recentTickets.peek());

    }
}

