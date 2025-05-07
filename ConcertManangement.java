package BinarySearch;


import java.util.*;

// Abstract User class
abstract class User {
    protected int userId;
    protected String name;
    protected String email;

    public User(int userId, String name, String email) {
        this.userId = userId;
        this.name = name;
        this.email = email;
    }

    public abstract void displayInfo();
}

// Admin class
class Admin extends User {

    public Admin(int userId, String name, String email) {
        super(userId, name, email);
    }

    public void addConcert(ConcertManager manager, Concert concert) {
        manager.addConcert(concert);
        System.out.println("Concert added successfully.");
    }

//    public void viewAllUsers(ConcertManager manager) {
//        manager.displayAllUsers();
//    }

    @Override
    public void displayInfo() {
        System.out.println("Admin: " + name + " (ID: " + userId + ")");
    }
    public void viewAllUsers(ConcertManager manager) {
        System.out.println("\n--- User List ---");
        System.out.printf("%-10s %-20s %-15s %-20s %-10s\n", "User ID", "Name", "Concert ID", "Concert Title", "Price");

        // Iterate over all users
        for (User user : manager.getUsers().values()) {
            if (user instanceof Attendee) {
                Attendee attendee = (Attendee) user;
                for (Ticket ticket : attendee.tickets) {
                    // Print the user and ticket information in a formatted manner
                    System.out.printf("%-10d %-20s %-15d %-20s $%-10.2f\n",
                            attendee.userId,
                            attendee.getName(),
                            ticket.getConcert().getConcertId(),
                            ticket.getConcert().getTitle(),
                            ticket.getPrice());
                }
            }
        }
    }
}

// Attendee class
class Attendee extends User {
    LinkedList<Ticket> tickets = new LinkedList<>();  // List of tickets booked by the user

    public Attendee(int userId, String name) {
        super(userId, name, name.toLowerCase().replace(" ", "") + "@example.com");
    }

    public void bookTicket(Concert concert) {
        Ticket ticket = new Ticket(concert, tickets.size() + 1, 100.0);
        tickets.add(ticket);
        System.out.println("Ticket booked for concert: " + concert.getTitle());
    }

    @Override
    public void displayInfo() {
        System.out.println("Attendee: " + name + " (ID: " + userId + ")");
        // Display all the tickets booked by the user
        if (tickets.isEmpty()) {
            System.out.println("No tickets booked yet.");
        } else {
            System.out.println("Tickets booked:");
            for (Ticket ticket : tickets) {
                System.out.println("Ticket ID: " + ticket.getTicketId() + ", Concert: " + ticket.getConcert().getTitle() + ", Price: $" + ticket.getPrice());
            }
        }
    }

    public String getName() {
        return name;  // Add this getter method to access the name
    }
}


// Concert class
class Concert implements Comparable<Concert> {
    private int concertId;
    private String title;
    private String date;
    private Venue venue;
    private ArrayList<Performer> performers;

    public Concert(int concertId, String title, String date, Venue venue) {
        this.concertId = concertId;
        this.title = title;
        this.date = date;
        this.venue = venue;
        this.performers = new ArrayList<>();
    }

    public void addPerformer(Performer performer) {
        performers.add(performer);
    }

    public int getConcertId() {
        return concertId;
    }

    public String getTitle() {
        return title;
    }

    public void displayConcert() {
        System.out.println("Concert ID: " + concertId + ", Title: " + title + ", Date: " + date);
        System.out.println("Venue: " + venue.getName() + ", Location: " + venue.getLocation());
        System.out.print("Performers: ");
        for (Performer p : performers) {
            System.out.print(p.getName() + " ");
        }
        System.out.println("\n-------------------------");
    }

    @Override
    public int compareTo(Concert other) {
        return Integer.compare(this.concertId, other.concertId);
    }
    public String getDate() {
        return date;
    }

    public Venue getVenue() {
        return venue;
    }

}

// Venue class
class Venue {
    private int venueId;
    private String name;
    private String location;
    private int capacity;

    public Venue(int venueId, String name, String location, int capacity) {
        this.venueId = venueId;
        this.name = name;
        this.location = location;
        this.capacity = capacity;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }
}

// Performer class
class Performer {
    private int performerId;
    private String name;
    private String genre;

    public Performer(int performerId, String name, String genre) {
        this.performerId = performerId;
        this.name = name;
        this.genre = genre;
    }

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


        public Ticket(Concert concert, int ticketId, double price) {
            this.concert = concert;
            this.ticketId = ticketId;
            this.price = price;
        }

        public int getTicketId() {
            return ticketId;
        }

        public double getPrice() {
            return price;
        }

        public Concert getConcert() {
            return concert;
        }


    public Ticket(Concert concert, int ticketId, double price, int userId, String userName) {
        this.concert = concert;
        this.ticketId = ticketId;
        this.price = price;
        this.userId = userId;
        this.userName = userName;
    }

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
    private ArrayList<Concert> concerts = new ArrayList<>();
    private HashMap<Integer, User> users = new HashMap<>();
    public HashMap<Integer, User> getUsers() {
        return users;
    }

    public void addUser(User user) {
        users.put(user.userId, user);
    }

    public void displayAllUsers() {
        for (User user : users.values()) {
            user.displayInfo();
            if (user instanceof Attendee) {
                Attendee attendee = (Attendee) user;
                System.out.println("Tickets for " + attendee.getName() + ":");
                if (attendee.tickets.isEmpty()) {
                    System.out.println("No tickets booked.");
                } else {
                    for (Ticket ticket : attendee.tickets) {
                        System.out.println("Ticket ID: " + ticket.getTicketId() + ", Concert: " + ticket.getConcert().getTitle() + ", Price: $" + ticket.getPrice());
                    }
                }
            }
        }
    }

    public void addConcert(Concert concert) {
        concerts.add(concert);
        Collections.sort(concerts); // Maintain sorted order for binary search
    }

    public void displayAllConcerts() {
        for (Concert c : concerts) {
            c.displayConcert();
        }
    }

    // Linear search by title
    public Concert searchConcertByTitle(String title) {
        for (Concert c : concerts) {
            if (c.getTitle().equalsIgnoreCase(title)) {
                return c;
            }
        }
        return null;
    }

    // Binary search by concert ID
    public Concert searchConcertById(int id) {
        int low = 0, high = concerts.size() - 1;
        while (low <= high) {
            int mid = (low + high) / 2;
            Concert midConcert = concerts.get(mid);
            if (midConcert.getConcertId() == id) {
                return midConcert;
            } else if (midConcert.getConcertId() < id) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return null;
    }
}

// Main class

public class ConcertManangement {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ConcertManager manager = new ConcertManager();

        // Sample data
        Admin admin = new Admin(1, "AdminUser", "admin@example.com");
        Attendee sampleAttendee = new Attendee(2, "John Doe");

        manager.addUser(admin);
        manager.addUser(sampleAttendee);

        Venue venue1 = new Venue(1, "City Hall", "Downtown", 5000);
        Concert concert1 = new Concert(101, "Rock Night", "2025-06-01", venue1);
        Performer performer1 = new Performer(1, "The Rockers", "Rock");

        concert1.addPerformer(performer1);
        admin.addConcert(manager, concert1);

        boolean running = true;
        while (running) {
            System.out.println("\n--- Concert Management System ---");
            System.out.println("1. View Concerts");
            System.out.println("2. Search Concert by Title (Linear Search)");
            System.out.println("3. Search Concert by ID (Binary Search)");
            System.out.println("4. Book Ticket");
            System.out.println("5. View Users");
            System.out.println("6. Add a New Concert");
            System.out.println("0. Exit");

            System.out.print("Select an option: ");
            int choice = sc.nextInt();
            sc.nextLine(); // Clear buffer

            switch (choice) {
                case 1:
                    manager.displayAllConcerts();
                    break;

                case 2:
                    System.out.print("Enter concert title: ");
                    String title = sc.nextLine();
                    Concert searchTitle = manager.searchConcertByTitle(title);
                    if (searchTitle != null) {
                        searchTitle.displayConcert();
                    } else {
                        System.out.println("Concert not found.");
                    }
                    break;

                case 3:
                    System.out.print("Enter concert ID: ");
                    int id = sc.nextInt();
                    Concert searchId = manager.searchConcertById(id);
                    if (searchId != null) {
                        searchId.displayConcert();
                    } else {
                        System.out.println("Concert not found.");
                    }
                    break;

                case 4:
                    manager.displayAllConcerts();
                    System.out.print("Enter your user ID: ");
                    int userId = sc.nextInt();
                    sc.nextLine(); // Clear buffer

                    Attendee attendee;
                    if (!manager.getUsers().containsKey(userId)) {
                        System.out.print("Enter your name: ");
                        String name = sc.nextLine();
                        attendee = new Attendee(userId, name);
                        manager.addUser(attendee);
                    } else {
                        attendee = (Attendee) manager.getUsers().get(userId);
                    }

                    System.out.print("Enter Concert ID to book ticket: ");
                    int concertIdToBook = sc.nextInt();
                    Concert concertToBook = manager.searchConcertById(concertIdToBook);
                    if (concertToBook != null) {
                        attendee.bookTicket(concertToBook);

                        // ✅ Show the latest ticket booked
                        Ticket latestTicket = attendee.tickets.getLast();
                        System.out.println("\nYour Ticket:");
                        System.out.println(latestTicket);
                    } else {
                        System.out.println("Concert not found.");
                    }
                    break;

                case 5:
                    admin.viewAllUsers(manager);
                    break;

                case 6:
                    System.out.print("Enter Concert ID: ");
                    int newConcertId = sc.nextInt();
                    sc.nextLine(); // clear buffer

                    System.out.print("Enter Concert Title: ");
                    String newTitle = sc.nextLine();

                    System.out.print("Enter Concert Date (YYYY-MM-DD): ");
                    String newDate = sc.nextLine();

                    System.out.print("Enter Venue ID: ");
                    int venueId = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Enter Venue Name: ");
                    String venueName = sc.nextLine();

                    System.out.print("Enter Venue Location: ");
                    String venueLoc = sc.nextLine();

                    System.out.print("Enter Venue Capacity: ");
                    int capacity = sc.nextInt();
                    sc.nextLine();

                    Venue newVenue = new Venue(venueId, venueName, venueLoc, capacity);
                    Concert newConcert = new Concert(newConcertId, newTitle, newDate, newVenue);

                    System.out.print("Enter number of performers: ");
                    int performerCount = sc.nextInt();
                    sc.nextLine();

                    for (int i = 0; i < performerCount; i++) {
                        System.out.print("Enter Performer ID: ");
                        int pid = sc.nextInt();
                        sc.nextLine();

                        System.out.print("Enter Performer Name: ");
                        String pname = sc.nextLine();

                        System.out.print("Enter Performer Genre: ");
                        String pgenre = sc.nextLine();

                        Performer perf = new Performer(pid, pname, pgenre);
                        newConcert.addPerformer(perf);
                    }

                    admin.addConcert(manager, newConcert);
                    System.out.println("Concert added successfully.");
                    break;

                case 0:
                    running = false;
                    System.out.println("Exiting...");
                    break;

                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
        sc.close();
    }
}


